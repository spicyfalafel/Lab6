package com.itmo.client;

import com.itmo.commands.FieldsScanner;

public class ClientMain {
    public static void main(String[] args) {
        if(args.length!=1){
            System.out.println("Укажите в аргументах порт (8080)");
            System.exit(1);
        }else{
            int port = 8080;
            try{
                port = Integer.parseInt(args[0]);
            }catch (NumberFormatException e){
                System.out.println("Не целое число, использую 8080");
            }
            Client client = new Client("localhost", port);
            client.run();
        }
    }
}
