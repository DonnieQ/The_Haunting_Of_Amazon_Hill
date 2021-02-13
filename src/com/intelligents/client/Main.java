package com.intelligents.client;


import com.intelligents.haunting.SplashScreen;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main implements java.io.Serializable {
    public static void main(String[] args) throws IOException {
        SplashScreen s = new SplashScreen();
        s.pressEnterToContinue();
        s.splash();

    }
}

