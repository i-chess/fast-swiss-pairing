package com.ichess.fastswisspairing;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class TestFirstRoundPairing extends TestCase {

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

    public void testInitialFirstRoundRandom() {
        Tournament tournament = new Tournament(5, 10);
        tournament.setFirstRoundMatchingRule(Tournament.FirstRoundMatchingRule.FIRST_ROUND_MATCH_RANDOM);
        tournament.pairNextRound();
        // TODO how do I check that its random
    }

    public void testInitialFirstRoundOrdered() {
        Tournament tournament = new Tournament(5, 10);
        tournament.setFirstRoundMatchingRule(Tournament.FirstRoundMatchingRule.FIRST_ROUND_MATCH_ORDERED);
        RoundMatching matching = tournament.pairNextRound();
        int index = 1;
        for (Match match : matching.getMatches())
        {
            assertEquals(match.getPlayer1().getName(), "player-" + index);
            index ++;
            assertEquals(match.getPlayer2().getName(), "player-" + index );
            index ++;
        }
    }

}
