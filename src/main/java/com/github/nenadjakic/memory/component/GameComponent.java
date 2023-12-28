package com.github.nenadjakic.memory.component;

import org.springframework.shell.component.view.TerminalUI;
import org.springframework.shell.component.view.control.View;

public interface GameComponent {
    View build();

    GameComponent configure(TerminalUI ui);
}
