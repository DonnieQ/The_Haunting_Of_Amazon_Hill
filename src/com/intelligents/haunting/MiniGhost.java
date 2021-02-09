package com.intelligents.haunting;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class MiniGhost extends Ghost implements java.io.Serializable {

    MiniGhost(String name, String type) {
        super(name, type);
    }

    @Override
    public String toString() {
        return getType() + ":\n";
    }
}