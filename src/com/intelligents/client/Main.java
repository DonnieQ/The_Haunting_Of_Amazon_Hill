package com.intelligents.client;

import com.intelligents.haunting.MapMaker;
import com.intelligents.haunting.SplashScreen;

public class Main implements java.io.Serializable {
    public static void main(String[] args) {
        SplashScreen s = new SplashScreen();
        s.pressEnterToContinue();
        s.splash();

        MapMaker m = new MapMaker();
        m.makeTheDiningRoom();



    }
}

