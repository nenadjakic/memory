package com.github.nenadjakic.memory.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.shell.component.view.control.BoxView;
import org.springframework.shell.component.view.control.ButtonView;
import org.springframework.shell.component.view.control.View;

@Getter
@Setter
public class Card {

    private Character symbol;

    private int state;

    private static int counter = 0;

    public View draw() {
        var view = new BoxView();
        return view;
    }
}
