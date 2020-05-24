package com.itmo.server;

import com.itmo.Exceptions.NoSuchDragonException;
import com.itmo.app.MyDragonsCollection;
import com.itmo.app.SerializationManager;
import com.itmo.app.XmlStaff;
import com.itmo.commands.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jdom2.JDOMException;
import org.jdom2.input.JDOMParseException;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Scanner;

public class Server {
    public static final int PORT = 8080;
    private static final int DEFAULT_BUFFER_SIZE = 2048;
    private static final String FILE_ENV = "INPUT_PATH";
    private static final String defaultFileName = "input.xml";
    private static SocketChannel socketChannel;
    private static ServerSocketChannel ssc;
    private static MyDragonsCollection drakoniNelegalnie;
    private static Scanner scanner = new Scanner(System.in);
    public static final Logger log = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args){
        try {
            run();
        } catch (IOException e) {
            log.info("Клиент отключен");
        } catch (NoSuchDragonException e) {
            log.info("Ошибка связанная с драконом которая никогда не вылазит ");
        }
    }

    private static void run() throws IOException, NoSuchDragonException {
        initializeCollection();
        CommandReceiver serverReceiver = new CommandReceiver(drakoniNelegalnie);
        firstConnection(); // теперь клиент точно подключен
        while(sendOneByte()){ // если отправлено значит соединение есть
            Command command = getCommandFromClient();
            String resp=null;
            if(command!=null)  {
                resp = command.execute(serverReceiver);
                sendResponse(resp);
            }
        }
        new SaveCommand().execute(serverReceiver);
        if(scanner.hasNextLine()){
            String[] line = scanner.nextLine().trim().split(" ");
            if(line[0].equals("save")){
                switch (line.length){
                    case 1: new SaveCommand().execute(serverReceiver);
                        log.info("saved");
                        break;
                    case 2: new SaveCommand(line[1]).execute(serverReceiver);
                        log.info("saved in " + line[1]);
                        break;
                    default: log.info("неверное количество аргументов");
                }
            }
        }
    }

    private static boolean sendOneByte() {
        byte[] b = new byte[1];
        b[0] = (byte) 127;
        ByteBuffer buff = ByteBuffer.wrap(b);
        int sended = 0;
        try {
            sended = socketChannel.write(buff);
            log.info("Sended: " + sended+ " (buff:" + Arrays.toString(buff.array()) +")");
        } catch (IOException e) {
            log.info("клиент отключен");
        }
        return sended != 0;
    }

    //•	Модуль приёма подключений.
    private static void firstConnection() throws IOException {
        if(ssc==null){
            ssc = ServerSocketChannel.open();
            log.info("ServerSocketChannel is opened");
            ssc.socket().bind(new InetSocketAddress(PORT));
            ssc.configureBlocking(false);
        }
        while(socketChannel==null){
            socketChannel = ssc.accept();
            if(socketChannel !=null) log.info("Клиент подключился");
        }
    }

    //•	Модуль отправки ответов клиенту.
    private static void sendResponse(String commandResult){
        Response response = new Response(commandResult);
        try {
            byte[] ans = SerializationManager.writeObject(response);
            log.info("response serialized");
            ByteBuffer buffer = ByteBuffer.wrap(ans);
            log.info("response buffered");
            int given = socketChannel.write(buffer);
            log.info("sended response: " + given + " bytes");
        } catch (IOException e) {
            log.error("Error while serialization response");
        }
    }

    //•	Модуль чтения запроса.
    //•	Модуль обработки полученных команд.
    private static Command getCommandFromClient() {
        try {
            Command command;
            ByteBuffer buffer = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE);
            int got = socketChannel.read(buffer);
            if(got>0){
                command = SerializationManager.readObject(buffer.array());
                log.info("получено байт:" + got);
                log.info("Полученная команда: " + command.toString());
                return command;
            }
            log.info("getCommand возвратило null");
            return null;
        } catch (ClassNotFoundException e) {
            log.error("Error while serialization");
            return null;
        } catch (IOException e) {
            log.error("IOException while getting command ");
            return null;
        }
    }

    private static void initializeCollection(){
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
            //log.error("файл не нашелся или нет прав на чтение.");
            drakoniNelegalnie = tryToGetFromDefaultFile();
        } catch (JDOMParseException e) {
            log.error("Данный файл не удалось распарсить, проверьте правильность ввода данных, расширение файла.");
            drakoniNelegalnie = tryToGetFromDefaultFile();
        } catch (JDOMException e) {
            log.error("файл не получилось распарсить");
            drakoniNelegalnie = tryToGetFromDefaultFile();
        }
    }

    public static MyDragonsCollection tryToGetFromDefaultFile() {
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