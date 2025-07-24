package com.tmn.graphic;

import java.awt.Component;
import java.util.ArrayList;
import javax.swing.JFrame;

public class NFrame extends JFrame implements NUpdatable {

    ArrayList<NUpdatable> updatables;

    public NFrame() {
        updatables = new ArrayList<>();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setEnabled(true);
    }

    @Override
    public Component add(Component comp) {
        Component c = super.add(comp);
        if (NUpdatable.class.isAssignableFrom(c.getClass())) {
            updatables.add((NUpdatable) comp);
        }
        return c;
    }

    public void update() {
        updatables.forEach((t) -> {
            t.update();
        });
    }

}
