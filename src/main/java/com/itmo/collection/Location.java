package com.itmo.collection;

import java.io.Serializable;

public class Location implements Serializable {
    private int x;
    private Long y; //Поле не может быть null
    private Float z; //Поле не может быть null
    private String name; //Поле не может быть null

    public Location(int x, Long y, Float z, String name) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
    }

    public int getX() {
        return x;
    }

    public Long getY() {
        return y;
    }

    public Float getZ() {
        return z;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "x: " + x +", y: "+ y +", z: "+ z +", locname: "+ name;
    }
}