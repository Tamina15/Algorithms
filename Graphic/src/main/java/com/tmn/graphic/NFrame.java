package com.tmn.graphic;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;

public class NFrame extends JFrame implements NUpdatable {

    private final List<NUpdatable> updatables = new ArrayList<>();

    public NFrame() {
        super();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setEnabled(true);
    }

    @Override
    public Component add(Component comp) {
        Component c = super.add(comp);
        if (c instanceof NUpdatable u) {
            updatables.add(u);
        }
        return c;
    }

    public void update() {
        updatables.forEach((t) -> {
            t.update();
        });
    }

}
