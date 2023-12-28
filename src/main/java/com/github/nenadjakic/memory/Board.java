package com.github.nenadjakic.memory;

import lombok.Getter;

@Getter
public class Board {

    private final Pairs pairs;
    private final int rows;
    private final int columns;

    public Board(Pairs pairs) {
        this.pairs = pairs;
        this.rows = pairs.getNumberOfPairs() / 4;
        this.columns = pairs.getNumberOfPairs() / 4;
    }

}
