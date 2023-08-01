package dev.crevan.l2j.c1.loginserver.serverpacket;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ServerList extends ServerBasePacket {

    private final List<ServerData> servers;

    public ServerList() {
        this.servers = new ArrayList<>();
    }

    public void addServer(final String ip, final int port, final boolean pvp, final int currentPlayers,
                          final int maxPlayers, final boolean testServer) {
        servers.add(new ServerData(ip, port, pvp, currentPlayers, maxPlayers, testServer));

    }

    @Override
    public byte[] getContent() {
        writeC(0x04);
        writeC(servers.size());
        writeC(0x00);

        for (int i = 0; i < servers.size(); i++) {
            ServerData serverData = servers.get(i);

            writeC(i + 1);

            try {
                InetAddress address = InetAddress.getByName(serverData.ip);
                byte[] raw = address.getAddress();
                writeC(raw[0] & 0xff);
                writeC(raw[1] & 0xff);
                writeC(raw[2] & 0xff);
                writeC(raw[3] & 0xff);
            } catch (UnknownHostException hostException) {
                hostException.printStackTrace();
                writeC(127);
                writeC(0);
                writeC(0);
                writeC(1);
            }

            writeD(serverData.port);
            writeC(0x0f);

            if (serverData.pvp) {
                writeC(0x01);
            } else {
                writeC(0x00);
            }

            writeH(serverData.currentPlayers);
            writeH(serverData.maxPlayers);
            writeC(0x01);

            if (serverData.testServer) {
                writeD(0x04);
            } else {
                writeD(0x00);
            }
        }
        return getBytes();
    }

    private static class ServerData {

        private final String ip;
        private final int port;
        private final boolean pvp;
        private final int currentPlayers;
        private final int maxPlayers;
        private final boolean testServer;

        public ServerData(final String ip, final int port, final boolean pvp,
                          final int currentPlayers, final int maxPlayers, final boolean testServer) {
            this.ip = ip;
            this.port = port;
            this.pvp = pvp;
            this.currentPlayers = currentPlayers;
            this.maxPlayers = maxPlayers;
            this.testServer = testServer;
        }
    }
}
