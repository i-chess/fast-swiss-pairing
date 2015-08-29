package com.ichess.fastswisspairing;

import com.ichess.jvoodoo.*;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import org.junit.*;
import static org.junit.Assert.*;

public class TestFirstRoundPairing {

    private final static Logger LOGGER = Logger.getLogger(Tournament.class.getName());

    @Before
    public void setUp() throws java.lang.Exception {
        Voodoo.castVoodooOn("com.ichess.fastswisspairing.RandomWrapper");
    }

    @Test
    public void testInitialFirstRoundOrdered() {
        Scenario scenario = new Scenario();
        scenario.add(new Construction("com.ichess.fastswisspairing.RandomWrapper", "fake random"));
        Tournament tournament = new Tournament(5, 10);
        scenario.assertFinished();

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

    @Test
    public void testInitialFirstRoundRandom() {
        Scenario scenario = new Scenario();
        scenario.add(new Construction("com.ichess.fastswisspairing.RandomWrapper", "fake random"));
        Tournament tournament = new Tournament(5, 10);
        scenario.assertFinished();

        tournament.setFirstRoundMatchingRule(Tournament.FirstRoundMatchingRule.FIRST_ROUND_MATCH_RANDOM);
        Random random = new java.util.Random();
        List<Integer> playerIndexes = new ArrayList<Integer>();
        List<Integer> expectedIndexes = new ArrayList<Integer>();
        for (int i=0;i<10;i++) {
            playerIndexes.add(i);
        }
        for (int i=0;i<10;i++) {

            int randomResult = random.nextInt(10 - i);
            scenario.add(new Invocation("fake random", "nextInt", randomResult, new ParameterEquals(10 - i)));
            expectedIndexes.add(playerIndexes.remove(randomResult));
        }
        RoundMatching matching = tournament.pairNextRound();
        int index = 0;
        for (Match match : matching.getMatches())
        {
            assertEquals(match.getPlayer1().getIndex(), expectedIndexes.get(index).intValue());
            index ++;
            assertEquals(match.getPlayer2().getIndex(), expectedIndexes.get(index).intValue());
            index ++;
        }
        scenario.assertFinished();
    }



}
