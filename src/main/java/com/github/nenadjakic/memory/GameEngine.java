package com.github.nenadjakic.memory;

import com.github.nenadjakic.memory.component.AboutComponent;
import com.github.nenadjakic.memory.component.BoardComponent;
import com.github.nenadjakic.memory.component.StatisticsComponent;
import com.github.nenadjakic.memory.model.GameSignal;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.shell.component.view.TerminalUI;
import org.springframework.shell.component.view.control.View;

@Setter
@Getter
public class GameEngine {
    private static final Logger log = LoggerFactory.getLogger(GameEngine.class);

    private Pairs pairs;
    private View view;
    private final TerminalUI terminalUI;
    private final AboutComponent aboutComponent;
    private final StatisticsComponent statisticsComponent;
    private final BoardComponent boardComponent;

    public GameEngine(final TerminalUI terminalUI) {
        this.terminalUI = terminalUI;

        aboutComponent = new AboutComponent();
        statisticsComponent = new StatisticsComponent();
        boardComponent = new BoardComponent(Pairs.FOUR);
    }

    public GameEngine configure(String gameSignal) {
        view = switch (GameSignal.valueOf(gameSignal)) {
            case ABOUT -> aboutComponent.configure(terminalUI).build();
            case STATISTICS -> statisticsComponent.configure(terminalUI).build();
            case NEW_GAME -> boardComponent.configure(terminalUI).build();
            default -> throw new IllegalStateException("Unexpected value: " + gameSignal);
        };

        return this;
    }
}
