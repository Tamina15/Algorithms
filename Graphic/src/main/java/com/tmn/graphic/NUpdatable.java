package com.tmn.graphic;

public interface NUpdatable {

    public abstract void update();

    public default void update(double fps, double ups) {
        update();
    }
}
