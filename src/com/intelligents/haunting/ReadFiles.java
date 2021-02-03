package com.intelligents.haunting;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class ReadFiles {


    public ReadFiles() {
    }


    public String read(String fileToRead) {
        String results = "";
        if (fileToRead != null) {
            try {
                results = Files.readString(Path.of("The_Haunting_Of_Amazon_Hill/resources", fileToRead));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Sorry file not in Path.");
        }
        return results;
    }
}
