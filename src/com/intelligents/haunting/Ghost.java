package com.intelligents.haunting;

import java.util.ArrayList;

public class Ghost {


    private String name;
    private String type;
    private String background;
    private ArrayList<String> evidence;

    // Single constructor

    public Ghost(String name, String type, String background, ArrayList<String> evidence) {
        this.name = name;
        this.type = type;
        this.background = background;
        this.evidence = evidence;
    }

    // Getters & Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public ArrayList<String> getEvidence() {
        return evidence;
    }

    public void setEvidence(ArrayList<String> evidence) {
        this.evidence = evidence;
    }

    @Override
    public String toString() {
        return getType() + ":\n" +
                "\tBackground: " + getBackground() + "\n" +
                "\tEvidence: " +  getEvidence();
    }
}
