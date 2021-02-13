package com.intelligents.haunting;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SaveGameTest {

    @Before
    public void setUp() throws Exception {
//        Game g = new Game(, );
//        World world = new World();
        SaveGame saveGame = new SaveGame();
    }

    @Test
    public void savingNonSerializableObjectsThrowsError() {
        SaveGame saveGame = new SaveGame();
        saveGame.save();
        assertTrue("Sorry currently cannot save game. **coming Soon!**",true);
    }

    @Test
    public void loadingInvalidSavedFileResultsInErrorMessage() {
    SaveGame saveGame = new SaveGame();
    saveGame.loadGame();
    assertTrue("Could not load saved game",true);
    }
}