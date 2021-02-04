package com.intelligents.haunting;

import java.util.Scanner;

class Prompter {
    private Scanner scanner;

    Prompter(Scanner var1) {
        this.scanner = var1;
    }

    String prompt(String var1) {
        System.out.println(var1);
        return this.scanner.nextLine();
    }


}
