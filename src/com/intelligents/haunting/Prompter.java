package com.intelligents.haunting;

import java.util.Scanner;

public class Prompter {
    private Scanner scanner;

    public Prompter(Scanner var1) {
        this.scanner = var1;
    }
    public String prompt(String var1) {
        System.out.println(var1);
        return this.scanner.nextLine();
    }


}
