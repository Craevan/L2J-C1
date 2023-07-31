package dev.crevan.l2j.c1.loginserver.crypt;

import java.io.IOException;

public class NewCrypt {

    BlowfishEngine crypt;
    BlowfishEngine decrypt;

    public NewCrypt(final String key) {
        byte[] keyBytes = key.getBytes();
        this.crypt = new BlowfishEngine();
        this.crypt.init(true, keyBytes);
        this.decrypt = new BlowfishEngine();
        this.decrypt.init(false, keyBytes);
    }

    public boolean checksum(final byte[] raw) {
        long checksum = 0;
        int count = raw.length - 8;
        int i;
        for (i = 0; i < count; i++) {
            checksum ^= calculate(raw, i);
        }

        long ecx = calculate(raw, i);
        raw[i] = (byte) (checksum & 0xff);
        raw[i + 1] = (byte) (checksum >> 0x08 & 0xff);
        raw[i + 2] = (byte) (checksum >> 0x10 & 0xff);
        raw[i + 3] = (byte) (checksum >> 0x18 & 0xff);

        return ecx == checksum;
    }

    public byte[] crypt(final byte[] raw) throws IOException {
        int count = raw.length / 8;
        byte[] result = new byte[raw.length];
        for (int i = 0; i < count; i++) {
            crypt.processBlock(raw, i * 8, result, i * 8);
        }

        return result;
    }

    public byte[] decrypt(final byte[] raw) throws IOException {
        int count = raw.length / 8;
        byte[] result = new byte[raw.length];
        for (int i = 0; i < count; i++) {
            decrypt.processBlock(raw, i * 8, result, i * 8);
        }

        return result;
    }

    private long calculate(final byte[] raw, final int index) {
        long ecx = raw[index] & 0xff;
        ecx |= raw[index + 1] << 8 & 0xff00;
        ecx |= raw[index + 2] << 0x10 & 0xff0000;
        ecx |= raw[index + 3] << 0x18 & 0xff000000;

        return ecx;
    }
}
