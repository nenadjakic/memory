package com.github.nenadjakic.memory.component;

import com.github.nenadjakic.memory.model.MenuItem;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.shell.component.message.ShellMessageBuilder;
import org.springframework.shell.component.view.control.*;
import org.springframework.shell.component.view.screen.Color;
import org.springframework.shell.component.view.screen.Screen;
import org.springframework.shell.geom.HorizontalAlign;
import org.springframework.shell.geom.Rectangle;
import org.springframework.shell.geom.VerticalAlign;

import java.util.List;
import java.util.stream.Collectors;

public class MenuComponent extends AbstractGameComponent {

    private static final Logger log = LoggerFactory.getLogger(MenuComponent.class);

    private final String title;
    private final List<MenuItem> menuItemList;

    @Getter
    @Setter
    private boolean focused = false;

    public MenuComponent(String title, List<MenuItem> menuItemList) {
        this.title = title;
        this.menuItemList = menuItemList;
    }

    @Override
    public View build() {
        log.trace("build() {}", this);

        BoxView view = new BoxView();

        view.setDrawFunction((screen, rect) -> {
            if (rect.width() > 0 && rect.height() > 0) {
                var x = getMenuStartColumn(rect.width());

                Rectangle r = new Rectangle(0, rect.y(), title.length(), 1);
                writeTitle(screen, r);

                int chars;
                for (int i = 0; i < menuItemList.size(); i++) {
                    var item = menuItemList.get(i);

                    var width = item.getText().length();
                    chars = width + 4; // shortcut length
                    r = new Rectangle(x, rect.y(), chars, 1);

                    x += chars;
                    String text = "[" + String.valueOf(item.getShortcut()).toUpperCase() + "] " + item.getText().toUpperCase();

                    screen.writerBuilder()
                            .style(item.isFocused() ? 1 : -1)
                            .color(item.isFocused() ? Color.GOLD : Color.ANTIQUEWHITE)
                            .build()
                            .text(text, r, HorizontalAlign.RIGHT, VerticalAlign.TOP);

                    if (i != menuItemList.size() - 1) {
                        r = new Rectangle(x, rect.y(), 3, 1);
                        x += 3;

                        screen.writerBuilder()
                                .color(Color.ANTIQUEWHITE)
                                .build()
                                .text(" | ", r, HorizontalAlign.RIGHT, VerticalAlign.TOP);
                    }
                }
            }
            return rect;
        });

        getEventloop().onDestroy(getEventloop().keyEvents().subscribe(event -> {
            log.debug("MENU: " + event.getPlainKey());
            log.debug("MENU CTRL: " + event.hasCtrl());
            log.debug("s CTRL: " + (int)menuItemList.get(0).getShortcut());
            if (event.hasCtrl()) {
                var optMenuItem = menuItemList.stream().filter(x -> x.getShortcut() == event.getPlainKey()).findAny();

                if (optMenuItem.isPresent()) {
                    menuItemList.forEach(x -> x.setFocused(false));
                    var menuItem = optMenuItem.get();
                    menuItem.setFocused(true);

                    getEventloop().dispatch(ShellMessageBuilder.ofSignal(menuItem.getGameSignal().name()));
                }
            }
        }));

        if (!menuItemList.isEmpty()) {
             var focusedMenuItem = menuItemList.stream().filter(MenuItem::isFocused).findAny().orElseGet(null);
             if (focusedMenuItem == null) {
                 focusedMenuItem = menuItemList.stream().findFirst().get();
                 focusedMenuItem.setFocused(true);
             }
        }

        return view;
    }

    private void writeTitle(Screen screen, Rectangle rectangle) {
        screen.writerBuilder()
                .style(1)
                .color(Color.AQUAMARINE)
                .build()
                .text(title, rectangle, HorizontalAlign.LEFT, VerticalAlign.TOP);
    }

    private int getMenuStartColumn(int width) {
        var size = menuItemList.size();
        return width
                - menuItemList.stream().map(MenuItem::getText).collect(Collectors.joining("")).length()
                - size * 4  // shortcut "[1] "
                - (size - 1) * 3; // delimeter " | "
    }
}
