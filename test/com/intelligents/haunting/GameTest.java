package com.intelligents.haunting;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class GameTest {
    Game g;
    Player p;

    @Before
    public void setUp() throws Exception {
        g = new Game();
        p = Player.getInstance();
    }

    @Test
    public void checkForWinnerForTwoOfTwoEvidenceItemsPositive() {
        p.setJournal("Slime");
        p.setJournal("Cheese");
        g.setPlayer(p);
        g.setCurrentGhost(g.getGhosts().get(0));

        assertTrue(g.checkWinnerTest());

    }

    @Test
    public void checkForWinnerForOneOfTwoEvidenceItemsPositive() {
        p.setJournal("Slime");
        g.setPlayer(p);
        g.setCurrentGhost(g.getGhosts().get(0));

        assertFalse(g.checkWinnerTest());
    }

    @Test
    public void checkForWinnerWithEvidenceInSentencePositive() {
        p.setJournal("Hello there is Slime in here");
        p.setJournal("There is Cheese in this one!!!");
        g.setPlayer(p);
        g.setCurrentGhost(g.getGhosts().get(0));

        assertTrue(g.checkWinnerTest());
    }

    @Test
    public void checkForRandomGhostSpawned() {
        g.populateGhostList();
        g.setCurrentGhost(g.getRandomGhost());
        Ghost gg = g.getCurrentGhost();


        System.out.println(gg);


    }

}