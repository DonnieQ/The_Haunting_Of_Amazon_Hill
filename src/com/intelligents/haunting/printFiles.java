package com.intelligents.haunting;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

 class printFiles {



    public printFiles() {

    }


    public void print(String fileToRead) {
        if (fileToRead != null) {
            String results = null;
            try {
                results = Files.readString(Path.of("The_Haunting_Of_Amazon_Hill/resources", fileToRead));
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(results);
        }else{
            System.out.println("Sorry that file is not in the Path.");
        }
    }











}

