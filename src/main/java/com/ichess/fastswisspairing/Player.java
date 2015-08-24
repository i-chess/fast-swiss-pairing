package com.ichess.fastswisspairing;

/**
 * Created by Ran on 20/08/2015.
 */
public class Player implements Comparable<Player> {
    private int index;
    private String name;
    private float score = 0;
    private int byeRound = 0;
    Player(int index, String name) {
        if (name == null) {
            throw new IllegalArgumentException("Illegal player name (null)");
        }
        this.index = index;

        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public int getByeRound() {
        return byeRound;
    }

    public void setByeRound(int byeRound) {
        this.byeRound = byeRound;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (!(other instanceof Player)) {
            return false;
        }
        Player otherPlayer = (Player) other;
        return otherPlayer.name.equals(name);
    }

    @Override
    public String toString() {
        return name;
    }

    public int compareTo(Player other) {
        if (score > other.score) {
            return 1;
        }
        if (score < other.score) {
            return -1;
        }
        return 0;
    }

}
