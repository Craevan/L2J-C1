package dev.crevan.l2j.c1.gameserver.model;

public enum Race {

    HUMAN(0), ELF(1), DARK_ELF(2), ORC(3), DWARF(4);

    private final int raceNumber;

    Race(final int raceNumber) {
        this.raceNumber = raceNumber;
    }

    public int getRaceNumber() {
        return raceNumber;
    }
}
