package com.tmn.flockingsimulation;

public class Option {

    public static double zoomFactor = 1;
    public static int scale = 1;
    public static int boidCount = 50;
//    public static int maxBoidCount = 150;
    public static int maxVelocity = 10;
    
    public static int alignPerception = 50;
    public static int alignPerceptionSq = 50 * 50;
    
    public static int avoidPerception = 50;
    public static int avoidPerceptionSq = 50 * 50;
    
    public static int matchingPerception = 50;
    public static int matchingPerceptionSq = 50 * 50;
    
    public static double alignFactor = 10;
    public static double avoidFactor = 10;
    public static double matchingFactor = 100;
}
