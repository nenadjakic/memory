package com.github.nenadjakic.memory.component;

import org.springframework.shell.component.view.control.BoxView;
import org.springframework.shell.component.view.control.View;
import org.springframework.shell.component.view.screen.Color;
import org.springframework.shell.style.StyleSettings;

public class AboutComponent extends AbstractGameComponent {
    @Override
    public View build() {
        var view = new BoxView();
        view.setShowBorder(true);
        view.setTitleColor(Color.GOLD);


        view.setDrawFunction((screen, rect) -> {
            //Rectangle r = new Rectangle(0, rect.y(), title.length(), 1);
            screen.writerBuilder().color(Color.AQUAMARINE)
                    .build().text("Memory match game", 10, 10);

            screen.writerBuilder().color(Color.AQUAMARINE)
                    .build().text("MIT Licence", 10, 12);

            return rect;
        });

        return view;
    }
}
