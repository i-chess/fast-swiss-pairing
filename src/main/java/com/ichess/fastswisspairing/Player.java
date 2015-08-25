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

    /**
     * return the player index in the tournmanet.
     * player indexes are in the range [ 0..N-1 ], given that there are N players in the tournament
     * @return the player index in the tournmanet.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Return the round in which the player was not paired ("bye" round).
     * Returns 0 if there is no such round
     * @return the round in which the player was not paired ("bye" round).
     */
    public int getByeRound() {
        return byeRound;
    }

    void setByeRound(int byeRound) {
        this.byeRound = byeRound;
    }

    /**
     * return the player name
     * @return the player name
     */
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
