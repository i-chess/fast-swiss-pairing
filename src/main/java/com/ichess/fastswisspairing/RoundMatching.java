package com.ichess.fastswisspairing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Ran on 20/08/2015.
 */
public class RoundMatching {
    private int round;
    private List<Match> matches = new ArrayList<Match>();

    RoundMatching(int round) {
        this.round = round;
    }

    public List<Match> getMatches() {
        return Collections.unmodifiableList(matches);
    }

    Match addMatch(Player player1, Player player2) {
        for (Match match : matches) {
            if (match.getPlayer1().equals(player1)) {
                throw new IllegalArgumentException("Could not add match " + player1 + " - " + player2 + " : player 1 already matches");
            }
            if (match.getPlayer1().equals(player1)) {
                throw new IllegalArgumentException("Could not add match " + player1 + " - " + player2 + " : player 2 already matches");
            }
        }
        Match match = new Match(round, player1, player2);
        matches.add(match);
        return match;
    }

    boolean hasMatchForPlayer(Player player) {
        for (Match match : matches) {
            if (match.hasPlayer(player)) {
                return true;
            }
        }
        return false;
    }

    boolean removeMatchWithPlayer(Player player) {
        Match foundMatch = null;
        for (Match match : matches) {
            if (match.hasPlayer(player)) {
                foundMatch = match;
                break;
            }
        }
        if (foundMatch != null) {
            return matches.remove(foundMatch);
        }
        return false;
    }

    public Match getMatch(Player player1, Player player2) {
        for (Match match : matches) {
            if ((match.getPlayer1().equals(player1)) && (match.getPlayer2().equals(player2))) {
                return match;
            }
        }
        return null;
    }

    public boolean hasAllResults() {
        for (Match match : matches) {
            if (!match.hasResult()) {
                return false;
            }
        }
        return true;
    }
}
