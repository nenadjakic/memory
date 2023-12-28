package com.github.nenadjakic.memory;

import lombok.Getter;

@Getter
public enum Pairs {
    FOUR(4),
    EIGHT(8),
    TWELVE(12),
    SIXTEEN(16);

    private final int numberOfPairs;

    Pairs(int numberOfPairs) {
        this.numberOfPairs = numberOfPairs;
    }
}
