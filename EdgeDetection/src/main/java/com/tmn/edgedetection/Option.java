package com.tmn.edgedetection;

public class Option {

    private double highFraction;
    private double lowFraction;

    public Option(double highFraction, double lowFraction) {
        this.highFraction = highFraction;
        this.lowFraction = lowFraction;
    }

    private double cap(double value, double low, double high) {
        if (value < low) {
            return low;
        }
        if (value > high) {
            return high;
        }
        return value;
    }

    private double cap(double value) {
        return cap(value, 0, 1);
    }

    public double getHighFraction() {
        return highFraction;
    }

    public void setHighFraction(double highFraction) {
        this.highFraction = highFraction;
    }

    public double getLowFraction() {
        return lowFraction;
    }

    public void setLowFraction(double lowFraction) {
        this.lowFraction = cap(lowFraction);
    }

}
