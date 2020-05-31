package com.itmo.collection;

import java.io.Serializable;

/**
 * комментарий
 */
public class Coordinates implements Serializable {
    private Integer x; //Поле не может быть null
    private long y;

    /**
     * Instantiates a new Coordinates.
     *
     * @param x the x
     * @param y the y
     */
    public Coordinates(Integer x, long y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets x.
     *
     * @return the x
     */
    public Integer getX() {
        return x;
    }

    /**
     * Gets y.
     *
     * @return the y
     */
    public long getY() {
        return y;
    }
}
