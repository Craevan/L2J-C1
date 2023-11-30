package dev.crevan.l2j.c1.gameserver.model;

import dev.crevan.l2j.c1.gameserver.serverpackets.ServerBasePacket;
import dev.crevan.l2j.c1.gameserver.serverpackets.SocialAction;
import dev.crevan.l2j.c1.gameserver.serverpackets.StatusUpdate;
import dev.crevan.l2j.c1.gameserver.serverpackets.SystemMessage;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

//TODO STUB
@Slf4j
public abstract class L2Character extends L2Object {

    public static final byte STATE_IDLE = 0;
    public static final byte STATE_PICKUP_ITEM = 1;
    public static final byte STATE_CASTING = 2;
    public static final byte STATE_RESTING = 3;
    public static final byte STATE_MOVING = 4;
    public static final byte STATE_ATTACKING = 5;
    public static final byte STATE_RANDOM_WALK = 6;
    public static final byte STATE_INTERACT = 7;
    public static final byte STATE_FOLLOW = 8;

    private static final Timer attackTimer = new Timer(true);
    private static final Timer hitTimer = new Timer(true);
    private static final Timer regenTimer = new Timer(true);
    private static final Timer moveTimer = new Timer(true);
    private static final Random random = new Random();


    private ArrayList<L2Character> statusListners = new ArrayList<>();
    private ArriveTask currentMoveTask;
    private AttackTask currentAttackTask;
    private HitTask currentHitTask;
    private MpRegenTask mpRegenTask;
    private HpRegenTask hpRegenTask = new HpRegenTask(this);
    private Object mpLock = new Object();
    private Object hpLock = new Object();
    private boolean isMpRegenActive;
    private boolean isHpRegenActive;
    private int moveOffset;
    private float effectiveSpeed;
    private long moveStartTime;
    private double xAddition;
    private double yAddition;
    private long timeToTarget;

    private String name;
    private int level;
    private int maxHp;
    private double currentHp;
    private int maxMp;
    private double currentMp;
    private int accuracy;
    private int criticalHit;
    private int evasionRate;
    private int magicalAttack;
    private int magicalDefense;
    private int physicalAttack;
    private int physicalDefense;
    private int physicalSpeed;
    private int runSpeed;
    private int walkSpeed;
    private int flyingRunSpeed;
    private int floatingRunSpeed;
    private int flyingWalkSpeed;
    private int floatingWalkSpeed;

    private int str;
    private int dex;
    private int con;
    private int anInt;
    private int wit;
    private int men;

    private int face;
    private int hairStyle;
    private int hairColor;

    private Gender gender;
    private int heading;

    private int xDestination;
    private int yDestination;
    private int zDestination;

    private double movementMultiplier;
    private double attackSpeedMultiplier;
    private double collisionRadius;
    private double collisionHeght;

    private L2Object target;
    private int activeSoulShotGrade;

    private boolean isInCombat;
    private boolean isMoving;
    private boolean isRunning;
    private boolean isMovingToPawn;

    private int pawnOffset;
    private L2Object pawnTarget;

    private boolean secondHit = false;
    private boolean currentlyAttacking = false;

    private L2Object attackTarget;

    public boolean knowsObject(final L2Object object) {
        return knownObjectList.contains(object);
    }

    public void onDecay() {
        L2World.getInstance().removeVisibleObject(this);
    }

    public void addStatusListener(final L2Character character) {
        statusListners.add(character);
    }

    public void removeStatusListner(final L2Character character) {
        statusListners.remove(character);
    }

    public int getX() {
        if (!isMoving) {
            return super.getX();
        } else {
            long elapsed = System.currentTimeMillis() - moveStartTime;
            int diff = (int) (elapsed * xAddition);
            int remain = Math.abs(xDestination - super.getX()) - Math.abs(diff);
            if (remain > 0) {
                return super.getX() + diff;
            } else {
                return xDestination;
            }
        }
    }

    public int getY() {
        if (!isMoving) {
            return super.getY();
        } else {
            long elapsed = System.currentTimeMillis() - moveStartTime;
            int diff = (int) (elapsed * yAddition);
            int remain = Math.abs(yDestination - super.getY()) - Math.abs(diff);
            if (remain > 0) {
                return super.getY() + diff;
            } else {
                return yDestination;
            }
        }
    }

    public int getZ() {
        return super.getZ();
    }

    public void stopMove() {
        if (currentMoveTask != null) {
            currentMoveTask.cancel();
            currentMoveTask = null;
        }

        setX(getX());
        setY(getY());
        setZ(getZ()); // TODO this the initially requested z coord, it has to be replaced with the real Z
        isMoving = false;
    }

    public void setCurrentHp(final double currentHp) {
        this.currentHp = currentHp;
        if (this.currentHp >= maxHp) {
            stopHpRegeneration();
            this.currentHp = maxHp;
        } else if (!isHpRegenActive && !isDead()) {
            startHpRegeneration();
        }
        broadcastStatusUpdate();
    }

    private void stopHpRegeneration() {
        if (isHpRegenActive) {
            hpRegenTask.cancel();
            hpRegenTask = null;
            isHpRegenActive = false;
            log.info("HP regen stop");
        }
    }

    private void startHpRegeneration() {
        log.info("HP regen started");
        hpRegenTask = new HpRegenTask(this);
        regenTimer.scheduleAtFixedRate(hpRegenTask, 3000, 3000);
        isHpRegenActive = true;
    }

    public boolean isDead() {
        return currentHp <= 0;
    }

    public void setCurrentMp(final double mp) {
        currentMp = mp;
        if (currentMp >= maxMp) {
            stopMpRegeneration();
            currentMp = maxMp;
        } else if (!isMpRegenActive && !isDead()) {
            startMpRegeneration();
        }
        broadcastStatusUpdate();
    }

    public void startMpRegeneration() {
        mpRegenTask = new MpRegenTask(this);
        log.info("MP regen started");
        regenTimer.scheduleAtFixedRate(mpRegenTask, 3000, 3000);
        isMpRegenActive = true;
    }

    public void stopMpRegeneration() {
        if (isMpRegenActive) {
            mpRegenTask.cancel();
            mpRegenTask = null;
            isMpRegenActive = false;
            log.info("MP regen stop");
        }
    }

    public void broadcastStatusUpdate() {
        ArrayList<L2Character> list = statusListners;
        if (list.isEmpty()) {
            return;
        }

        StatusUpdate su = new StatusUpdate(getObjectId());
        su.addAttribute(StatusUpdate.CUR_HP, (int) currentHp);
        su.addAttribute(StatusUpdate.CUR_MP, (int) currentMp);

        for (int i = 0; i < list.size(); i++) {
            L2Character temp = list.get(i);
            if (temp instanceof L2PcInstance player) {
                try {
                    player.sendPacket(su);
                } catch (Exception e) {
                    log.error("Error sending packet = {}, stacktrace = {}", e.getMessage(), e.getStackTrace());
                }
            }
        }
    }

    public void increaseLevel() {
        log.info("Increasing level of {}", name);
        level++;

        StatusUpdate su = new StatusUpdate(getObjectId());
        su.addAttribute(StatusUpdate.LEVEL, level);
        sendPacket(su);
        sendPacket(new SystemMessage(SystemMessage.YOU_INCREASED_YOUR_LEVEL));

        SocialAction sa = new SocialAction(getObjectId(), 15);
        broadcastPacket(sa);
        sendPacket(sa);
    }

    public L2Character[] broadcastPacket(ServerBasePacket mov) {
        Set<L2PcInstance> knownPlayersSet = getKnownPlayers();

        L2Character[] players = knownPlayersSet.toArray(new L2PcInstance[0]);
        log.info("players to notify:" + players.length + " packet:" + mov.getType());

        for (int i = 0; i < players.length; i++) {
            players[i].sendPacket(mov);
        }

        return players;
    }

    public void sendPacket(ServerBasePacket packet) {

    }

    //TODO

    @AllArgsConstructor
    class ArriveTask extends TimerTask {
        L2Character instance;

        @Override
        public void run() {
            // TODO
        }
    }

    class AttackTask extends TimerTask {

        @Override
        public void run() {
            // TODO
        }
    }

    class HitTask extends TimerTask {

        @Override
        public void run() {
            // TODO
        }
    }

    class HpRegenTask extends TimerTask {
        final L2Character instance;

        public HpRegenTask(final L2Character instance) {
            this.instance = instance;
        }

        @Override
        public void run() {
            // TODO
        }
    }

    class MpRegenTask extends TimerTask {

        private final L2Character instance;

        public MpRegenTask(final L2Character instance) {
            this.instance = instance;
        }

        @Override
        public void run() {
            // TODO
        }
    }

    @RequiredArgsConstructor
    class DecayTask extends TimerTask {

        final L2Character instance;

        @Override
        public void run() {
            instance.onDecay();
        }
    }
}
