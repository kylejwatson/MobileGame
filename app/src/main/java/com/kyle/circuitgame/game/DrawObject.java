package com.kyle.circuitgame.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

class DrawObject {
    private float mWidth;
    private float mHeight;
    private Drawable mImage;
    private Paint mPaint;
    private Bitmap mBitmap;

    DrawObject(Drawable image) {
        mImage = image;
        mWidth = image.getIntrinsicWidth();
        mHeight = image.getIntrinsicHeight();
    }

    DrawObject(Paint paint, float width, float height) {
        mPaint = paint;
        mWidth = width;
        mHeight = height;
    }

    DrawObject(Bitmap bitmap) {
        mBitmap = bitmap;
        mWidth = bitmap.getWidth();
        mHeight = bitmap.getHeight();
    }

    void draw(Canvas canvas, Vector2D position) {
        if (mPaint != null) canvas.drawRect(getRect(position), mPaint);
        if (mBitmap != null) canvas.drawBitmap(mBitmap, position.x, position.y, mPaint);
        if (mImage == null) return;
        mImage.setBounds(getRect(position));
        mImage.draw(canvas);
    }

    Rect getRect(Vector2D position) {
        return new Rect((int) position.x, (int) position.y, (int) (position.x + mWidth),
                (int) (position.y + mHeight));
    }
}
