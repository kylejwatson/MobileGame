package com.kyle.circuitgame.game;

import android.graphics.Canvas;
import android.graphics.Rect;

class GameObject {
    Vector2D position;
    private DrawObject mDrawObject;

    GameObject(DrawObject drawObject, Vector2D position) {
        this.position = position.clone();
        mDrawObject = drawObject;
    }

    void draw(Canvas canvas) {
        mDrawObject.draw(canvas, position);
    }

    Rect getRect() {
        return mDrawObject.getRect(position);
    }
}

