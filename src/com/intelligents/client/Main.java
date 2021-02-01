package com.intelligents.client;

import com.intelligents.haunting.Game;
import com.intelligents.haunting.SplashScreen;

public class Main implements java.io.Serializable{
    public static void main(String[] args)  {
        SplashScreen s = new SplashScreen();
        s.pressEnterToContinue();
        s.splash();
    }
}

