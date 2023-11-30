package dev.crevan.l2j.c1.gameserver.serverpackets;

import java.io.IOException;

//todo
public class SocialAction extends ServerBasePacket {

    public SocialAction(final int objectId, final int i) {
    }

    @Override
    public byte[] getContent() throws IOException {
        return new byte[0];
    }

    @Override
    public String getType() {
        return null;
    }
}
