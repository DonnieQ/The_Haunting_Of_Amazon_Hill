package com.intelligents.client;

import com.intelligents.haunting.Game;
import com.intelligents.haunting.SplashScreen;

import java.io.IOException;

public class Main {
    public static void main(String[] args)  {
        Game g = new Game();
        for (int i = 0; i < 15; i++) {
            System.out.println(g.getRandomRoomFromWorld());
        }
        SplashScreen s = new SplashScreen();
        s.pressAnyKeyToContinue();
        s.splash();
    }
}

