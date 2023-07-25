package dev.crevan.l2j.c1.loginserver.exception;

public class HackingException extends Exception {

    private final String ip;

    public HackingException(final String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }
}
