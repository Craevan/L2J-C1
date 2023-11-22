package dev.crevan.l2j.c1.gameserver.model;

import dev.crevan.l2j.c1.util.CopyOnWriteArrayList;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class L2Object implements Serializable {

    public static final Logger log = Logger.getLogger(L2Object.class.getName());

    private int objectId;
    private int x;
    private int y;
    private int z;
    private Set<L2PcInstance> knownPlayer = new HashSet<>();

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

    public void addKnownObject(final L2Object l2Object) {
        knownObjectList.add(l2Object);

        if (l2Object instanceof L2PcInstance) {
            knownPlayer.add((L2PcInstance) l2Object);
        }
    }

    public void removeKnownObject(final L2Object l2Object) {
        knownObjectList.remove(l2Object);

        if (l2Object instanceof L2PcInstance) {
            knownPlayer.remove((L2PcInstance) l2Object);
        }
    }

    public void removeAllKnownObjects() {
        knownObjectList.forEach(l2Object -> l2Object.removeKnownObject(l2Object));

        if (knownObjectList.size() != 0) {
            log.info("KnownObjectList is not clear");
            knownObjectList.clear();
        }

    }

    public Set<L2PcInstance> getKnownPlayer() {
        return knownPlayer;
    }
}
