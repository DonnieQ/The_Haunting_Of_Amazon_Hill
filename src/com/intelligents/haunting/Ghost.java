package com.intelligents.haunting;

enum Ghost {
    SAMCA("an evil spirit, said to curse children and pregnant women with illness", new String[]{"Sulfuric Scent", "Moldy Cheese"});

    private final String background;
    private final String[] evidence;


     Ghost(String background, String[] evidence) {
        this.background = background;
        this.evidence = evidence;
    }

    public String getBackground() {
        return background;
    }

    public String[] getEvidence() {
        return evidence;
    }
}