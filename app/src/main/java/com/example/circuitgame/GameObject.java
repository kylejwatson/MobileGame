package com.example.circuitgame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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

    @Override
    public String toString(){
        return "X: " + x + " Y: " + y;
    }
}

public class GameObject {
    protected Vector2D position;
    private float width;
    private float height;
    private Drawable image;
    private Paint paint = new Paint();


    public GameObject(Drawable image, Vector2D position) {
        this.position = position.clone();
        this.image = image;
        width = image.getIntrinsicWidth();
        height = image.getIntrinsicHeight();
        paint.setColor(Color.RED);
    }

    public void draw(Canvas canvas) {
        canvas.drawRect(position.x, position.y, position.x + width, position.y + height, paint);
        image.setBounds((int) position.x, (int) position.y, (int) (position.x + width), (int) (position.y + height));
        image.draw(canvas);
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}

