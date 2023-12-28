package com.github.nenadjakic.memory.component;

import com.github.nenadjakic.memory.util.Constant;
import org.springframework.shell.component.view.control.BoxView;
import org.springframework.shell.component.view.control.View;
import org.springframework.shell.component.view.screen.Color;
import org.springframework.shell.geom.HorizontalAlign;
import org.springframework.shell.geom.Rectangle;
import org.springframework.shell.geom.VerticalAlign;

public class AboutComponent extends AbstractGameComponent {
    @Override
    public View build() {
        var view = new BoxView();
        view.setShowBorder(true);
        view.setTitleColor(Color.GOLD);


        view.setDrawFunction((screen, rect) -> {
            int startY = 10;
            Rectangle r = new Rectangle(10, startY, rect.width() - 10, 1);
            screen.writerBuilder()
                    .color(Constant.FOCUSED_FONT_COLOR)
                    .style(1)
                    .build()
                    .text("Memory match game", r, HorizontalAlign.CENTER, VerticalAlign.CENTER);

            startY += 2;
            r = new Rectangle(10, startY, rect.width() - 10, 1);
            screen.writerBuilder()
                    .color(Constant.FONT_COLOR)
                    .build()
                    .text("Author: Nenad Jakic", r, HorizontalAlign.CENTER, VerticalAlign.CENTER);

            startY += 1;
            r = new Rectangle(10, startY, rect.width() - 10, 1);
            screen.writerBuilder()
                    .color(Constant.FONT_COLOR)
                    .build()
                    .text("Licence: MIT", r, HorizontalAlign.CENTER, VerticalAlign.CENTER);

            startY += 4;
            r = new Rectangle(10, startY, rect.width() - 10, 1);
            screen.writerBuilder()
                    .color(Constant.FONT_COLOR)
                    .build()
                    .text("How to play", r, HorizontalAlign.CENTER, VerticalAlign.CENTER);

            startY += 2;
            r = new Rectangle(10, startY, rect.width() - 10, 1);
            screen.writerBuilder()
                    .color(Constant.FONT_COLOR)
                    .build()
                    .text("To choose option use CTRL key press plus char in brackets in front of option. Example: CTRL + Q for quit.", r, HorizontalAlign.CENTER, VerticalAlign.CENTER);

            startY += 1;
            r = new Rectangle(10, startY, rect.width() - 10, 1);
            screen.writerBuilder()
                    .color(Constant.FONT_COLOR)
                    .build()
                    .text("To start playing game press CTRL + P.", r, HorizontalAlign.CENTER, VerticalAlign.CENTER);
            return rect;
        });

        return view;
    }
}
