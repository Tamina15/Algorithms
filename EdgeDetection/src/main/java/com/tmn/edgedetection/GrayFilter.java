package com.tmn.edgedetection;

import java.awt.image.RGBImageFilter;

public class GrayFilter extends RGBImageFilter {

    @Override
    public int filterRGB(int x, int y, int rgb) {
        int r = (rgb >> 16) & 0xff;
        int g = (rgb >> 8) & 0xff;
        int b = rgb & 0xff;
        int gray = r;
        if (g > gray) {
            gray = g;
        }
        if (b > gray) {
            gray = b;
        }
//        int gray = (r + b + g) / 3;
        if (gray < 0) {
            gray = 0;
        }
        if (gray > 255) {
            gray = 255;
        }
        return (rgb & 0xff000000) | (gray << 16) | (gray << 8) | (gray << 0);
    }

}
