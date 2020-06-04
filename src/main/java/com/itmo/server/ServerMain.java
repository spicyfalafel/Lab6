package com.itmo.server;

import ch.qos.logback.classic.Logger;
import com.itmo.client.Client;
import org.slf4j.LoggerFactory;

public class ServerMain {
    public static Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Server.class);
    public static void main(String[] args){

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
            Server server = new Server(port, logger);
            server.run();
        }
    }
}
