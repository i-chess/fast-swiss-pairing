package com.ichess.fastswisspairing;

/**
 * Created by Ran on 20/08/2015.
 */
public class Match {
    private int round;
    private Player player1;
    private Player player2;
    private MatchResult result = MatchResult.MATCH_RESULT_NO_RESULT;

    Match(int round, Player player1, Player player2) {
        this.round = round;
        this.player1 = player1;
        this.player2 = player2;
    }

    public boolean hasResult() {
        return result != MatchResult.MATCH_RESULT_NO_RESULT;
    }

    public void setResult(MatchResult result) {
        this.result = result;
    }

    public Player getPlayer1() {
        return player1;
    }

    public boolean hasPlayer(Player player) {
        return (player.equals(player1)) || (player.equals(player2));
    }

    public boolean hasPlayers(Player player1, Player player2) {
        return ((player1.equals(this.player1)) && (player2.equals(this.player2))) ||
                ((player1.equals(this.player2)) && (player2.equals(this.player1)));
    }

    public Player getPlayer2() {
        return player2;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (!(other instanceof Match)) {
            return false;
        }
        Match otherMatch = (Match) other;
        return ((otherMatch.round == round) &&
                (otherMatch.player1.equals(player1)) &&
                (otherMatch.player2.equals(player2)));
    }

    @Override
    public String toString() {
        return "round " + round + " : " + player1 + " - " + player2;
    }

    public enum MatchResult {
        MATCH_RESULT_NO_RESULT,
        MATCH_RESULT_PLAYER_1_WON,
        MATCH_RESULT_PLAYER_2_WON,
        MATCH_RESULT_DRAW,
    }
}
