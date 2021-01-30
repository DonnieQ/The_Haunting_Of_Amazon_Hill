package com.intelligents.haunting;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class GameTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void checkForWinner() {

        Player p = new Player("John");
        ArrayList<String> evi = new ArrayList<>();
        Game h = new Game();
        Ghost g = new Ghost("Beck", "Demon", "Does demonic stuff", evi);

        p.setJournal("Claws");
        evi.add("Claws");
        evi.add("teeth");




    }
}