package com.kyle.circuitgame.game;

import androidx.annotation.NonNull;

class Vector2D {
    static final Vector2D ZERO = new Vector2D(0, 0);
    float x, y;

    Vector2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @NonNull
    @Override
    protected Vector2D clone() {
        return new Vector2D(x, y);
    }

    @Override
    public String toString() {
        return "X: " + x + " Y: " + y;
    }

    Vector2D plus(Vector2D vector2D) {
        return new Vector2D(x + vector2D.x, y + vector2D.y);
    }

    void translate(Vector2D vector2D) {
        x += vector2D.x;
        y += vector2D.y;
    }

    Vector2D multiply(float scale) {
        return new Vector2D(x * scale, y * scale);
    }

    float length() {
        return (float) Math.sqrt(x * x + y * y);
    }
}