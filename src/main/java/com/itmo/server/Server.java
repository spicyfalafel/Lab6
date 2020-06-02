package com.itmo.server;

import com.itmo.collection.MyDragonsCollection;
import com.itmo.app.SerializationManager;
import com.itmo.app.XmlStaff;
import com.itmo.commands.*;
import org.slf4j.Logger;
import org.jdom2.JDOMException;
import org.jdom2.input.JDOMParseException;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

import static com.itmo.server.ServerMain.PORT;

public class Server {
    private final int DEFAULT_BUFFER_SIZE = 2048;
    private final String FILE_ENV = "INPUT_PATH";
    private final String defaultFileName = "input.xml";
    private ServerSocketChannel ssc;
    private MyDragonsCollection drakoniNelegalnie;
    private final Logger log;
    private CommandReceiver serverReceiver;
    private boolean serverOn = true;
    private final ByteBuffer b = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE);
    private final int port;
    private static Selector selector;

    public void run() {
        initializeCollection();
        setupNet();
        while (serverOn) {
            checkClients();
            checkServerCommand();
        }
        new SaveCommand().execute(serverReceiver);
        closeEverything();
    }


    private boolean checkOneByte(SocketChannel channel) throws IOException {
        int bytesFromClient = 0;
        if(channel!=null) bytesFromClient = channel.read(b);
        return (bytesFromClient==1);
    }

    private void checkCommandFromClient(SocketChannel channel) throws IOException {
        if(checkOneByte(channel)){ // если ПОЛУЧЕНО значит соединение есть
            log.info("получен байт проверки");
            long a = System.currentTimeMillis();
            Command command = getCommandFromClient(channel);
            String resp;
            if(command!=null){
                resp = command.execute(serverReceiver);
                sendResponse(resp, channel);
                if(command instanceof ExitCommand){
                    log.info("Клиент вышел");
                }
            }
            log.info("Команда выполнилась за " + (System.currentTimeMillis()-a) + " миллисекунд");
        }
    }


    private void checkAcceptable(SelectionKey selectionKey) throws IOException {
        if(selectionKey.isAcceptable()) {
            SocketChannel channel = ssc.accept();
            if (channel != null) {
                log.info("accepted client");
                try {
                    channel.configureBlocking(false);
                    channel.register(selector, SelectionKey.OP_WRITE);
                } catch (IOException e) {
                    log.error("Unable to use channel");
                    selectionKey.cancel();
                }
            }
        }
    }

    private void checkWritable(SelectionKey selectionKey) throws IOException {
        if(selectionKey.isWritable()) {
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            checkCommandFromClient(socketChannel);
        }
    }


    private void checkClients(){
        try {
            if(selector.selectNow()==0){
                return;
            }
            Set<SelectionKey> keySet = selector.selectedKeys();
            Iterator<SelectionKey> itor = keySet.iterator();
            while (itor.hasNext() && serverOn) {
                SelectionKey selectionKey = itor.next();
                itor.remove();
                checkAcceptable(selectionKey);
                checkWritable(selectionKey);
                checkServerCommand();
            }
        } catch (IOException ioException) {
            log.error("Не удалось проверить клиентов");
        }
    }

    //•	Модуль отправки ответов клиенту.
    private void sendResponse(String commandResult, SocketChannel channel){
        Response response = new Response(commandResult);
        try {
            byte[] ans = SerializationManager.writeObject(response);
            log.info("response serialized");
            ByteBuffer buffer = ByteBuffer.wrap(ans);
            int given = channel.write(buffer);
            log.info("sended response: " + given + " bytes");
        } catch (IOException e) {
            log.error("Error while serialization response");
        }
    }

    //я перепишу метод если надо будет в сервере использовать побольше команд
    private void checkServerCommand() {
        try {
            if(System.in.available()>0){
                String[] line;
                String line1;
                Scanner scanner = new Scanner(System.in);
                if ((line1 = scanner.nextLine()) != null) {
                    line = line1.trim().split(" ");
                    if (line[0].equals("save")) {
                        switch (line.length) {
                            case 1:
                                new SaveCommand().execute(serverReceiver);
                                log.info("saved in default file");
                                break;
                            case 2:
                                new SaveCommand(line[1]).execute(serverReceiver);
                                log.info("saved in " + line[1]);
                                break;
                            default:
                                log.info("неверное количество аргументов");
                        }
                    } else if (line[0].equals("exit") && line.length == 1) {
                        log.info("exiting");
                        scanner.close();
                        closeEverything();
                        serverOn = false;
                    } else {
                        log.info("no such command");
                    }
                }
            }
        } catch (NoSuchElementException | IOException e) {

        }
    }

    private void closeEverything() {
        try {
            if(ssc!=null) ssc.close();
            if(selector!=null) selector.close();
        } catch (IOException ignored) {
        }
        ssc = null;
        selector = null;
    }

    private void setupNet(){
        selector = null;
        ssc = null;
        try {
            ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);
            InetSocketAddress inetSocketAddress = new InetSocketAddress(PORT);
            ssc.socket().bind(inetSocketAddress);
            selector = Selector.open();
            ssc.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            System.out.println("Сервер уже запущен с другого окна...");
        }
    }

    public Server(int port, Logger log){
        this.log = log;
        this.port = port;
    }

    //•	Модуль чтения запроса.
    //•	Модуль обработки полученных команд.
    private Command getCommandFromClient(SocketChannel channel) {
        try {
            Command command;
            int got;
            while(b.position()==1){
                got = channel.read(b);
                if(got!=0) log.info("получено байт:" + got);
            }
            if(b.remaining()!=0){
                command = SerializationManager.readObject(Arrays.copyOfRange(b.array(), 1, b.array().length-1));
                log.info("Полученная команда: " + command.toString());
                b.clear();
                return command;
            }
            return null;
        } catch (ClassNotFoundException e) {
            log.error("Error while serialization");
            return null;
        }catch (StreamCorruptedException e ){ // при попытке десериализации объекта, который не полностью передался
            System.out.println(e.getLocalizedMessage());
            return null;
        } catch(IOException e) {
            return null;
        }
    }

    private void initializeCollection(){
        String fileName = System.getenv(FILE_ENV);
        log.info("имя файла, полученное из переменной окружения:" + fileName);
        try {
            File file = new File("input.xml");
            if (fileName != null) {
                file = new File(fileName);
                if (!file.exists() || file.isDirectory()) {
                    log.info("xml файла по пути " + file.getAbsolutePath() + " не нашлось, использую input.xml");
                    file = new File("input.xml");
                } else {
                    log.info("Файл существует, попытаемся считать коллекцию");
                }
            } else {
                log.info("переменная равна null, использую input.xml");
            }
            drakoniNelegalnie = new MyDragonsCollection(XmlStaff.fromXmlToDragonList(file));
        } catch (FileNotFoundException | NullPointerException e) {
            drakoniNelegalnie = tryToGetFromDefaultFile();
        } catch (JDOMParseException e) {
            log.error("Данный файл не удалось распарсить, проверьте правильность ввода данных, расширение файла.");
            drakoniNelegalnie = tryToGetFromDefaultFile();
        } catch (JDOMException e) {
            log.error("файл не получилось распарсить");
            drakoniNelegalnie = tryToGetFromDefaultFile();
        }finally {
            serverReceiver = new CommandReceiver(drakoniNelegalnie);
        }
    }

    public MyDragonsCollection tryToGetFromDefaultFile() {
        log.info("Попытаемся получить коллекцию из файла " + defaultFileName);
        try {
            return new MyDragonsCollection(XmlStaff.fromXmlToDragonList(new File(defaultFileName)));
        } catch (JDOMException e) {
            log.error("Не удалось распарсить файл " + defaultFileName);
        } catch (FileNotFoundException e) {
            log.error("Не удалось найти файл " + defaultFileName + ". Проверьте, существует ли файл по" +
                    " пути " + (new File(defaultFileName).getAbsolutePath()) + " и ваши на него права.");
        }
        return new MyDragonsCollection();
    }
}