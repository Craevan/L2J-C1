package dev.crevan.l2j.c1.loginserver.serverpacket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class ServerBasePacket {
    private final ByteArrayOutputStream bao;

    protected ServerBasePacket() {
        bao = new ByteArrayOutputStream();
    }

    public abstract byte[] getContent() throws Exception;

    public int getLength() {
        return bao.size() + 2;
    }

    public byte[] getBytes() {
        writeD(0x00);

        int padding = bao.size() % 8;
        if (padding != 0) {
            for (int i = padding; i < 8; i++) {
                writeC(0x00);
            }
        }
        return bao.toByteArray();
    }

    protected void writeD(final int value) {
        bao.write(value & 0xff);
        bao.write(value >> 8 & 0xff);
        bao.write(value >> 16 & 0xff);
        bao.write(value >> 24 & 0xff);
    }

    protected void writeH(final int value) {
        bao.write(value & 0xff);
        bao.write(value >> 8 & 0xff);
    }

    protected void writeC(final int value) {
        bao.write(value & 0xff);
    }

    protected void writeS(final String text) {
        try {
            if (text != null) {
                bao.write(text.getBytes(StandardCharsets.UTF_16LE));
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        bao.write(0);
        bao.write(0);
    }
}
