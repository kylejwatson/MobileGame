package com.example.circuitgame;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class GameView extends SurfaceView {
    private static final int TICK = 1000/120;
    private final float pixelsToMetres;
    SurfaceHolder surfaceHolder;
    Thread drawThread;
    Thread physicsThread;
    boolean isRunning = true;
    long previousTime;
    SensorManager sensorManager;
    Sensor sensor;

    List<GameObject> objects;
    Paint white;
    Vector2D gravity;

    Runnable drawRunnable = new Runnable() {
        @Override
        public void run() {
            while (isRunning) {
                if (!surfaceHolder.getSurface().isValid())
                    continue;
                Canvas canvas = surfaceHolder.lockCanvas();
                if (canvas == null)
                    continue;
                canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), white);
                for (int i = 0; i < objects.size(); i++) {
                    objects.get(i).draw(canvas);
                }
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    };

    Runnable physicsRunnable = new Runnable() {
        @Override
        public void run() {
            while (isRunning) {
                long currentTime = System.currentTimeMillis();
                float changeInTime = (currentTime - previousTime);
//                if (changeInTime < TICK)
//                    continue;

                previousTime = currentTime;


                for (int i = 0; i < objects.size(); i++) {
                    GameObject object = objects.get(i);
                    if (object instanceof PhysicsObject)
                        ((PhysicsObject) object).update(pixelsToMetres * changeInTime/1000f);
                }
            }
        }
    };

    public GameView(final Context context) {
        super(context);
        objects = new ArrayList<>();
        surfaceHolder = getHolder();
        white = new Paint();
        white.setColor(Color.WHITE);
        Vector2D position = new Vector2D(1000, 1800);
        gravity = new Vector2D(0, 0);
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        pixelsToMetres = metrics.densityDpi * 39.37008f;



        for (int i = 0; i < 1; i++) {
            PhysicsObject physicsObject = new PhysicsObject(
                    ContextCompat.getDrawable(context, R.drawable.square),
                    position.multiply(Math.random()),
                    (float) Math.random(),
                    (float) Math.random() * 0.8f);
            physicsObject.gravity = gravity;
            objects.add(physicsObject);
        }

        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

        sensorManager.registerListener(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                Log.d("GRAV", "x: " + event.values[0] + " y: " + event.values[1]);
                gravity.x = -event.values[0];
                gravity.y = event.values[1];
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        }, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        OnTouchListener touchListener = new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Vector2D touch = new Vector2D(event.getX(), event.getY());
                PhysicsObject physicsObject = new PhysicsObject(
                        ContextCompat.getDrawable(context, R.drawable.square),
                        touch,
                        (float) Math.random(),
                        (float) Math.random() * 0.8f);
                physicsObject.gravity = gravity;
                objects.add(physicsObject);
                return true;
            }
        };

        setOnTouchListener(touchListener);
        previousTime = System.currentTimeMillis();
        drawThread = new Thread(drawRunnable);
        drawThread.start();
        physicsThread = new Thread(physicsRunnable);
        physicsThread.start();
    }
}
