package com.tmn.flockingsimulation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;

public class ControlPanel extends JPanel {

    private final int width, height;

    public ControlPanel(int width, int height) {
        this.width = width;
        this.height = height;
        System.out.println(width);
        this.setPreferredSize(new Dimension(width, height));
        this.setDoubleBuffered(true);
        this.setBackground(new Color(212, 212, 212));
        this.setFocusable(true);
        init();
    }

    private void init() {
        FlowLayout flow = new FlowLayout(FlowLayout.CENTER, 0, 20);
        this.setLayout(flow);
        newZoomPanel();
        newMatchingPerceptionPanel();
        newAlignPerceptionPanel();
        newAvoidPerceptionPanel();
    }

    private JComponent[] newPanel(String name, int sliderDefault) {
        JPanel row = new JPanel();
        row.setPreferredSize(new java.awt.Dimension(380, 60));
        row.setLayout(new BorderLayout(0, 20));
        JLabel label = new JLabel(name);
        label.setPreferredSize(new Dimension(100, 16));
        row.add(label, BorderLayout.LINE_START);
        JSlider slider = new JSlider(1, 100, sliderDefault);
        row.add(slider, BorderLayout.CENTER);
        this.add(row);
        return new JComponent[]{label, slider};
    }

    private void newZoomPanel() {
        String name = "Zoom";
        JComponent[] component = newPanel(name, 10);
        JSlider slider = (JSlider) component[1];
        JLabel label = (JLabel) component[0];
        slider.addChangeListener((ChangeEvent e) -> {
            JSlider source = (JSlider) e.getSource();
            int value = source.getValue();
            Option.zoomFactor = value * 1.0 / 10;
            label.setText(name + ": " + value);
        });
    }

    private void newMatchingPerceptionPanel() {
        String name = "Matching";
        JComponent[] component = newPanel(name, Option.matchingPerception);
        JSlider slider = (JSlider) component[1];
        JLabel label = (JLabel) component[0];
        slider.setMaximum(200);
        slider.addChangeListener((ChangeEvent e) -> {
            JSlider source = (JSlider) e.getSource();
            int value = source.getValue();
            Option.matchingPerception = value;
            Option.matchingPerceptionSq = value * value;
            label.setText(name + ": " + value);
        });
    }

    private void newAlignPerceptionPanel() {
        String name = "Align";
        JComponent[] component = newPanel(name, Option.alignPerception);
        JSlider slider = (JSlider) component[1];
        JLabel label = (JLabel) component[0];
        slider.setMaximum(200);
        slider.addChangeListener((ChangeEvent e) -> {
            JSlider source = (JSlider) e.getSource();
            int value = source.getValue();
            Option.alignPerception = value;
            Option.alignPerceptionSq = value * value;
            label.setText(name + ": " + value);
        });
    }

    private void newAvoidPerceptionPanel() {
        String name = "Avoid";
        JComponent[] component = newPanel(name, Option.avoidPerception);
        JSlider slider = (JSlider) component[1];
        JLabel label = (JLabel) component[0];
        slider.setMaximum(200);
        slider.addChangeListener((ChangeEvent e) -> {
            JSlider source = (JSlider) e.getSource();
            int value = source.getValue();
            Option.avoidPerception = value;
            Option.avoidPerceptionSq = value * value;
            label.setText(name + ": " + value);
        });
    }

}
