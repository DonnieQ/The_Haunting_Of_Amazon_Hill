package com.intelligents.client;

import com.intelligents.haunting.Game;
import com.intelligents.haunting.SplashScreen;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Game g = new Game();
        g.populateGhostList();
        g.print();
////        Map g = new Map();
////        String banner = Files.readString(Path.of("The_Haunting_Of_Amazon_Hill/resources", "introText"));
////        System.out.println(banner);
////        g.start();
//        SplashScreen s = new SplashScreen();
//        s.pressAnyKeyToContinue();
//        s.splash();
    }
}

