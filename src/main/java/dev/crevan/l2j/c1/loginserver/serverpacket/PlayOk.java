package dev.crevan.l2j.c1.loginserver.serverpacket;

public class PlayOk extends ServerBasePacket {

    public PlayOk(final int sessionKey) {
        writeC(0x07);
        writeD(sessionKey);
        writeD(0x55667788);
    }

    @Override
    public byte[] getContent() {
        return getBytes();
    }
}
