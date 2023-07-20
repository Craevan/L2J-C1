package dev.crevan.l2j.c1;

import dev.crevan.l2j.c1.gameserver.GameServer;
import dev.crevan.l2j.c1.loginserver.LoginServer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;

public class Server {
    public static void main(String[] args) throws IOException {
        File logFolder = new File("log");
        logFolder.mkdir();
        try (InputStream is = new Server().getClass().getResourceAsStream("/log.cfg")) {
            LogManager.getLogManager().readConfiguration(is);
        }

//        Starting game & login servers
        new GameServer().start();
        new LoginServer().start();
    }
}
