package com.example.circuitgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class GameView extends SurfaceView implements Runnable {
    SurfaceHolder surfaceHolder;
    Thread thread;
    boolean isRunning=true;

    List<GameObject> objects;
    Paint white;
    public GameView(final Context context) {
        super(context);
        objects = new ArrayList<>();
        surfaceHolder = getHolder();
        white = new Paint();
        white.setColor(Color.WHITE);
        Vector2D position = new Vector2D(100,100);
        GameObject square = new PhysicsObject(ContextCompat.getDrawable(context, R.drawable.square), position, 1, 1, position);
        objects.add(square);
        objects = new ArrayList<>();

        OnTouchListener touchListener = new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Vector2D touch = new Vector2D(event.getX(),event.getY());

                return true;
            }
        };

        setOnTouchListener(touchListener);

        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while(isRunning)
        {
            if(!surfaceHolder.getSurface().isValid())
                continue;
            Canvas canvas = surfaceHolder.lockCanvas();
            canvas.drawRect(0,0,canvas.getWidth(),canvas.getHeight(), white);
            for (int i = 0; i < objects.size(); i++) {
                objects.get(i).draw(canvas);
            }
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }
}
