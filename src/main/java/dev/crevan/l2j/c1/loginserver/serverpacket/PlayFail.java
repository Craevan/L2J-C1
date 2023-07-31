package dev.crevan.l2j.c1.loginserver.serverpacket;

public class PlayFail extends ServerBasePacket {

    public static int REASON_TOO_MANY_PLAYERS = 0x0f;
    public static int REASON1 = 0x01; // account in use
    public static int REASON2 = 0x02;
    public static int REASON3 = 0x03;
    public static int REASON4 = 0x04;

    public PlayFail(final int reason) {
        writeC(0x06);
        writeC(reason);
    }

    @Override
    public byte[] getContent() {
        return getBytes();
    }
}
