package dev.crevan.l2j.c1.gameserver.model;

import dev.crevan.l2j.c1.util.CopyOnWriteArrayList;

import java.io.Serializable;
import java.util.List;

//TODO STUB
public class L2Object implements Serializable {

    private int objectId;
    private int x;
    private int y;
    private int z;

    protected List<L2Object> knownObjectList = new CopyOnWriteArrayList<>();

    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(final int objectId) {
        this.objectId = objectId;
    }

    public int getX() {
        return x;
    }

    public void setX(final int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(final int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(final int z) {
        this.z = z;
    }

    public List<L2Object> getKnownObjectList() {
        return knownObjectList;
    }

    public void setKnownObjectList(final List<L2Object> knownObjectList) {
        this.knownObjectList = knownObjectList;
    }
}
