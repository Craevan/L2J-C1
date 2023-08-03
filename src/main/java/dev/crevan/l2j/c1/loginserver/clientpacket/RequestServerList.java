package dev.crevan.l2j.c1.loginserver.clientpacket;

public class RequestServerList {

    private final int data3;

    private long data1;
    private long data2;

    public RequestServerList(byte[] rawPacket) {
        data1 = rawPacket[1] & 0xff;
        data1 |= rawPacket[2] << 8 & 0xff00;
        data1 |= rawPacket[3] << 0x10 & 0xff0000;
        data1 |= rawPacket[4] << 0x18 & 0xff000000;

        data2 = rawPacket[5] & 0xff;
        data2 |= rawPacket[6] << 8 & 0xff00;
        data2 |= rawPacket[7] << 0x10 & 0xff0000;
        data2 |= rawPacket[8] << 0x18 & 0xff000000;

        data3 = rawPacket[9] & 0xff;
    }

    public long getData1() {
        return data1;
    }

    public long getData2() {
        return data2;
    }

    public int getData3() {
        return data3;
    }
}
