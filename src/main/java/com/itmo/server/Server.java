package com.itmo.server;

import com.itmo.Exceptions.NoSuchDragonException;
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
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Server {
    private final int DEFAULT_BUFFER_SIZE = 2048;
    private final String FILE_ENV = "INPUT_PATH";
    private final String defaultFileName = "input.xml";
    private SocketChannel socketChannel;
    private ServerSocketChannel ssc;
    private MyDragonsCollection drakoniNelegalnie;
    private final Logger log;
    private CommandReceiver serverReceiver;
    private boolean serverOn = true;
    private final ByteBuffer b = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE);
    private final int port;

    public Server(int port, Logger log){
        this.log = log;
        this.port = port;
    }


    public void run() {
        initializeCollection();
        serverReceiver = new CommandReceiver(drakoniNelegalnie);
        connect(); // теперь клиент точно подключен
        while(serverOn){
            checkServerCommand();
            try {
                checkCommandFromClient();
            } catch (IOException e) {
                log.info("Клиент отключился.");
            } catch (NoSuchDragonException ignored) {
            }
        }
        closeEverything();
    }

    private void checkCommandFromClient() throws IOException, NoSuchDragonException {
        if(checkOneByte()){ // если ПОЛУЧЕНО значит соединение есть
            log.info("получен байт проверки");
            long a = System.currentTimeMillis();
            Command command = getCommandFromClient();
            String resp;
            if(command!=null){
                resp = command.execute(serverReceiver);
                sendResponse(resp);
                if(command instanceof ExitCommand){
                    new SaveCommand().execute(serverReceiver);
                }
            }
            log.info("Команда выполнилась за " + (System.currentTimeMillis()-a) + " миллисекунд");
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
            log.info("checked if there was a command from server");
        }
    }

    private void closeEverything() {
        try {
            if(ssc!=null) ssc.close();
            if(socketChannel!=null) socketChannel.close();
        } catch (IOException ignored) {
        }
        ssc = null;
        socketChannel = null;
    }

    private boolean checkOneByte() throws IOException {
        int bytesFromClient = 0;
        if(socketChannel!=null) bytesFromClient = socketChannel.read(b);
        return (bytesFromClient==1);
    }

    //•	Модуль приёма подключений.
    private void connect() {
        try {
            if (ssc == null) {
                ssc = ServerSocketChannel.open();
                log.info("ServerSocketChannel is opened. Waiting for client.");
                ssc.socket().bind(new InetSocketAddress(port));
                ssc.configureBlocking(false);
            }
            while (socketChannel == null) {
                checkServerCommand();
                socketChannel = ssc.accept();
                if (socketChannel != null){
                    socketChannel.configureBlocking(false);
                    log.info("Клиент подключился");
                }
            }
        } catch (IOException e) {
            log.info("неправильный порт");
        }
    }
    //•	Модуль отправки ответов клиенту.
    private void sendResponse(String commandResult){
        Response response = new Response(commandResult);
        try {
            byte[] ans = SerializationManager.writeObject(response);
            log.info("response serialized");
            ByteBuffer buffer = ByteBuffer.wrap(ans);
            int given = socketChannel.write(buffer);
            log.info("sended response: " + given + " bytes");
        } catch (IOException e) {
            log.error("Error while serialization response");
        }
    }

    //•	Модуль чтения запроса.
    //•	Модуль обработки полученных команд.
    private Command getCommandFromClient() {
        try {
            Command command;
            int got;
            while(b.position()==1){
                got = socketChannel.read(b);
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