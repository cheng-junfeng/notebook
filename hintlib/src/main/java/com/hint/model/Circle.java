package com.hint.model;

import android.graphics.PointF;


public class Circle {
    private PointF center;
    private float radius;
    private int color;

    public Circle() {
        center = new PointF();
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public void setCenter(float x, float y) {
        center.set(x, y);
    }

    public PointF getCenter() {
        return center;
    }

    public void setCenter(PointF center) {
        this.center = center;
    }

    public float getRadius() {
        return radius;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}