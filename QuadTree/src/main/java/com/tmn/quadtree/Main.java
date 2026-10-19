package com.tmn.quadtree;

import com.tmn.graphic.NFrame;
import com.tmn.graphic.NMain;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main {

    public static void main(String[] args) {
        JPanel p = new Panel(800, 600);
        JFrame f = new NFrame();
        f.add(p);
        NMain main = new NMain(f);
        main.run();
    }
}
