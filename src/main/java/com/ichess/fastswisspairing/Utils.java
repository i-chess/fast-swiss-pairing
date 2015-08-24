package com.ichess.fastswisspairing;

import java.util.List;

/**
 * Created by Ran on 23/08/2015.
 */
public class Utils {
    
    public static boolean listContainsMatchBetweenPlayers(List<Match> matches, Player player1, Player player2) {
        for (Match match : matches) {
            if (match.hasPlayers(player1, player2)) {
                return true;
            }
        }
        return false;
    }
}
