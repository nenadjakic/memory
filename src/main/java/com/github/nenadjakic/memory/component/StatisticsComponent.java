package com.github.nenadjakic.memory.component;

import org.springframework.shell.component.view.control.BoxView;
import org.springframework.shell.component.view.control.View;

public class StatisticsComponent extends AbstractGameComponent {
    @Override
    public View build() {
        var view = new BoxView();
        view.setTitle("STATISTICS");
        view.setShowBorder(true);
        return view;
    }
}
