package com.ichess.fastswisspairing;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import static org.junit.Assert.*;

public class TestNames {

    @Test
    public void test_names() {
        Tournament tournament = new Tournament(1, 2, Arrays.asList("Alice", "Bob"));
        List<Player> players = tournament.getPlayers();
        assertEquals(players.get(0).getName(), "Alice");
        assertEquals(players.get(1).getName(), "Bob");
    }

    @Test
    public void test_namesWithNullThrows() {
        try {
            new Tournament(1, 3, Arrays.asList("Alice", "Bob", null));
        } catch (IllegalArgumentException ex) {
            return;
        }
        fail();
    }

    @Test
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
