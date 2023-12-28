package com.github.nenadjakic.memory.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuItem {
    private String text;
    private char shortcut;
    private boolean focused;
    private GameSignal gameSignal;

    public MenuItem(String text, char shortcut, boolean focused, GameSignal gameSignal) {
        this.text = text;
        this.shortcut = shortcut;
        this.focused = focused;
        this.gameSignal = gameSignal;
    }
}
