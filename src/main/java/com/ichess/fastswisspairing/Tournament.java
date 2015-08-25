package com.ichess.fastswisspairing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Created by Ran on 20/08/2015.
 */
public class Tournament {

    private final static Logger LOGGER = Logger.getLogger(Tournament.class.getName());
    private List<Player> players = new ArrayList<Player>();
    private int rounds = 0;
    private int numberOfPlayers;
    private int currentRound = 0;
    private FirstRoundMatchingRule firstRoundMatchingRule;
    private List<Match> allMatches = new ArrayList<Match>();
    private List<RoundMatching> allMatchings = new ArrayList<RoundMatching>();
    private Random random = new Random();

    /**
     * create a new tournmant
     * @param rounds number of rounds in the tournment. must be >= 1
     * @param numberOfPlayers number of players in the tournment. must be >= twice the number of rounds
     * @param names list of player names. if null, players will be named "player-1".."player-N"
     */
    public Tournament(int rounds, int numberOfPlayers, List<String> names) {
        if (rounds <= 0) {
            throw new IllegalArgumentException("Illegal number of rounds " + rounds);
        }
        if (names == null) {
            names = new ArrayList<String>();
            for (int i = 0; i < numberOfPlayers; i++) {
                names.add("player-" + (i + 1));
            }
        }
        if (names.size() != numberOfPlayers) {
            throw new IllegalArgumentException("Expected " + numberOfPlayers + " number of names, received " + names.size());
        }
        for (int i = 0; i < numberOfPlayers; i++) {
            Player player = new Player(i, names.get(i));
            this.players.add(player);
        }
        this.rounds = rounds;
        this.numberOfPlayers = numberOfPlayers;
    }

    public Tournament(int rounds, int numberOfPlayers) {
        this(rounds, numberOfPlayers, null);
    }

    public int getRounds() {
        return rounds;
    }

    public void setFirstRoundMatchingRule(FirstRoundMatchingRule firstRoundMatchingRule) {
        if (currentRound != 0) {
            throw new IllegalArgumentException("First round was already paired");
        }
        this.firstRoundMatchingRule = firstRoundMatchingRule;
    }

    public Player getPlayer(int index) {
        return players.get(index);
    }

    public List<Player> getPlayers() {
        return new ArrayList<Player>(players);
    }

    private RoundMatching firstRoundOrderedMatching()
    {
        RoundMatching matching = new RoundMatching(currentRound);
        int index = 0;
        while (index < numberOfPlayers - 1) {
            Match match = new Match(currentRound, getPlayer(index), getPlayer(index + 1));
            allMatches.add(match);
            matching.addMatch(match.getPlayer1(), match.getPlayer2());
            index += 2;
        }
        allMatchings.add(matching);
        return matching;
    }

    private RoundMatching firstRoundRandomdMatching()
    {
        RoundMatching matching = new RoundMatching(currentRound);
        List<Player> notPairedYet = new ArrayList<Player>(players);
        while (notPairedYet.size() > 1) {
            int player1Index = random.nextInt(notPairedYet.size());
            Player player1 = notPairedYet.get(player1Index);
            notPairedYet.remove(player1Index);
            int player2Index = random.nextInt(notPairedYet.size());
            Player player2 = notPairedYet.get(player2Index);
            notPairedYet.remove(player2Index);
            Match match = new Match(currentRound, player1, player2);
            allMatches.add(match);
            matching.addMatch(match.getPlayer1(), match.getPlayer2());
        }
        allMatchings.add(matching);
        return matching;
    }

    /**
     * moves to next round, and return the next round matches
     * @return next round matches
     */
    public synchronized RoundMatching pairNextRound() {
        currentRound++;
        if (currentRound > rounds) {
            throw new IllegalArgumentException("Tournament ended after " + rounds + " rounds");
        }
        if (currentRound == 1) {
            // first round matching
            if (firstRoundMatchingRule == FirstRoundMatchingRule.FIRST_ROUND_MATCH_ORDERED) {
                return firstRoundOrderedMatching();

            }
            if (firstRoundMatchingRule == FirstRoundMatchingRule.FIRST_ROUND_MATCH_RANDOM) {
                return firstRoundRandomdMatching();

            }
            throw new IllegalArgumentException("not supported yet");
        }
        // not first round. check that previous round has all results
        RoundMatching lastRoundMatching = allMatchings.get(currentRound - 2);
        if (!lastRoundMatching.hasAllResults()) {
            throw new IllegalStateException("Round " + (currentRound - 1) + " results were not set yet");
        }

        // need to pair new round
        RoundMatching matching = getNextRoundMatching();
        allMatchings.add(matching);
        return matching;
    }

    private RoundMatching getNextRoundMatching() {

        // sort the players based on their score
        List<Player> sortedPlayers = new ArrayList<Player>(players);
        Collections.sort(sortedPlayers);
        int byePlayerIndex = -1;
        //List<Match> newMatches = new ArrayList<Match>();
        RoundMatching newMatching = new RoundMatching(currentRound);

        if ((numberOfPlayers % 2) == 1) {
            // choose a bye player
            int index = numberOfPlayers - 1;
            while (index >= 0) {
                Player player = sortedPlayers.get(index);
                if (player.getByeRound() == 0) {
                    player.setByeRound(currentRound);
                    byePlayerIndex = index;
                    LOGGER.fine("player " + player + " bye round " + currentRound);
                    break;
                }
                index--;
            }
        }

        // iterate over the players, find a matching for the top player
        for (int i = 0; i < numberOfPlayers; i++) {

            int bestPlayerId;
            Player bestScorePlayer;

            bestScorePlayer = sortedPlayers.get(i);
            bestPlayerId = bestScorePlayer.getIndex();

            if (bestPlayerId == byePlayerIndex) {
                LOGGER.fine("player " + bestScorePlayer + " bye this round");
                continue;
            }

            // check if this player is already scheduled this round
            if (newMatching.hasMatchForPlayer(bestScorePlayer)) {
                LOGGER.fine("round " + currentRound + " player " + bestScorePlayer + " already scheduled");
                continue;
            }

            boolean matchForBestPlayerFound = false;
            for (int j = i + 1; j < numberOfPlayers; j++) {

                int nextPlayerIndex;
                Player nextScorePlayer;
                nextScorePlayer = players.get(j);
                nextPlayerIndex = nextScorePlayer.getIndex();

                if (nextPlayerIndex == byePlayerIndex) {
                    LOGGER.fine(nextScorePlayer + " bye this round");
                    continue;
                }

                // check if this player is already scheduled this round
                if (newMatching.hasMatchForPlayer(nextScorePlayer)) {
                    LOGGER.fine("round " + currentRound + " player " + nextScorePlayer + " already scheduled");
                    continue;
                }

                // check if such game already happened
                if (Utils.listContainsMatchBetweenPlayers(allMatches, bestScorePlayer, nextScorePlayer)) {
                    // already played. find next opp
                    LOGGER.fine("round " + currentRound + " game " + bestScorePlayer + " - " + nextScorePlayer + " exists");
                    continue;
                }

                Match match = newMatching.addMatch(bestScorePlayer, nextScorePlayer);
                allMatches.add(match);
                matchForBestPlayerFound = true;
                break;
            }

            if (matchForBestPlayerFound) {
                // ok ! continue on to next player
                continue;
            }

            if (newMatching.getMatches().size() == (numberOfPlayers / 2)) {
                // we have all games that we need
                continue;
            }

            // no match for the best player found. we now have to find a couple to break,
            // and opp for this player that will satisfy all conditions
            // so iterate on the pairing so far in reverse order
            LOGGER.fine("round " + currentRound + " need to switch pairs for " + bestScorePlayer + " we have " + newMatching.getMatches().size() + " games");

            for (int g = newMatching.getMatches().size() - 1; g >= 0; g--) {
                Match pairedGame = newMatching.getMatches().get(g);
                // see if the best player can be matched vs any of this couple
                Player player1 = pairedGame.getPlayer1();
                Player player2 = pairedGame.getPlayer2();

                if ((Utils.listContainsMatchBetweenPlayers(allMatches, bestScorePlayer, player1)) &&
                        (Utils.listContainsMatchBetweenPlayers(allMatches, bestScorePlayer, player2))) {
                    // we can't use this pair because the best score user already played vs both of them
                    continue;
                }

                // ok have a candidate pairing. lets iterate over the players again from the bottom to find someone
                // to switch pairs with

                Player switchPlayer;

                for (int p = numberOfPlayers - 1; p >= 0; p--) {

                    switchPlayer = players.get(p);

                    // check that the switch player is not scheduled, and that it is not the bye user, or the best
                    // score user, or the chosen pairs wid,bid
                    if (newMatching.hasMatchForPlayer(switchPlayer)) {
                        LOGGER.fine("round " + currentRound + " switch user " + switchPlayer + " already scheduled");
                        continue;
                    }

                    if ((switchPlayer.equals(bestScorePlayer)) || (switchPlayer.getIndex() == byePlayerIndex) ||
                            (switchPlayer.equals(player1)) || (switchPlayer.equals(player2))) {
                        LOGGER.fine("round " + currentRound + " switch user " + switchPlayer + " is either the best score, bye, wid or bid");
                        continue;
                    }

                    LOGGER.fine("round " + currentRound + " candidate switch player " + switchPlayer);

                    // ok ! the last thing to check it that it is possible to make some pairing switch

                    if (!((Utils.listContainsMatchBetweenPlayers(allMatches, player1, switchPlayer)) ||
                            (Utils.listContainsMatchBetweenPlayers(allMatches, player2, bestScorePlayer)))) {
                        // we can switch. wid vs the switch user, best player vs bid
                        LOGGER.fine("pairing remove game " + pairedGame);

                        if (!newMatching.removeMatchWithPlayer(player1)) {
                            LOGGER.warning("could not remove game with " + player1);
                            return null;
                        }

                        Match newMatch = newMatching.addMatch(player1, switchPlayer);
                        allMatches.add(newMatch);
                        newMatch = newMatching.addMatch(player2, bestScorePlayer);
                        allMatches.add(newMatch);

                        matchForBestPlayerFound = true;
                        break;
                    }

                    if (!((Utils.listContainsMatchBetweenPlayers(allMatches, player2, switchPlayer)) ||
                            (Utils.listContainsMatchBetweenPlayers(allMatches, player1, bestScorePlayer)))) {
                        // we can switch. wid vs the switch user, best player vs bid
                        LOGGER.fine("pairing remove game " + pairedGame);

                        if (!newMatching.removeMatchWithPlayer(player1)) {
                            LOGGER.warning("could not remove match with " + player1);
                            return null;
                        }

                        Match newMatch = newMatching.addMatch(player2, switchPlayer);
                        allMatches.add(newMatch);
                        newMatch = newMatching.addMatch(player1, bestScorePlayer);
                        allMatches.add(newMatch);

                        matchForBestPlayerFound = true;
                        break;
                    }
                }

                if (matchForBestPlayerFound) {
                    break;
                }
            }

            if (!matchForBestPlayerFound) {
                // nothing to do... probably not enough players or some crazy pairing
                LOGGER.warning("could not match all players. not enough players ?");
                break;
            }
        }

        return newMatching;
    }

    public enum FirstRoundMatchingRule {
        /**
         * match the first round player1-player2, player3-player4, etc
         * this is the default
         */
        FIRST_ROUND_MATCH_ORDERED,
        /**
         * match the first round randomly.
         */
        FIRST_ROUND_MATCH_RANDOM,
    }

}
