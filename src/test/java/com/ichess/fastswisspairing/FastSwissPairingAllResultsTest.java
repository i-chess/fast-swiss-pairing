package com.ichess.fastswisspairing;

import junit.framework.TestCase;

import java.util.*;
import java.util.logging.Logger;

public class FastSwissPairingAllResultsTest extends TestCase {

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
            RoundMatching matching = tournament.nextRound();
            assertEquals(matching.getMatches().size(), players.size() / 2);
            for (Match match : matching.getMatches()) {
                LOGGER.info("paired match " + match);
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
        Match.MatchResult[] allResults = new Match.MatchResult[rounds * gamesPerRound];

        for (int round=0;round<rounds;round++)
        {
            for (int game=0;game<gamesPerRound;game++) {
                int index = (round * rounds) + game;
                for (Match.MatchResult result : Match.MatchResult.values()) {
                    if (result == Match.MatchResult.MATCH_RESULT_NO_RESULT) {
                        continue;
                    }
                    allResults[index] = result;
                    Tournament tournament = new Tournament(rounds, players);
                    testTournment(tournament, Arrays.asList(allResults));
                }
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

    public void testAllResults_10players_5rounds() {
        testAllTournmentResults(5, 10);
    }

    public void testAllResults_12players_6rounds() {
        testAllTournmentResults(6, 12);
    }
}
