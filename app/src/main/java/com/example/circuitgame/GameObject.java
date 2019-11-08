package com.example.circuitgame;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

class Vector2D {
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
}

public class GameObject {
    protected Vector2D position;
    private float width;
    private float height;
    private Drawable image;

    public GameObject(Drawable image, Vector2D position) {
        this.position = position.clone();
        this.image = image;
        width = image.getIntrinsicWidth();
        height = image.getIntrinsicHeight();
    }

    public void draw(Canvas canvas) {
        image.setBounds((int) position.x, (int) position.y, (int) (position.x + width), (int) (position.y + height));
        image.draw(canvas);
    }
}
