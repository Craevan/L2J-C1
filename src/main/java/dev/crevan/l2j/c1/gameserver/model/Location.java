package dev.crevan.l2j.c1.gameserver.model;

public class Location {

    private final int x;
    private final int y;
    private final int z;

    public Location(final int x, final int y, final int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }
}
