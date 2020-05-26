package com.itmo.client;
import com.itmo.Exceptions.NoSuchCommandException;
import com.itmo.Exceptions.WrongArgumentsNumberException;
import com.itmo.app.SerializationManager;
import com.itmo.commands.*;
import com.itmo.server.Response;
import com.itmo.server.Server;

import java.net.*;

import java.io.*;
import java.util.Scanner;

public class Client {
    private static CommandsInvoker invoker;
    public static Socket socket;
    private static boolean notExit = true;
    private static final byte[] pingpong = new byte[1];
    private static final Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        registerCommands();
        connect();
        while (notExit) {
            try {
                checkOneByte();
                handshake();
            } catch (IOException e) {
                connect();
            }
        }
        scanner.close();
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("socket was closed before");
        }
    }

    private static void checkOneByte() throws IOException {
        while(true){
            int bytesFromServer = socket.getInputStream().read(pingpong);
            if(bytesFromServer==1) return;
        }
    }

    public static void handshake(){
        if(socket==null) {
            System.out.println("socket==null");
            return;
        }
        try{
            Command command = null;
            do{
                if(scanner.hasNextLine()){
                    command = getCommandFromString(scanner.nextLine());
                }
            }while(command==null);
            command.clientInsertion();
            if(command instanceof ExitCommand) notExit = false;
            sendCommand(command);
            getAnswer();
        } catch (IOException e) {
            System.out.println("Сервер отключен");
        }catch (ClassNotFoundException e){
            System.out.println("Ошибка при сериализации");
        }
    }

    private static void getAnswer() throws IOException, ClassNotFoundException {
        byte[] buff = new byte[4096];
        int got = socket.getInputStream().read(buff);
        if(got>0){
            Response r = SerializationManager.readObject(buff);
            System.out.println(r.getAnswer());
        }
    }
    private static void sendCommand(Command command) throws IOException {
        byte[] serializedCommand = SerializationManager.writeObject(command);
        socket.getOutputStream().write(serializedCommand);
    }

    private static void connect()  {
        System.out.println("Пытаюсь установить соединение с сервером");
        while (true) {
            try {
                InetAddress addr = InetAddress.getByName(null);
                socket = new Socket(addr, Server.PORT);
                System.out.println("socket = " + socket);
                return;
            } catch (UnknownHostException e) {
                System.out.println("Неправильно указан хост");
            } catch (IOException e) {
                System.out.println("Сервер отключен");
            }
        }
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

    private static void registerCommands(){
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