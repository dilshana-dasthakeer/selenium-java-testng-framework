package com.dilshana.qa.config;

import java.util.Arrays;

public enum Browser {
    CHROME, FIREFOX, EDGE;

    public static Browser from(String name) {
        return Arrays.stream(values())
                .filter(b -> b.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unsupported browser '" + name + "'. Use chrome, firefox or edge."));
    }
}
