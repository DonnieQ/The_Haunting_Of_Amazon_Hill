package com.intelligents.haunting;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameTest {
    Game g;
    Player p;

    @Before
    public void setUp() throws Exception {
        g = new Game();
        p = new Player("John");
    }

    @Test
    public void checkForWinnerForTwoOfTwoEvidenceItemsPositive() {
        p.setJournal("Slime");
        p.setJournal("Cheese");
        g.setPlayer(p);
        g.setCurrentGhost(g.getGhosts().get(0));

        assertTrue(g.checkForWinner());

    }

    @Test
    public void checkForWinnerForOneOfTwoEvidenceItemsPositive() {
        p.setJournal("Slime");
        g.setPlayer(p);
        g.setCurrentGhost(g.getGhosts().get(0));

        assertFalse(g.checkForWinner());
    }

    @Test
    public void checkForWinnerWithEvidenceInSentencePositive() {
        p.setJournal("Hello there is Slime in here");
        p.setJournal("There is Cheese in this one!!");
        g.setPlayer(p);
        g.setCurrentGhost(g.getGhosts().get(0));

        assertTrue(g.checkForWinner());
    }
}