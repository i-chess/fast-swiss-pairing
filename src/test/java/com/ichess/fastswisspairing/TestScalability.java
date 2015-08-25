package com.ichess.fastswisspairing;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class TestScalability extends TestCase {

    private final static Logger LOGGER = Logger.getLogger(Tournament.class.getName());
    private Random random = new Random();

    private void testTournment(Tournament tournament) {
        int rounds = tournament.getRounds();

        List<Player> players = tournament.getPlayers();
        for (int index = 0; index < players.size(); index++) {
            Player player = players.get(index);
            assertEquals(player.getName(), "player-" + (index + 1));
        }

        tournament.setFirstRoundMatchingRule(Tournament.FirstRoundMatchingRule.values()[random.nextInt(2)]);
        List<Player> pairedPlayers;
        List<Match> allMatches = new ArrayList<Match>();
        for (int round = 1; round <= rounds; round++) {
            pairedPlayers = new ArrayList<Player>();
            RoundMatching matching = tournament.pairNextRound();
            assertEquals(matching.getMatches().size(), players.size() / 2);
            for (Match match : matching.getMatches()) {
                LOGGER.fine("paired match " + match);
                assertNotNull(match);
                assertFalse(pairedPlayers.contains(match.getPlayer1()));
                assertFalse(pairedPlayers.contains(match.getPlayer2()));
                pairedPlayers.add(match.getPlayer1());
                pairedPlayers.add(match.getPlayer2());
                match.setResult(Match.MatchResult.values()[(random.nextInt(3)) + 1]);
                assertFalse(Utils.listContainsMatchBetweenPlayers(allMatches, match.getPlayer1(), match.getPlayer2()));
                allMatches.add(match);
            }
        }
    }

    public void testSimpleTournment_100players_50rounds() {
        Tournament tournament = new Tournament(20, 40);
        testTournment(tournament);
    }

    public void testSimpleTournment_1000players_50rounds() {
        Tournament tournament = new Tournament(20, 1000);
        testTournment(tournament);
    }

}
