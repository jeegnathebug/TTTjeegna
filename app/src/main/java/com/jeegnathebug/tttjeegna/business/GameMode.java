package com.jeegnathebug.tttjeegna.business;

/**
 * Created by jeegna on 19/09/16.
 */
public enum GameMode {
    PvP(0), PvE(1);

    private int value;

    GameMode(int value) {
        this.value = value;
    }

    public static GameMode fromInt(int x) {
        switch (x) {
            case 0:
                return PvP;
            case 1:
                return PvE;
        }
        return null;
    }

    public int getValue() {
        return value;
    }
}
