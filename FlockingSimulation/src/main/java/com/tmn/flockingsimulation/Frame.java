package com.tmn.flockingsimulation;

import java.awt.BorderLayout;
import javax.swing.JFrame;

public class Frame extends JFrame {

    MainPanel mainPanel = new MainPanel(600, 600);
    ControlPanel controlPanel = new ControlPanel(400, 600);

    public Frame() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setEnabled(true);
        init();
    }

    final void init() {
        add(mainPanel, BorderLayout.LINE_START);
        add(controlPanel, BorderLayout.LINE_END);
        pack();
    }

    public void update() {
        mainPanel.update();
    }
}
