package com.tmn.edgedetection;

public class Option {

    /**
     * Number of standard deviations above mean for high threshold
     */
    private double numDev;

    /**
     * The fraction in which higher values
     * are considered to be strong edge
     */
    private double lowFraction;

    public Option(double numDev, double lowFraction) {
        this.numDev = numDev;
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

    public double getNumDev() {
        return numDev;
    }

    public void setNumDev(double numDev) {
        this.numDev = numDev;
    }

    public double getLowFraction() {
        return lowFraction;
    }

    public void setLowFraction(double lowFraction) {
        this.lowFraction = cap(lowFraction);
    }

}
