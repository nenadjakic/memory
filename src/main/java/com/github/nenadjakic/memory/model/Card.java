package com.github.nenadjakic.memory.model;

import com.github.nenadjakic.memory.util.Constant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.shell.component.view.control.BoxView;
import org.springframework.shell.component.view.control.View;
import org.springframework.shell.geom.HorizontalAlign;
import org.springframework.shell.geom.Rectangle;
import org.springframework.shell.geom.VerticalAlign;

import static com.github.nenadjakic.memory.util.Constant.CARD_RESOLVED_FONT_COLOR;
import static com.github.nenadjakic.memory.util.Constant.FOCUSED_FONT_COLOR;

@Getter
@Setter
public class Card {

    private final String shortcut;
    private final char symbol;
    private CardState state;

    public Card(char symbol, String shortcut) {
        this.symbol = symbol;
        this.shortcut = shortcut;
        this.state = CardState.CLOSED;
    }

    public View draw() {
        var view = new BoxView();
        view.setShowBorder(true);
        view.setDrawFunction(((screen, rectangle) -> {
            Rectangle r;
            if (CardState.OPENED == state || CardState.RESOLVED == state) {

                r = new Rectangle(rectangle.x(), rectangle.y(), rectangle.width(), rectangle.height());
                screen.writerBuilder()
                        .color(CardState.OPENED == state ? FOCUSED_FONT_COLOR : CARD_RESOLVED_FONT_COLOR)
                        .style(1)
                        .build()
                        .text(String.valueOf(symbol), r, HorizontalAlign.CENTER, VerticalAlign.CENTER);
            }

            r = new Rectangle(rectangle.x(), rectangle.y(), rectangle.width() - 1, rectangle.height() - 1);
            screen.writerBuilder()
                   .color(Constant.CARD_FONT_SHORTCUT)
                   .build()
                   .text("[" + shortcut + "]", r, HorizontalAlign.RIGHT, VerticalAlign.BOTTOM);
            return rectangle;
        }));
        return view;
    }
}
