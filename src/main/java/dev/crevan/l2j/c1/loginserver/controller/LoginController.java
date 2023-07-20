package dev.crevan.l2j.c1.loginserver.controller;

import dev.crevan.l2j.c1.gameserver.net.Connection;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class LoginController {

    private static LoginController instance;

    private final Map<String, Integer> logins;
    private final Map<String, Socket> accountsInLoginServer;
    private final Map<String, Connection> accountsInGameServer;
    private final Map<String, Integer> accessLevels;

    private int maxAllowedPlayers;

    private LoginController() {
        logins = new HashMap<>();
        accountsInGameServer = new HashMap<>();
        accountsInLoginServer = new HashMap<>();
        accessLevels = new HashMap<>();
    }

    public static LoginController getInstance() {
        if (instance == null) {
            instance = new LoginController();
        }
        return instance;
    }

    public int assignSessionKeyToLogin(final String account, final int accessLevel, final Socket socket) {
        int key = (int) (System.currentTimeMillis() & 0xffffff);
        logins.put(account, key);
        accountsInLoginServer.put(account, socket);
        accessLevels.put(account, accessLevel);

        return key;
    }

    public void addGameServerLogin(final String account, final Connection connection) {
        accountsInGameServer.put(account, connection);
    }

    public void removeGameServerLogin(final String account) {
        if (account != null) {
            logins.remove(account);
            accountsInGameServer.remove(account);
        }
    }

    public void removeLoginServerLogin(final String account) {
        if (account != null) {
            Socket socket = accountsInLoginServer.remove(account);
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
    }

    public boolean isAccountInLoginServer(final String account) {
        return accountsInLoginServer.containsKey(account);
    }

    public boolean isAccountIngameServer(final String account) {
        return accountsInGameServer.containsKey(account);
    }

    public int getKeyForAccount(final String account) {
        Integer key = logins.get(account);
        return key == null ? 0 : key;
    }

    public int getOnlinePlayerCount() {
        return accountsInGameServer.size();
    }

    public int getMaxAllowedPlayers() {
        return maxAllowedPlayers;
    }

    public void setMaxAllowedPlayers(final int maxAllowedPlayers) {
        this.maxAllowedPlayers = maxAllowedPlayers;
    }

    public boolean loginPosible(final int accessLevel) {
        return accessLevel >= 50 || accountsInGameServer.size() < maxAllowedPlayers;
    }

    public int getAccessLevel(final String login) {
        return accessLevels.get(login);
    }

    public Connection getClientConnection(final String login) {
        return accountsInGameServer.get(login);
    }

    public Socket getLoginServerConnection(final String login) {
        return accountsInLoginServer.get(login);
    }
}
