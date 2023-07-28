package dev.crevan.l2j.c1.loginserver.clientpacket;

public class RequestAuthLogin {
    private final String user;
    private final String password;

    public RequestAuthLogin(final byte[] rawPacket) {
        this.user = new String(rawPacket, 1 , 14).trim().toLowerCase();
        this.password = new String(rawPacket, 15, 14).trim();
    }

    public String getPassword() {
        return this.password;
    }

    public String getUser() {
        return user;
    }
}
