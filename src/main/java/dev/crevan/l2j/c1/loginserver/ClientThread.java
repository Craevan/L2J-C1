package dev.crevan.l2j.c1.loginserver;

import dev.crevan.l2j.c1.loginserver.clientpacket.RequestAuthLogin;
import dev.crevan.l2j.c1.loginserver.clientpacket.RequestServerList;
import dev.crevan.l2j.c1.loginserver.clientpacket.RequestServerLogin;
import dev.crevan.l2j.c1.loginserver.controller.LoginController;
import dev.crevan.l2j.c1.loginserver.crypt.NewCrypt;
import dev.crevan.l2j.c1.loginserver.exception.HackingException;
import dev.crevan.l2j.c1.loginserver.serverpacket.*;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

// TODO
public class ClientThread extends Thread {

    private static final Logger log = Logger.getLogger(ClientThread.class.getName());
    private static final List<String> bannedIP = new ArrayList<>();

    private InputStream is;
    private OutputStream os;
    private NewCrypt crypt;
    private Logins logins;

    private Socket socket;
    private String gameServerHost;
    private int gameServerPort;

    public ClientThread(final Socket client, final Logins logins, final String host, final int port) throws IOException {
        this.socket = client;
        String ip = client.getInetAddress().getHostAddress();
        if (bannedIP.contains(ip)) {
            throw new IOException("banned IP");
        }

        this.is = client.getInputStream();
        this.os = new BufferedOutputStream(client.getOutputStream());
        this.crypt = new NewCrypt("[;'.]94-31==-%&@!^+\000");
        this.logins = logins;
        this.gameServerHost = host;
        this.gameServerPort = port;

        start();
    }

    public static void addBannedIP(final String ip) {
        bannedIP.add(ip);
    }

    private void sendPacket(final ServerBasePacket serverBasePacket) {
        //TODO
    }

    private String fillHex(final int data, final int digits) {
        StringBuilder number = new StringBuilder(Integer.toHexString(data));
        for (int i = number.length(); i < digits; i++) {
            number.insert(0, "0");
        }
        return number.toString();
    }

    private String printData(final byte[] data, final int len) {
        // TODO
        return null;
    }

    @Override
    public void run() {
        log.fine("Login Server thread started");

        int lengthHi;
        int lengthLo;
        int length;
        int sessionKey = 1;
        String account = null;
        String gameServerIp;

        try {
            InetAddress address = InetAddress.getByName(gameServerHost);
            gameServerIp = address.getHostAddress();

            Init startPacket = new Init();
            os.write(startPacket.getLength() & 0xff);
            os.write(startPacket.getLength() >> 8 & 0xff);
            os.write(startPacket.getContent());
            os.flush();

            while (true) {
                lengthLo = is.read();
                lengthHi = is.read();
                length = lengthHi * 256 + lengthLo;

                if (length < 0) {
                    log.warning("Client terminated the connection");
                    break;
                }

                byte[] incoming = new byte[length];
                incoming[0] = (byte) lengthLo;
                incoming[1] = (byte) lengthHi;

                int receivedBytes = 0;
                int newBytes = 0;
                while (newBytes != -1 && receivedBytes < (length - 2)) {
                    newBytes = is.read(incoming, 2, length - 2);
                    receivedBytes = receivedBytes + newBytes;
                }

                if (receivedBytes != length - 2) {
                    log.warning("Incomplete Packet is sent to the server, closing connection.");
                    break;
                }

                byte[] decrypt = new byte[length - 2];
                System.arraycopy(incoming, 2, decrypt, 0, decrypt.length);
                decrypt = crypt.decrypt(decrypt);

                log.finest("[C]\n" + printData(decrypt, decrypt.length));

                int packetType = decrypt[0] & 0xff;
                switch (packetType) {
                    case 0: {
                        RequestAuthLogin requestAuthLogin = new RequestAuthLogin(decrypt);
                        account = requestAuthLogin.getUser().toLowerCase();
                        log.fine("RequestAuthLogin from user: " + account);

                        LoginController controller = LoginController.getInstance();

                        if (logins.loginValid(account, requestAuthLogin.getPassword(), socket.getInetAddress())) {
                            if (!controller.isAccountIngameServer(account) && !controller.isAccountInLoginServer(account)) {
                                int accessLevel = logins.getAccessLevel(account);
                                if (accessLevel < 0) {
                                    LoginFail loginFail = new LoginFail(LoginFail.REASON_ACCOUNT_BANNED);
                                    sendPacket(loginFail);
                                } else {
                                    sessionKey = controller.assignSessionKeyToLogin(account, accessLevel, socket);
                                    log.fine("assigned SessionKey:" + Integer.toHexString(sessionKey));
                                    LoginOk lok = new LoginOk();
                                    sendPacket(lok);
                                }
                            } else {
                                log.fine("KICK!");
                                if (controller.isAccountInLoginServer(account)) {
                                    log.warning("Account: " + account + " is in use on Login server (kicking off)");
                                    controller.getLoginServerConnection(account).close();
                                    controller.removeLoginServerLogin(account);
                                }
                                if (controller.isAccountIngameServer(account)) {
                                    log.warning("Account: " + account + " is in use on Game server (kicking off)");
                                    controller.getClientConnection(account).close();
                                    controller.removeGameServerLogin(account);
                                }
                                LoginFail loginFail = new LoginFail(LoginFail.REASON_ACCOUNT_IN_USE);
                                sendPacket(loginFail);
                            }
                        } else {
                            LoginFail loginFail = new LoginFail(LoginFail.REASON_USER_OR_PASS_WRONG);
                            sendPacket(loginFail);
                        }
                        break;
                    }
                    case 2: {
                        log.fine("Request Server Login");
                        RequestServerLogin rsl = new RequestServerLogin(decrypt);
                        log.fine("Login to Server: " + rsl.getData3());
                        PlayOk playOk = new PlayOk(sessionKey);
                        sendPacket(playOk);
                        break;
                    }
                    case 5: {
                        log.fine("Request Server List");
                        RequestServerList rsl = new RequestServerList(decrypt);
                        ServerList sl = new ServerList();
                        int current = LoginController.getInstance().getOnlinePlayerCount();
                        int max = LoginController.getInstance().getMaxAllowedPlayers();
                        sl.addServer(gameServerIp, gameServerPort, true, current, max, false);
                        sendPacket(sl);
                        break;
                    }
                    default: {
                        log.warning("Unknown packet: " + packetType);
                        log.warning(printData(decrypt, decrypt.length));
                    }
                }
            }
        } catch (HackingException he) {
            log.warning("IP: " + he.getIp() + " will be banned");
            bannedIP.add(he.getIp());
        } catch (UnknownHostException e) {
            log.warning("Unknown Host");
            e.printStackTrace();
        } catch (IOException e) {
            log.warning("I/O Exception");
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException ioe) {
                log.warning("Error while socket closing");
                ioe.printStackTrace();
            }
        }
        LoginController.getInstance().removeLoginServerLogin(account);
        log.fine("Login Server thread[C] stopped");
    }
}
