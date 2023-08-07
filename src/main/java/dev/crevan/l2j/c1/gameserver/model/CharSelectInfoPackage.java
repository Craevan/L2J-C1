package dev.crevan.l2j.c1.gameserver.model;

public class CharSelectInfoPackage {

    private String name;
    private int charId = 0x00030b7a;
    private int exp = 0;
    private int sp = 0;
    private int clanId = 0;
    private Race race = Race.HUMAN;
    private int classId = 0;
    private int deleteTimer = 0;
    private int face = 0;
    private int hairStyle = 0;
    private int hairColor = 0;
    private Gender gender = Gender.MALE;
    private int level = 1;
    private int maxHp = 0;
    private double currentHp = 0;
    private int maxMp = 0;
    private double currentMp = 0;
    private Inventory inventory = new Inventory();

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getCharId() {
        return charId;
    }

    public void setCharId(final int charId) {
        this.charId = charId;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(final int exp) {
        this.exp = exp;
    }

    public int getSp() {
        return sp;
    }

    public void setSp(final int sp) {
        this.sp = sp;
    }

    public int getClanId() {
        return clanId;
    }

    public void setClanId(final int clanId) {
        this.clanId = clanId;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(final Race race) {
        this.race = race;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(final int classId) {
        this.classId = classId;
    }

    public int getDeleteTimer() {
        return deleteTimer;
    }

    public void setDeleteTimer(final int deleteTimer) {
        this.deleteTimer = deleteTimer;
    }

    public int getFace() {
        return face;
    }

    public void setFace(final int face) {
        this.face = face;
    }

    public int getHairStyle() {
        return hairStyle;
    }

    public void setHairStyle(final int hairStyle) {
        this.hairStyle = hairStyle;
    }

    public int getHairColor() {
        return hairColor;
    }

    public void setHairColor(final int hairColor) {
        this.hairColor = hairColor;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(final Gender gender) {
        this.gender = gender;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(final int level) {
        this.level = level;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(final int maxHp) {
        this.maxHp = maxHp;
    }

    public double getCurrentHp() {
        return currentHp;
    }

    public void setCurrentHp(final double currentHp) {
        this.currentHp = currentHp;
    }

    public int getMaxMp() {
        return maxMp;
    }

    public void setMaxMp(final int maxMp) {
        this.maxMp = maxMp;
    }

    public double getCurrentMp() {
        return currentMp;
    }

    public void setCurrentMp(final double currentMp) {
        this.currentMp = currentMp;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(final Inventory inventory) {
        this.inventory = inventory;
    }
}
