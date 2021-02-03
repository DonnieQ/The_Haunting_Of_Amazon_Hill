package com.intelligents.haunting;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

public class UserInputTest {
    boolean isGameLoaded = true;


    @Test
    public void testValidInputOfUserName() throws Exception{
       // Game g = new Game();
       // g.start(!isGameLoaded);
        Prompter prompter = new Prompter(new Scanner(new File("The_Haunting_Of_Amazon_Hill/test/testFiles/UserMatt")));
       String name = prompter.prompt("What would you like your name to be?  ");
       // String name = prompter.
        Player p = new Player(name);


        assertEquals("Matt", p.getName());



    }



}
