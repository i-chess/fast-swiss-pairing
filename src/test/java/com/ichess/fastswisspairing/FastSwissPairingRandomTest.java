package com.ichess.fastswisspairing;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class FastSwissPairingRandomTest extends TestCase {

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
            RoundMatching matching = tournament.nextRound();
            assertEquals(matching.getMatches().size(), players.size() / 2);
            for (Match match : matching.getMatches()) {
                LOGGER.info("paired match " + match);
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

    public void testSimpleTournment_2players_1round() {
        Tournament tournament = new Tournament(1, 2);
        testTournment(tournament);
    }

    public void testSimpleTournment_3players_1round() {
        Tournament tournament = new Tournament(1, 3);
        testTournment(tournament);
    }

    public void testSimpleTournment_4players_2rounds() {
        Tournament tournament = new Tournament(2, 4);
        testTournment(tournament);
    }

    public void testSimpleTournment_5players_2rounds() {
        Tournament tournament = new Tournament(2, 5);
        testTournment(tournament);
    }

    public void testSimpleTournment_6players_3rounds() {
        Tournament tournament = new Tournament(3, 6);
        testTournment(tournament);
    }

    public void testSimpleTournment_7players_3rounds() {
        Tournament tournament = new Tournament(3, 7);
        testTournment(tournament);
    }

    public void testSimpleTournment_8players_4rounds() {
        Tournament tournament = new Tournament(4, 8);
        testTournment(tournament);
    }

    public void testSimpleTournment_9players_4rounds() {
        Tournament tournament = new Tournament(4, 9);
        testTournment(tournament);
    }

    public void testSimpleTournment_10players_5rounds() {
        Tournament tournament = new Tournament(5, 10);
        testTournment(tournament);
    }

    public void testSimpleTournment_100players_50rounds() {
        Tournament tournament = new Tournament(50, 100);
        testTournment(tournament);
    }

    public void test_names() {
        Tournament tournament = new Tournament(1, 2, Arrays.asList("Alice", "Bob"));
        List<Player> players = tournament.getPlayers();
        assertEquals(players.get(0).getName(), "Alice");
        assertEquals(players.get(1).getName(), "Bob");
    }

    public void test_namesWithNullThrows() {

        try {
            new Tournament(1, 3, Arrays.asList("Alice", "Bob", null));
        } catch (IllegalArgumentException ex) {
            return;
        }
        fail();
    }

    public void test_namesWithBadLengthThrows() {
        try {
            new Tournament(1, 3, Arrays.asList("Alice", "Bob"));
        }
        catch (IllegalArgumentException ex) {
            return;
        }
        fail();
    }
}
