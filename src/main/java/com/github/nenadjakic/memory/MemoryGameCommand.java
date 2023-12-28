package com.github.nenadjakic.memory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.component.view.TerminalUIBuilder;
import org.springframework.shell.standard.AbstractShellComponent;

@Command
public class MemoryGameCommand extends AbstractShellComponent {
    private final TerminalUIBuilder terminalUIBuilder;

    @Autowired
    public MemoryGameCommand(final TerminalUIBuilder terminalUIBuilder) {
        this.terminalUIBuilder = terminalUIBuilder;
    }

    @Command(command = "memory-game")
    public void memoryGame() {
        MemoryGame memoryGame = new MemoryGame(terminalUIBuilder);
        memoryGame.run();
    }
}