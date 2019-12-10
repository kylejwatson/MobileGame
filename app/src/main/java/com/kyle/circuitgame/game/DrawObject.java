package com.kyle.circuitgame.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

class DrawObject {
    private float width;
    private float height;
    private Drawable image;
    private Paint paint;
    private Bitmap bitmap;

    DrawObject(Drawable image) {
        this.image = image;
        width = image.getIntrinsicWidth();
        height = image.getIntrinsicHeight();
    }

    DrawObject(Paint paint, float width, float height) {
        this.paint = paint;
        this.width = width;
        this.height = height;
    }

    DrawObject(Bitmap bitmap) {
        this.bitmap = bitmap;
        width = bitmap.getWidth();
        height = bitmap.getHeight();
    }

    void draw(Canvas canvas, Vector2D position) {
        if (paint != null)
            canvas.drawRect(getRect(position), paint);
        if (image != null) {
            image.setBounds(getRect(position));
            image.draw(canvas);
        }
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, position.x, position.y, paint);
        }
    }

    Rect getRect(Vector2D position){
        return new Rect((int) position.x, (int) position.y, (int) (position.x + width), (int) (position.y + height));
    }
}
