package dev.crevan.l2j.c1.gameserver.model;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

//TODO STUB
@Slf4j
public abstract class L2Character extends L2Object {

    private static final Timer attackTimer = new Timer(true);
    private static final Timer hitTimer = new Timer(true);
    private static final Timer regenTimer = new Timer(true);
    private static final Timer moveTimer = new Timer(true);
    private static final Random random = new Random();


    private ArrayList statusListners = new ArrayList();
    private ArriveTask currentMoveTask;
    private AttackTask currentAttackTask;
    private HitTask currentHitTask;
    private MpRegenTask mpRegenTask;
    private HpRegenTask hpRegenTask;
    private Object mpLock;
    private Object hpLock;
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
    private int currentHp;
    private int maxMp;
    private int currentMp;
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

        @Override
        public void run() {
            // TODO
        }
    }

    class MpRegenTask extends TimerTask {

        @Override
        public void run() {
            // TODO
        }
    }
}
