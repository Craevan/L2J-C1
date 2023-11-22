package dev.crevan.l2j.c1.gameserver.model;

import dev.crevan.l2j.c1.util.CopyOnWriteArrayList;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Getter
@Setter
public class L2Object implements Serializable {

    private int objectId;
    private int x;
    private int y;
    private int z;
    private Set<L2PcInstance> knownPlayer = new HashSet<>();

    protected List<L2Object> knownObjectList = new CopyOnWriteArrayList<>();

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
