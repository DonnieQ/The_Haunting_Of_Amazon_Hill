package com.intelligents.haunting;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Ghost implements java.io.Serializable {


    private String name;
    private String type;
    private String background;
    private ArrayList<String> evidence;
    private String backstory;

    public String getBackstory() {
        return backstory;
    }

    public void setBackstory(String backstory) {
        this.backstory = backstory;
    }



    // Single constructor


    Ghost(String name, String type) {
        this.name = name;
        this.type = type;
    }

    Ghost(String name, String type, String background, ArrayList<String> evidence, String backstory) {
        this.name = name;
        this.type = type;
        this.background = background;
        this.evidence = evidence;
        this.backstory = backstory;
    }

    // Getters & Setters

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    String getType() {
        return type;
    }

    void setType(String type) {
        this.type = type;
    }

    String getBackground() {
        return background;
    }

    void setBackground(String background) {
        this.background = background;
    }

    ArrayList<String> getEvidence() {
        return evidence;
    }

    void setEvidence(ArrayList<String> evidence) {
        this.evidence = evidence;
    }

    @Override
    public String toString() {
        return getType() + ":\n" +
                "\tBackground: " + getBackground() + "\n" +
                "\tEvidence: " +
                getEvidence()
                        .stream()
                        .map(x -> x.substring(x.lastIndexOf(" ") + 1))
                        .collect(Collectors.toList()) + "\n\n";
    }
}
