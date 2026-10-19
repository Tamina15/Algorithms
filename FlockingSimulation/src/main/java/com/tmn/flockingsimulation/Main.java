package com.tmn.flockingsimulation;

import com.tmn.graphic.NFrame;
import com.tmn.graphic.NMain;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        JFrame frame = new NFrame();
        JPanel mainPanel = new MainPanel(600, 600);
        JPanel controlPanel = new ControlPanel(400, 600);
        frame.add(mainPanel, BorderLayout.LINE_START);
        frame.add(controlPanel, BorderLayout.LINE_END);
        NMain main = new NMain(frame);
        main.run();
    }

}
