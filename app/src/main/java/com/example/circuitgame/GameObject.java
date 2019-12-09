package com.example.circuitgame;

import android.graphics.Canvas;

import androidx.annotation.NonNull;

class Vector2D {
    public static final Vector2D ZERO = new Vector2D(0, 0);
    public float x, y;

    @NonNull
    @Override
    protected Vector2D clone() {
        return new Vector2D(x, y);
    }

    public Vector2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D plus(Vector2D vector2D) {
        return new Vector2D(x + vector2D.x, y+vector2D.y);
    }

    public void translate(Vector2D vector2D) {
        x += vector2D.x;
        y += vector2D.y;
    }

    public Vector2D multiply(float scale) {
        return new Vector2D(x * scale, y * scale);
    }
    public Vector2D multiply(double scale) {
        return new Vector2D(x * (float)scale, y * (float)scale);
    }

    @Override
    public String toString(){
        return "X: " + x + " Y: " + y;
    }
}

public class GameObject {
    protected Vector2D position;
    private DrawObject drawObject;


    public GameObject(DrawObject drawObject, Vector2D position) {
        this.position = position.clone();
        this.drawObject = drawObject;
    }

    public void draw(Canvas canvas) {
        drawObject.draw(canvas, position);
    }

    public float getWidth() {
        return drawObject.getWidth();
    }

    public float getHeight() {
        return drawObject.getHeight();
    }
}

