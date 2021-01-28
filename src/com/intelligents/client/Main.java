package com.intelligents.client;

import com.intelligents.haunting.Game;
import com.intelligents.haunting.SplashScreen;

import java.io.IOException;

public class Main {
    public static void main(String[] args)  {
        Game g = new Game();
        SplashScreen s = new SplashScreen();
        s.pressAnyKeyToContinue();
        s.splash();
    }
}

