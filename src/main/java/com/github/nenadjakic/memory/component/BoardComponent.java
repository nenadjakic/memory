package com.github.nenadjakic.memory.component;

import com.github.nenadjakic.memory.Pairs;
import com.github.nenadjakic.memory.model.Card;
import com.github.nenadjakic.memory.model.CardState;
import com.github.nenadjakic.memory.util.Constant;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.shell.component.view.control.GridView;
import org.springframework.shell.component.view.control.View;
import org.springframework.shell.component.view.event.KeyEvent;
import org.springframework.shell.component.view.screen.Color;
import org.springframework.shell.geom.HorizontalAlign;
import org.springframework.shell.geom.Rectangle;
import org.springframework.shell.geom.VerticalAlign;
import org.springframework.shell.style.StyleSettings;

import java.awt.*;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class BoardComponent extends AbstractGameComponent {
    private static final Logger log = LoggerFactory.getLogger(BoardComponent.class);

    private final Pairs pairs;
    private final Table<Integer, Integer, Card> gameMap = HashBasedTable.create();
    private final Map<Integer, Card> shortcutCardMap = new HashMap<>();

    private int rows;
    private int columns;

    @Getter
    private int resolvedPairs;

    @Getter
    private int unresolvedPairs;

    private boolean isGameFinished() {
        return resolvedPairs == pairs.getNumberOfPairs();
    }

    private Card card1;
    private Card card2;
    private Integer firstUserNumber;
    private Integer secondUserNumber;
    private String tempNumber = "";

    public BoardComponent(Pairs pairs) {
        this.pairs = pairs;
        initGame(pairs);
    }

    public void initGame(Pairs pairs) {
        setRowAndColumn(pairs);

        Supplier<Stream<Integer>> pairKeysSupplier = () -> IntStream.rangeClosed(1, pairs.getNumberOfPairs()).boxed();
        var pairKeys = pairKeysSupplier.get().collect(Collectors.toCollection(LinkedList::new));
        pairKeys.addAll(pairKeysSupplier.get().toList());
        Collections.shuffle(pairKeys);

        clear();

        var numbers = IntStream.rangeClosed(1, 32).boxed().collect(Collectors.toCollection(LinkedList::new));
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                int shortcut = numbers.pop();
                var card = new Card((char)(64 + pairKeys.pop()), String.valueOf(shortcut));
                gameMap.put(r, c, card);
                shortcutCardMap.put(shortcut, card);
            }
        }
    }

    private void setRowAndColumn(Pairs pairs) {
        switch (pairs) {
            case FOUR -> {
                rows = 2;
                columns = 4;
            }
            case EIGHT -> {
                rows = 4;
                columns = 4;
            }
            case TWELVE -> {
                rows = 6;
                columns = 4;
            }
            case SIXTEEN -> {
                rows = 6;
                columns = 6;
            }
        }
    }
    private boolean isNumber(int key) {
        return Arrays.asList(KeyEvent.Key.Zero, KeyEvent.Key.One, KeyEvent.Key.Two, KeyEvent.Key.Three, KeyEvent.Key.Four,
                        KeyEvent.Key.Five, KeyEvent.Key.Six, KeyEvent.Key.Seven, KeyEvent.Key.Eight, KeyEvent.Key.Nine)
                .contains(key);
    }

    private void clear() {
        unresolvedPairs = pairs.getNumberOfPairs();
        resolvedPairs = 0;
        gameMap.clear();
        shortcutCardMap.clear();
        card1 = null;
        card2 = null;
        firstUserNumber = null;
        secondUserNumber = null;
        tempNumber = "";
    }


    @Override
    public View build() {
        var view = new GridView();
        view.setColumnSize(0);
        view.setRowSize(0);

        for (int r = 0; r < gameMap.rowMap().size(); r++) {
            for (int c = 0; c < gameMap.columnMap().size(); c++)  {
                var card = gameMap.get(r, c);
                view.addItem(card.draw(), r, c, 1,1, 0, 0);
            }
        }

        getEventloop().onDestroy(getEventloop().keyEvents().subscribe(keyEvent -> {
            // save result to temp variables
            if (KeyEvent.Key.Enter == keyEvent.key()) {
                if (!tempNumber.isEmpty()) {
                    if (firstUserNumber == null && secondUserNumber == null) {
                        firstUserNumber = Integer.valueOf(tempNumber);
                        // if number is not valid, reset temp variable
                        var card = shortcutCardMap.get(firstUserNumber);
                        if (card != null) {
                            if  (card.getState() == CardState.RESOLVED) {
                                firstUserNumber = null;
                            } else {
                                card.setState(CardState.OPENED);
                            }
                        } else {
                            firstUserNumber = null;
                        }
                        tempNumber = "";
                    } else if (firstUserNumber != null && secondUserNumber == null) {
                        secondUserNumber = Integer.valueOf(tempNumber);
                        card1 = shortcutCardMap.get(firstUserNumber);
                        card2 = shortcutCardMap.get(secondUserNumber);
                        // if number is not valid, reset temp variable
                        // card1 is allready checked in previuos if block
                        if (card2 != null) {
                            if (card2.getState() == CardState.RESOLVED) {
                                secondUserNumber = null;
                            } else {
                                card2.setState(CardState.OPENED);
                            }
                        } else {
                            secondUserNumber = null;
                        }

                        if (card1.getSymbol() == card2.getSymbol()) {
                            card1.setState(CardState.RESOLVED);
                            card2.setState(CardState.RESOLVED);
                            resolvedPairs++;
                            unresolvedPairs--;

                            if (!isGameFinished()) {
                                view.setDrawFunction(((screen, rectangle) -> {
                                    log.debug("x,y,w,h, " + rectangle.x()+","+ rectangle.y()+","+rectangle.width()+","+ rectangle.height());
                                    Rectangle r = new Rectangle(rectangle.x(), rectangle.y(), rectangle.width(), rectangle.height());
                                    screen.writerBuilder()
                                            .color(Color.BLUE)
                                            .style(1)
                                            .build()
                                            .border(rectangle.x(), rectangle.y(), rectangle.width(), rectangle.height());
                                    screen.writerBuilder()
                                            .color(Color.GOLDENROD)
                                            .style(16)
                                            .build()
                                            .text("Game over! You found all pairs!", r, HorizontalAlign.CENTER, VerticalAlign.CENTER);
                                    return rectangle;
                                }));
                            }
                        }
                        tempNumber = "";
                    }
                } else if (firstUserNumber != null && secondUserNumber != null) {
                    firstUserNumber = null;
                    secondUserNumber = null;

                    if (CardState.RESOLVED != card1.getState()) {
                        card1.setState(CardState.CLOSED);
                    }
                    if (CardState.RESOLVED != card2.getState()) {
                        card2.setState(CardState.CLOSED);
                    }
                }
            }
            if (firstUserNumber != null && secondUserNumber != null) {
                return;
            }
            if (isNumber(keyEvent.key())) {
                tempNumber += (char)keyEvent.key();
            }
        }));

        return view;
    }
}
