package com.jeegnathebug.tttjeegna.business;

/**
 * Created by jeegna on 19/09/16.
 */
public enum GameMode {
    PvP("Player vs. Player", 0),
    PvE("Player vs. Droid", 1);

    private String text;
    private int value;

    private GameMode(String text, int value) {
        this.text = text;
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

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return text;
    }
}
