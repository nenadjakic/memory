package com.github.nenadjakic.memory;

import com.github.nenadjakic.memory.model.GameSignal;
import com.github.nenadjakic.memory.model.MenuItem;
import com.github.nenadjakic.memory.component.MenuComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.shell.component.message.ShellMessageBuilder;
import org.springframework.shell.component.view.TerminalUI;
import org.springframework.shell.component.view.TerminalUIBuilder;
import org.springframework.shell.component.view.control.*;
import org.springframework.shell.component.view.event.EventLoop;

import java.util.Arrays;

import static com.github.nenadjakic.memory.util.Constant.TITLE;

public class MemoryGame {
    private static final Logger log = LoggerFactory.getLogger(MemoryGame.class);
    private final TerminalUIBuilder terminalUIBuilder;
    private final GameViewSelector gameViewSelector;

    private final TerminalUI ui;
    private GridView mainView;
    private MenuComponent menuComponent;
    private AppView app;
    private final EventLoop eventLoop;

    public MemoryGame(TerminalUIBuilder terminalUIBuilder) {
        this.terminalUIBuilder = terminalUIBuilder;
        this.ui = this.terminalUIBuilder.build();
        this.eventLoop = ui.getEventLoop();
        this.gameViewSelector = new GameViewSelector(ui).configure(GameSignal.ABOUT.name());

        init();
    }

    private void init() {
        mainView = new GridView();
        mainView.setBorderPadding(2,0,0,0);
        mainView.setColumnSize(0);
        mainView.setRowSize(0);
        ui.configure(mainView);

        menuComponent = (MenuComponent) new MenuComponent(TITLE, Arrays.asList(
                new MenuItem("About", 'a', true, GameSignal.ABOUT),
                new MenuItem("Play", 'p', false, GameSignal.NEW_GAME),
                new MenuItem("Statistics", 's', false, GameSignal.STATISTICS),
                new MenuItem("Quit", 'q', false, GameSignal.QUIT))
        ).configure(ui);
    }

    public void run() {
        app = buildView();
        eventLoop.onDestroy(eventLoop.signalEvents().doOnNext(m -> {
            if (GameSignal.QUIT.name().equals(m))
            {
                requestQuit();
            } else if (Arrays.asList(GameSignal.ABOUT.name(), GameSignal.STATISTICS.name(), GameSignal.NEW_GAME.name()).contains(m)) {
                gameViewSelector.configure(m);
                mainView.clearItems();
                mainView.addItem(gameViewSelector.getView(), 0,0,1,1,0,0);
                ui.redraw();
            }
        }).subscribe());

        ui.setRoot(app, true);
        ui.run();
    }

    public AppView buildView() {
        mainView.addItem(gameViewSelector.getView(), 0,0,1,1,0,0);
        var menuView = menuComponent.build();
        app = new AppView(mainView, menuView, new BoxView());
        ui.configure(app);

        return app;
    }

    private void requestQuit() {
        eventLoop.dispatch(ShellMessageBuilder.ofInterrupt());
    }
}
