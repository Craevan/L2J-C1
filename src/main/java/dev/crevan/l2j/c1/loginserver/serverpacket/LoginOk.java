package dev.crevan.l2j.c1.loginserver.serverpacket;

public class LoginOk extends ServerBasePacket {

    public LoginOk() {
        writeC(0x03);
        writeD(0x55555555);
        writeD(0x44444444);
        writeD(0x00);
        writeD(0x00);
        writeD(0x000003ea);
        writeD(0x00);
        writeD(0x00);
        writeD(0x02);
    }

    @Override
    public byte[] getContent() {
        return getBytes();
    }
}
