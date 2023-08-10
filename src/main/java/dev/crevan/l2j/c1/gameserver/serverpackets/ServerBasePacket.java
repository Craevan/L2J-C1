package dev.crevan.l2j.c1.gameserver.serverpackets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public abstract class ServerBasePacket {

    private static final Logger log = Logger.getLogger(ServerBasePacket.class.getName());

    ByteArrayOutputStream bao;

    protected ServerBasePacket() {
        bao = new ByteArrayOutputStream();
        log.finest(getType());
    }

    protected void writeD(int value) {
        bao.write(value & 0xff);
        bao.write(value >> 8 & 0xff);
        bao.write(value >> 16 & 0xff);
        bao.write(value >> 24 & 0xff);
    }

    protected void writeH(int value) {
        bao.write(value & 0xff);
        bao.write(value >> 8 & 0xff);
    }

    protected void writeC(int value) {
        bao.write(value & 0xff);
    }

    protected void writeF(double org) {
        long value = Double.doubleToRawLongBits(org);
        bao.write((int) (value & 0xff));
        bao.write((int) (value >> 8 & 0xff));
        bao.write((int) (value >> 16 & 0xff));
        bao.write((int) (value >> 24 & 0xff));
        bao.write((int) (value >> 32 & 0xff));
        bao.write((int) (value >> 40 & 0xff));
        bao.write((int) (value >> 48 & 0xff));
        bao.write((int) (value >> 56 & 0xff));
    }

    protected void writeS(String text) {
        try {
            if (text != null) {
                bao.write(text.getBytes(StandardCharsets.UTF_16LE));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        bao.write(0);
        bao.write(0);
    }

    public byte[] getBytes() {
        return bao.toByteArray();
    }

    public abstract byte[] getContent() throws IOException;

    public abstract String getType();
}
