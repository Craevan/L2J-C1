package dev.crevan.l2j.c1.loginserver.serverpacket;

public class LoginFail extends ServerBasePacket {

    public static final int REASON_ACCOUNT_BANNED = 0x09;
    public static final int REASON_ACCOUNT_IN_USE = 0x07;
    public static final int REASON_ACCESS_FAILED = 0x04;
    public static final int REASON_USER_OR_PASS_WRONG = 0x03;
    public static final int REASON_PASS_WRONG = 0x02;
    public static final int REASON_SYSTEM_ERROR = 0x01;

    public LoginFail(final int reason) {
        writeC(0x01);
        writeD(reason);
    }

    @Override
    public byte[] getContent() {
        return getBytes();
    }
}
