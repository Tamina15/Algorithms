package com.tmn.graphic;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {

    public static void main(String[] args) {
        NPanel p = new NPanel(800,600);
        NFrame f = new NFrame();
        f.addWindowFocusListener(new WindowAdapter() {
            public void windowGainedFocus(WindowEvent e) {
                p.requestFocusInWindow();
            }
        });
        f.add(p);
        f.pack();
        f.setLocationRelativeTo(null);

        NMain main = new NMain(f);
        main.run();
    }
}
