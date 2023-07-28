package dev.crevan.l2j.c1.loginserver.serverpacket;

public class Init {
    private static final byte[] content = {
            (byte)0x00, (byte)0x9c, (byte)0x77, (byte)0xed,
            (byte)0x03, (byte)0x5a, (byte)0x78, (byte)0x00,
            (byte)0x00
    };

    public byte[] getContent() {
        return content;
    }

    public int getLength() {
        return content.length + 2;
    }
}
