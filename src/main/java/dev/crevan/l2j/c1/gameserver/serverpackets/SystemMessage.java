package dev.crevan.l2j.c1.gameserver.serverpackets;

import java.io.IOException;

//todo
public class SystemMessage extends ServerBasePacket {

    public static final int YOU_INCREASED_YOUR_LEVEL = 0x60;

    private static final String S_7_A_SYSTEM_MESSAGE = "[S] 7A SystemMessage";

    private final int messageId;
    public SystemMessage(final int messageId) {
        this.messageId = messageId;
    }

    @Override
    public byte[] getContent() throws IOException {
        return new byte[0];
    }

    @Override
    public String getType() {
        return S_7_A_SYSTEM_MESSAGE;
    }
}
