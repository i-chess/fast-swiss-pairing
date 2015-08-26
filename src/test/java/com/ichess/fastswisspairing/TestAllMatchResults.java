package com.ichess.fastswisspairing;

import junit.framework.TestCase;

import java.util.*;
import java.util.logging.Logger;

public class TestAllMatchResults extends TestCase {

    private final static Logger LOGGER = Logger.getLogger(Tournament.class.getName());
    private Random random = new Random();

    private void testTournment(Tournament tournament, List<Match.MatchResult> results) {
        int rounds = tournament.getRounds();

        Iterator<Match.MatchResult> it = results.iterator();
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
            assertEquals(tournament.getCurrentRound(), round-1);
            RoundMatching matching = tournament.pairNextRound();
            assertEquals(tournament.getCurrentRound(), round);
            assertEquals(matching.getMatches().size(), players.size() / 2);
            for (Match match : matching.getMatches()) {
                LOGGER.fine("paired match " + match);
                assertNotNull(match);
                assertFalse(pairedPlayers.contains(match.getPlayer1()));
                assertFalse(pairedPlayers.contains(match.getPlayer2()));
                pairedPlayers.add(match.getPlayer1());
                pairedPlayers.add(match.getPlayer2());
                match.setResult(it.next());
                assertFalse(Utils.listContainsMatchBetweenPlayers(allMatches, match.getPlayer1(), match.getPlayer2()));
                allMatches.add(match);
            }
        }
    }

    private void testAllTournmentResults(int rounds, int players)
    {
        int gamesPerRound = players / 2;
        int totalGames = rounds * gamesPerRound;
        int totalMatchResultOptions = (0x1 << totalGames);

        Match.MatchResult[] allResults = new Match.MatchResult[totalGames];
        int nextPrintIndex = 0;
        for (int index = 0; index < totalMatchResultOptions; index ++ ) {
            for (int bit = 0; bit < totalGames; bit ++)
            {
                if ((index & (0x1 << bit )) == 0) {
                    allResults[bit] = Match.MatchResult.MATCH_RESULT_PLAYER_1_WON;
                } else {
                    allResults[bit] = Match.MatchResult.MATCH_RESULT_PLAYER_2_WON;
                }
            }
            Tournament tournament = new Tournament(rounds, players);
            testTournment(tournament, Arrays.asList(allResults));
            if (index > nextPrintIndex) {
                LOGGER.info("tested " + index + "/" + totalMatchResultOptions + " match result options so far");
                nextPrintIndex += (totalMatchResultOptions / 10);
            }
        }
    }

    public void testAllResults_4players_2rounds() {
        testAllTournmentResults(2, 4);
    }

    public void testAllResults_6players_3rounds() {
        testAllTournmentResults(3, 6);
    }

    public void testAllResults_8players_4rounds() {
        testAllTournmentResults(4, 8);
    }
}
