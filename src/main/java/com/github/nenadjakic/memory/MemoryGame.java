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
import org.springframework.shell.component.view.event.KeyEvent;
import org.springframework.shell.geom.HorizontalAlign;
import org.springframework.shell.geom.VerticalAlign;

import java.util.Arrays;

public class MemoryGame {
    private static final Logger log = LoggerFactory.getLogger(MemoryGame.class);
    private final TerminalUIBuilder terminalUIBuilder;
    private final GameEngine gameEngine;

    private TerminalUI ui;
    private GridView mainView;
    private AppView app;
    private EventLoop eventLoop;

    public MemoryGame(TerminalUIBuilder terminalUIBuilder) {
        this.terminalUIBuilder = terminalUIBuilder;
        this.ui = this.terminalUIBuilder.build();
        this.gameEngine = new GameEngine(ui).configure(GameSignal.ABOUT.name());
    }

    public void run() {
        ui = terminalUIBuilder.build();
        eventLoop = ui.getEventLoop();
        app = buildView();
        eventLoop.onDestroy(eventLoop.signalEvents().doOnNext(m -> {
            if (GameSignal.QUIT.name().equals(m))
            {
                requestQuit();
            } else if (GameSignal.ABOUT.name().equals(m)) {
                gameEngine.configure(m);
                ui.configure(gameEngine.getView());
                mainView.addItem(gameEngine.getView(), 0,0,1,1,0,0);
                ui.redraw();
            }
        }).subscribe());
        eventLoop.systemEvents().doOnNext(m -> {

        });
        eventLoop.onDestroy(eventLoop.keyEvents()
                .doOnNext(m -> {
                    if (m.getPlainKey() == KeyEvent.Key.Q) {
                        requestQuit();
                    }
                    if (m.getPlainKey() == KeyEvent.Key.q && m.hasCtrl()) {
                        //requestQuit();
                    }


                    if (m.getPlainKey() == KeyEvent.Key.w && m.hasCtrl())
                    {
                        ui.setModal(null);
                    }
                })
                .subscribe());

        ui.setRoot(app, true);
        ui.run();
    }

    public AppView buildView() {
        mainView = new GridView();
        mainView.setBorderPadding(2,0,0,0);
        mainView.setColumnSize(0);
        mainView.setRowSize(0);
        ui.configure(mainView);

        mainView.addItem(gameEngine.getView(), 0,0,1,1,0,0);

        var menuComponent = new MenuComponent(Arrays.asList(
                new MenuItem("About", '1', true, GameSignal.ABOUT),
                new MenuItem("Play", '2', false, GameSignal.NEW_GAME),
                new MenuItem("Statistics", '3', false, GameSignal.STATISTICS),
                new MenuItem("Quit", '4', false, GameSignal.QUIT))
        );
        var menuView = menuComponent.configure(ui).build();

        app = new AppView(mainView, menuView, new BoxView());
        ui.configure(app);

        return app;
    }

    private void requestQuit() {
        eventLoop.dispatch(ShellMessageBuilder.ofInterrupt());
    }
}
