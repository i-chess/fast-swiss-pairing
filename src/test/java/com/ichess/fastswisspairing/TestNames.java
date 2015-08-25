package com.ichess.fastswisspairing;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class TestNames extends TestCase {

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
