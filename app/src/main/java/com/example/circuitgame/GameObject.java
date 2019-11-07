package com.example.circuitgame;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

class Vector2D {
    public float x, y;

    public Vector2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D plus(Vector2D vector2D) {
        return new Vector2D(x + vector2D.x, y+vector2D.y);
    }

    public void add(Vector2D vector2D) {
        x += vector2D.x;
        y += vector2D.y;
    }
}

public class GameObject {
    protected Vector2D position;
    private float width;
    private float height;
    private Drawable image;

    public GameObject(Drawable image, Vector2D position) {
        this.position = position;
        this.image = image;
        width = image.getIntrinsicWidth();
        height = image.getIntrinsicHeight();
    }

    public GameObject(Drawable image) {
        this.image = image;
    }

    public void draw(Canvas canvas) {
        image.setBounds((int) position.x, (int) position.y, (int) (position.x + width), (int) (position.y + width));
        image.draw(canvas);
    }
}
