package com.tmn.graphic;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;

public class NFrame extends JFrame implements NUpdatable {

    public double fps, ups;
    private final List<NUpdatable> updatables = new ArrayList<>();

    public NFrame() {
        super();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setEnabled(true);
    }

    @Override
    protected void addImpl(Component comp, Object constraints, int index) {
        super.addImpl(comp, constraints, index);
        if (comp instanceof NUpdatable u) {
            updatables.add(u);
        }
    }

    public void update() {
        updatables.forEach((t) -> {
            t.update(fps, ups);
        });
    }

}
