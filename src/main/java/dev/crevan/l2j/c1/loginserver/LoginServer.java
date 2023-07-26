package dev.crevan.l2j.c1.loginserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.logging.Logger;

public class LoginServer extends Thread {

    private static final Logger log = Logger.getLogger(LoginServer.class.getName());

    private final int gamePort;
    private final Logins logins;

    private ServerSocket serverSocket;
    private String externalHostName;
    private String internalHostName;

    public static void main(String[] args) {
        LoginServer loginServer = new LoginServer();
        log.config("LoginServer listening on port 2106");
        loginServer.start();
    }

    public LoginServer() {
        super("LoginServer");

        Properties serverSettings = new Properties();
        try (InputStream is = getClass().getResourceAsStream("/server.cfg")) {
            serverSettings.load(is);
        } catch (IOException ioException) {
            System.err.println("Exception during serverSettings loading");
        }
        String loginIp = serverSettings.getProperty("LoginServerHostName");
        externalHostName = serverSettings.getProperty("ExternalHostname");
        if (externalHostName == null) {
            externalHostName = "localhost";
        }
        internalHostName = serverSettings.getProperty("InternalHostname");
        if (internalHostName == null) {
            internalHostName = "localhost";
        }
        String gamePort = serverSettings.getProperty("GameServerPort");
        String createAccounts = serverSettings.getProperty("AutoCreateAccounts");

        try {
            if (!"*".equals(loginIp)) {
                InetAddress address = InetAddress.getByName(loginIp);
                String ip = address.getHostAddress();
                log.config("LoginServer listening on IP: " + ip + " Port: 2106");
                serverSocket = new ServerSocket(2106, 50, address);
            } else {
                log.config("LoginServer listening on all available IPs on Port 2106");
                serverSocket = new ServerSocket(2106);
            }
        } catch (IOException ioe) {
            log.warning("Error while LoginServer initializing");
        }

        log.config("Hostname for external connection is: " + externalHostName);
        log.config("Hostname for external connection is: " + internalHostName);

        logins = new Logins(Boolean.parseBoolean(createAccounts));
        this.gamePort = Integer.parseInt(gamePort);

        try {
            InputStream bannedFile = getClass().getResourceAsStream("/banned_ip.cfg");

            if (bannedFile != null) {
                int count = 0;
                InputStreamReader reader = new InputStreamReader(bannedFile);
                LineNumberReader lineNumberReader = new LineNumberReader(reader);
                String line;
                while ((line = lineNumberReader.readLine()) != null) {
                    line = line.trim();
                    if (line.length() > 0) {
                        ++count;
                        ClientThread.addBannedIP(line);
                    }
                }
                log.info(count + " banned IPs defined");
            } else {
                log.info("banned_ip.cfg not found");
            }
        } catch (IOException ioe) {
            log.warning("Error while reading file: banned_ip.cfg");
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                log.fine("Waiting for client connection...");
                Socket socket = serverSocket.accept();
                log.fine("Connection from " + socket.getInetAddress());

                String connectedIp = socket.getInetAddress().getHostAddress();
                if (connectedIp.startsWith("192.168") || connectedIp.startsWith("10.")) {
                    log.fine("Using internal IP as server IP " + internalHostName);
                    new ClientThread(socket, logins, internalHostName, gamePort); // TODO ClientThread
                } else {
                    log.fine("Using external IP as server IP " + externalHostName);
                    new ClientThread(socket, logins, externalHostName, gamePort);
                }
            } catch (IOException ioe) {
                System.err.println("[LS exception]");
                ioe.printStackTrace();
            }
        }
    }
}
