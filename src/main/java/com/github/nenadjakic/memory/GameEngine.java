package com.github.nenadjakic.memory;

import com.github.nenadjakic.memory.component.AboutComponent;
import com.github.nenadjakic.memory.model.GameSignal;
import lombok.Getter;
import lombok.Setter;
import org.springframework.shell.component.view.TerminalUI;
import org.springframework.shell.component.view.control.BoxView;
import org.springframework.shell.component.view.control.View;

@Setter
@Getter
public class GameEngine {

    private Pairs pairs;
    private View view;
    private final TerminalUI terminalUI;

    public GameEngine(final TerminalUI terminalUI) {
        this.terminalUI = terminalUI;
    }

    public GameEngine configure(String gameSignal) {
        if (GameSignal.ABOUT.name().equals(gameSignal)) {
            AboutComponent aboutComponent = new AboutComponent();

            view = aboutComponent.configure(terminalUI).build();
        } else {
            var boxView = new BoxView();
            boxView.setShowBorder(true);
            boxView.setTitleColor(5);

            view = boxView;
        }
        return this;
    }
}
