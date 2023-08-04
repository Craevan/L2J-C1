package dev.crevan.l2j.c1.gameserver.model;

public enum Gender {

    MALE(0), FEMALE(1);

    private final int gender;

    Gender(final int gender) {
        this.gender = gender;
    }

    public int getGender() {
        return gender;
    }
}
