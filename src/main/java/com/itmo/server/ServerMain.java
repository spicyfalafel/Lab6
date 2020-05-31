package com.itmo.server;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

public class ServerMain {
    public static final int PORT = 8080;
    public static Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Server.class);
    public static void main(String[] args){
        Server server = new Server(PORT, logger);
        server.run();
    }
}
