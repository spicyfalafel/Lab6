package com.itmo.commands;

import com.itmo.app.SerializationManager;
import com.itmo.client.Client;
import com.itmo.server.Response;
import com.itmo.server.Server;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Scanner;

/**
 * The type Execute script command.
 */
public class ExecuteScriptCommand extends Command {

    /**
     * Instantiates a new Command.
     *
     * @param args
     */
    public ExecuteScriptCommand(String[] args) {
        super(args);
    }

    @Override
    public int getNumberOfRequiredArgs() {
        return 1;
    }

    @Override
    public void clientInsertion() {
        try (BufferedReader reader = new BufferedReader(new FileReader(args[0]))) {
            String line;
            System.out.println("Запуск скрипта " + args[0]);
            while ((line = reader.readLine()) != null) {
                Command c = Client.getCommandFromString(line);
                if (c != null) {
                    c.clientInsertion();
                    byte[] serializedCommand = SerializationManager.writeObject(c);
                    Client.socket.getOutputStream().write(serializedCommand);
                    byte[] buff = new byte[4096];
                    int got = Client.socket.getInputStream().read(buff);
                    Response r = SerializationManager.readObject(buff);
                    System.out.println(r.getAnswer());
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
        } catch (IOException e) {
            System.out.println("капут");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (StackOverflowError e){
            System.out.println("Стэк переполнен!");
        }

    }


    /**
     * скрипт выполняется путём вызова метода выполнения команды из com.itmo.Main.
     * это немного нарушает концепцию шаблона Команда, но это крайне удобно
     * @return

     */
    @Override
    public String execute(CommandReceiver receiver) {
        return "script done";
    }

    @Override
    public String getDescription() {
        return "считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.";
    }
}
