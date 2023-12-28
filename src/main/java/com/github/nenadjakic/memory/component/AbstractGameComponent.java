package com.github.nenadjakic.memory.component;

import org.springframework.shell.component.view.TerminalUI;
import org.springframework.shell.component.view.control.View;
import org.springframework.shell.component.view.control.ViewService;
import org.springframework.shell.component.view.event.EventLoop;

public abstract class AbstractGameComponent implements GameComponent {

    private TerminalUI ui;
    private ViewService viewService;
    private EventLoop eventloop;

    protected ViewService getViewService() {
        return viewService;
    }

    protected EventLoop getEventloop() {
        return eventloop;
    }

    protected TerminalUI getTerminalUI() {
        return ui;
    }

    public GameComponent configure(TerminalUI ui) {
        this.ui = ui;
        this.eventloop = ui.getEventLoop();
        this.viewService = ui.getViewService();
        return this;
    }

    protected void configure(View view) {
        if (ui != null) {
            ui.configure(view);
        }
    }
}
