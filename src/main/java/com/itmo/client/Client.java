package com.itmo.client;
import com.itmo.Exceptions.NoSuchCommandException;
import com.itmo.Exceptions.WrongArgumentsNumberException;
import com.itmo.app.SerializationManager;
import com.itmo.commands.*;
import com.itmo.server.Response;

import java.net.*;

import java.io.*;
import java.util.Scanner;

public class Client {
    private static CommandsInvoker invoker;
    public static Socket socket;
    private static boolean notExit = true;
    private static final Scanner scanner = new Scanner(System.in);
    private final String host;
    private final int port;
    public void run() {
        registerCommands();
        connect();
        while (notExit) {
            handshake();
        }
        scanner.close();
        try {
            socket.close();
        } catch (IOException ignored) {
        }
    }
    public Client(String host, int port){
        this.host = host;
        this.port = port;
    }

    public static void sendOneByte() throws IOException {
        byte[] b = new byte[1];
        b[0] = (byte) 127;
        socket.getOutputStream().write(b);
    }

    public void handshake(){
        if(socket==null) {
            System.out.println("socket==null");
            return;
        }
        try{
            Command command = scanCommandFromConsole();
            sendOneByte();
            sendCommand(command);
            getAnswer();
            if(command instanceof ExitCommand) {
                notExit = false;
            }
        } catch (IOException e) {
            System.out.println("Потеря соединения");
        }catch (ClassNotFoundException e){
            System.out.println("Ошибка при сериализации");
        }
    }

    private Command scanCommandFromConsole(){
        Command command = null;
        do{
            if(scanner.hasNextLine()){
                command = getCommandFromString(scanner.nextLine());
            }
        }while(command==null);
        command.clientInsertion();
        return command;
    }

    public static void getAnswer() throws IOException, ClassNotFoundException {
        byte[] buff = new byte[4096];
        int got = socket.getInputStream().read(buff);
        if(got>0){
            Response r = SerializationManager.readObject(buff);
            System.out.println(r.getAnswer());
        }
    }
    private void sendCommand(Command command) throws IOException {
        byte[] serializedCommand = SerializationManager.writeObject(command);
        socket.getOutputStream().write(serializedCommand);
    }

    public static Command getCommandFromString(String command){
        String[] splitted = command.split(" ");
        String commandName = splitted[0];
        String[] arguments = new String[splitted.length - 1];
        System.arraycopy(splitted, 1, arguments, 0, splitted.length - 1);
        try {
            return invoker.validateCommand(commandName, arguments);
        } catch (WrongArgumentsNumberException e) {
            System.out.println("WrongArgumentsNumberException");
            return null;
        }catch (NoSuchCommandException e){
            return null;
        }
    }

    private void connect()  {
        System.out.println("Пытаюсь установить соединение с сервером");
        while (true) {
            try {
                InetAddress addr = InetAddress.getByName(host);
                socket = new Socket(addr, port);
                System.out.println("socket = " + socket);
                return;
            } catch (UnknownHostException e) {
                System.out.println("Неправильно указан хост");
            } catch (IOException e) {
                System.out.println("Сервер отключен");
            }
        }
    }

    private void registerCommands(){
        invoker = CommandsInvoker.getInstance();
        invoker.register("info", new InfoCommand( null));
        invoker.register("help", new HelpCommand(null));
        invoker.register("exit", new ExitCommand(null));
        invoker.register("clear", new ClearCommand(null));
        invoker.register("remove_by_id", new RemoveByIdCommand(null));
        invoker.register("add", new AddElementCommand(null));
        invoker.register("show", new ShowCommand(null));
        invoker.register("add", new AddElementCommand(null));
        invoker.register("update", new UpdateIdCommand(null));
        invoker.register("filter_starts_with_name", new FilterStartsWithNameCommand(null));
        invoker.register("add_if_max", new AddIfMaxCommand(null));
        invoker.register("add_if_min", new AddIfMinCommand(null));
        invoker.register("remove_lower", new RemoveLowerThanElementCommand(null));
        invoker.register("print_field_ascending_wingspan", new PrintFieldAscendingWingspanCommand(null));
        invoker.register("print_descending", new PrintDescendingCommand(null));
        //invoker.register("save", new SaveCommand(mainReceiver, null));
        invoker.register("execute_script", new ExecuteScriptCommand(null));
    }
}