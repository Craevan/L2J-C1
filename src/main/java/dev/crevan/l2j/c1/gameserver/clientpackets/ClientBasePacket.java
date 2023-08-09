package dev.crevan.l2j.c1.gameserver.clientpackets;

import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public abstract class ClientBasePacket {

    private static final Logger log = Logger.getLogger(ClientBasePacket.class.getName());

    private final byte[] decrypt;
    private int off;

    public ClientBasePacket(final byte[] decrypt) {
        log.finest(getType());
        this.decrypt = decrypt;
        this.off = 1;
    }

    public abstract String getType();

    public int readD() {
        int result = decrypt[off++] & 0xff;
        result |= decrypt[off++] << 8 & 0xff00;
        result |= decrypt[off++] << 0x10 & 0xff0000;
        result |= decrypt[off++] << 0x18 & 0xff000000;
        return result;
    }

    public int readC() {
        return decrypt[off++] & 0xff;
    }

    public int readH() {
        int result = decrypt[off++] & 0xff;
        result |= decrypt[off++] << 8 & 0xff00;
        return result;
    }

    public double readF() {
        long result = decrypt[off++] & 0xff;
        result |= decrypt[off++] << 8 & 0xff00;
        result |= decrypt[off++] << 0x10 & 0xff0000;
        result |= decrypt[off++] << 0x18 & 0xff000000;
        result |= (long) decrypt[off++] << 0x20 & 0xff00000000L;
        result |= (long) decrypt[off++] << 0x28 & 0xff0000000000L;
        result |= (long) decrypt[off++] << 0x30 & 0xff000000000000L;
        result |= (long) decrypt[off++] << 0x38 & 0xff00000000000000L;
        return Double.longBitsToDouble(result);
    }

    public String readS() {
        String result;
        try {
            result = new String(decrypt, off, decrypt.length - off, StandardCharsets.UTF_16LE);
            result = result.substring(0, result.indexOf(0x00));
        } catch (Exception e) {
            result = "";
        }

        off += result.length() * 2 + 2;
        return result;
    }

    public byte[] readB(int length) {
        byte[] result = new byte[length];
        System.arraycopy(decrypt, off, result, 0, length);
        off += length;

        return result;
    }
}
