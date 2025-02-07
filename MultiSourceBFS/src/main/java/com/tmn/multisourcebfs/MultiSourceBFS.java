package com.tmn.multisourcebfs;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 *
 * @author nhat.tranminh
 */
public class MultiSourceBFS {

    BufferedImage image;
    int[] pixels, shorts, isWater;
    int height, width, length, heightScale, widthScale, offsetX, offsetY;
    boolean hasAlphaChannel;
    byte pixelLength;
    Queue<Integer> queue = new LinkedList<>();

    public MultiSourceBFS(int width, int height) {
        this.width = width;
        this.height = height;
        this.widthScale = width;
        this.heightScale = height;
        this.length = width * height;
        this.isWater = new int[length];

        image = new BufferedImage(width, height, 1);
        hasAlphaChannel = image.getAlphaRaster() != null;
        pixelLength = (byte) (hasAlphaChannel ? 4 : 3);
        this.pixels = image.getRaster().getPixels(0, 0, width, height, (int[]) null);
        this.shorts = new int[length];

        pixel = new int[pixelLength];
        init();
    }

    public MultiSourceBFS(int width, int height, int offsetX, int offsetY) {
        this(width, height);
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public MultiSourceBFS(int width, int height, int offsetX, int offsetY, int widthScale, int heightScale) {
        this(width, height, offsetX, offsetY);
        this.widthScale = widthScale;
        this.heightScale = heightScale;
    }

    private void init() {
        int black = Color.BLACK.getRGB();
        for (int i = 0; i < length; i++) {
            isWater[i] = -1;
            shorts[i] = black;
        }
        randomColor();
    }

    public void reset(int amount) {
        init();
        randomWater(amount);
    }

    public void setWater(int x, int y) {
        int position = y * width + x;
        isWater[position] = 0;
        shorts[position] = Color.blue.getRGB();
        queue.add(position);
        setPixel(position);
    }

    public void randomWater(int amount) {
        Random r = new Random();
        int blue = Color.BLUE.getRGB();
        for (int i = 0; i < amount; i++) {
            int x = r.nextInt(length);
            isWater[x] = 0;
            shorts[x] = blue;
            queue.add(x);
        }
//        setPixels();
    }

    public void randomColor() {
        Random rand = new Random();
        colorsInt = new int[150];
        int c = rand.nextInt();
        for (int i = 0; i < colorsInt.length; i++) {
            if (i % 15 == 0) {
                c = rand.nextInt();
            }
            colorsInt[i] = c;
        }
    }

    public int[] colorsInt;

    public void draw(Graphics2D g2d) {
        g2d.drawImage(image, offsetX, offsetY, widthScale, heightScale, null);
    }

    private void runOneNode(boolean update) {
        int position = queue.poll();
        int x = position % width; // width
        int y = position / width; // height

        int up = y > 0 ? position - width : -1;
        updatePeakHeight(up, position, update);

        int right = x < width - 1 ? position + 1 : -1;
        updatePeakHeight(right, position, update);

        int down = y < height - 1 ? position + width : -1;
        updatePeakHeight(down, position, update);

        int left = x > 0 ? position - 1 : -1;
        updatePeakHeight(left, position, update);
    }

    private void updatePeakHeight(int neighbor, int position, boolean update) {
        if (neighbor != -1 && isWater[neighbor] == -1) {
            isWater[neighbor] = isWater[position] + 1;
            shorts[neighbor] = colorsInt[isWater[neighbor] % colorsInt.length];
            queue.add(neighbor);
            if (update) {
                setPixel(neighbor);
            }
        }
    }

    private void runOnce(boolean update) {
        int n = queue.size();
        for (int i = 0; i < n; i++) {
            runOneNode(update);
        }
    }

    public void run(int times) {
        run(times, true);
    }

    public void run(int times, boolean update) {
        if (queue.isEmpty()) {
            return;
        }
        for (int i = 0; i < times; i++) {
            if (!queue.isEmpty()) {
                runOnce(update);
            } else {
                break;
            }
        }
        if (!update) {
            setPixels();
        }
    }

    public boolean isDone() {
        return queue.isEmpty();
    }

    int[] pixel;

    private void setPixel(int position) {
        int x = position % width; // width
        int y = position / width; // height
        int argb = shorts[position];
        int pos = 0;
        if (hasAlphaChannel) {
            pixel[pos++] = (argb >> 24 & 0xff);
        }
        pixel[pos++] = (argb >> 16 & 0xff);
        pixel[pos++] = (argb >> 8 & 0xff);
        pixel[pos++] = (argb & 0xff);
        image.getRaster().setPixel(x, y, pixel);
    }

    /**
     * @deprecated use setPixel for better performance
     */
    @Deprecated
    private void setPixels() {
        for (int i = 0; i < length; i++) {
            int argb = shorts[i];
            int pos = i * pixelLength;
            if (hasAlphaChannel) {
                pixels[pos++] = (argb >> 24 & 0xff);
            }
            pixels[pos++] = (argb >> 16 & 0xff);
            pixels[pos++] = (argb >> 8 & 0xff);
            pixels[pos++] = (argb & 0xff);
        }
        image.getRaster().setPixels(0, 0, width, height, pixels);
    }
}
