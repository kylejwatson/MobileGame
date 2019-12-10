package com.example.circuitgame;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.SoundPool;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.List;

public class GameView extends SurfaceView {

    private static GameView instance;
    private boolean isPaused = false;
    private List<Level> levels;
    private int collisionSound;
    private final SoundPool soundPool;

    public boolean getPaused() {
        return isPaused;
    }

    public interface ObjectiveListener {
        void objectiveReached(Objective objective);
    }

    private float pixelsToMetres;
    private SurfaceHolder surfaceHolder;
    private boolean isRunning = true;

    private List<GameObject> objects;
    private List<GameObject> removeObjects;
    private Vector2D gravity;
    private int currentLevel = 0;

    public static GameView getInstance(final Context context, final ObjectiveListener objectiveListener, SoundPool soundPool) {
        if (instance != null) instance.stop();
        instance = new GameView(context, objectiveListener, soundPool);
        return instance;
    }

    public static GameView getInstance() {
        return instance;
    }

    private GameView(final Context context, final ObjectiveListener objectiveListener, SoundPool soundPool) {
        super(context);
        surfaceHolder = getHolder();

        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        pixelsToMetres = metrics.densityDpi * 39.37008f;

        this.soundPool = soundPool;
        collisionSound = soundPool.load(getContext(), R.raw.foot1, 1);
        final int pickupSound = soundPool.load(getContext(), R.raw.pickup, 1);

        objects = new ArrayList<>();
        removeObjects = new ArrayList<>();
        gravity = Vector2D.ZERO.clone();
        attachGravitySensor(context);

        levels = new ArrayList<>();

        Vector2D position = new Vector2D(1050, 1600);
        DrawObject profile = getProfile(context);

        Level.ObjectiveEvent event = new Level.ObjectiveEvent() {
            @Override
            public void trigger(final Objective objective) {
                removeObjects.add(objective);
                GameView.this.soundPool.play(pickupSound, 0.5f, 0.5f, 1, 0, 1);
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("OBJ", "next: " + (objective.getNextObjective() == null));
                        objectiveListener.objectiveReached(objective);
                    }
                });
                if (objective.getNextObjective() == null) {
                    loadLevel(objective);
                }
            }
        };

        Level level1 = new Level(Color.RED, event, gravity);
        level1.addWall(Vector2D.ZERO, 10, position.y);
        level1.addWall(Vector2D.ZERO, position.x, 10);
        level1.addWall(new Vector2D(position.x, 0), 10, position.y);
        level1.addWall(new Vector2D(0, position.y), position.x, 10);
        level1.addWall(new Vector2D(position.x / 2, position.y / 2), 10, position.y / 2);
        level1.addWall(new Vector2D(position.x / 2, position.y / 2), position.x / 4, 10);
        level1.addWall(new Vector2D((position.x * 3) / 4, position.y / 2), 10, position.y / 4);
        level1.addObjective("Cell", new DrawObject(ContextCompat.getDrawable(context, R.drawable.battery)), new Vector2D(position.x * 0.2f, position.y * 0.8f));
        level1.addObjective("Bulb", new DrawObject(ContextCompat.getDrawable(context, R.drawable.bulb)), new Vector2D((position.x * 5) / 8, (position.y * 5) / 8));
        PhysicsObject character = new PhysicsObject(profile, new Vector2D((position.x * 5) / 8, position.y / 4), 0.5f, 0.5f);
        level1.addPhysicsObject(character);

        int gap = 150;
        Level level2 = new Level(Color.RED, event, gravity);
        level2.addWall(Vector2D.ZERO, 10, position.y);
        level2.addWall(Vector2D.ZERO, position.x, 10);
        level2.addWall(new Vector2D(position.x, 0), 10, position.y);
        level2.addWall(new Vector2D(0, position.y), position.x, 10);

        level2.addWall(new Vector2D(position.x / 2, gap), 10, position.y / 2 - gap * 2);
        level2.addWall(new Vector2D(position.x / 2, position.y / 2 + gap), 10, position.y / 2 - gap * 2);
        level2.addWall(new Vector2D(position.x / 2 - gap, position.y / 2 - gap), gap * 2, 10);
        level2.addWall(new Vector2D(position.x / 2 - gap, position.y / 2 + gap), gap * 2, 10);
        level2.addWall(new Vector2D(position.x / 2 - gap, position.y / 2 - gap), 10, gap * 2);

        level2.addWall(new Vector2D(position.x / 2 + gap, position.y / 2 - gap), 10, gap / 2);
        level2.addWall(new Vector2D(position.x / 2 + gap, position.y / 2 + gap / 2), 10, gap / 2);
        level2.addWall(new Vector2D(gap, position.y / 2), position.x / 2 - gap * 2, 10);

        level2.addWall(new Vector2D(position.x / 2 + gap * 2, position.y / 2 - gap), position.x / 2 - gap * 2, 10);
        level2.addWall(new Vector2D(position.x / 2 + gap * 2, position.y / 2 + gap), position.x / 2 - gap * 2, 10);

        level2.addObjective("Motor", new DrawObject(ContextCompat.getDrawable(context, R.drawable.motor)), position.multiply(0.5f).plus(new Vector2D(-gap * 2, -gap * 2)));
        level2.addObjective("Switch", new DrawObject(ContextCompat.getDrawable(context, R.drawable.switchoff)), position.multiply(0.5f).plus(new Vector2D(gap * 2, gap * 2)));
        level2.addObjective("Buzzer", new DrawObject(ContextCompat.getDrawable(context, R.drawable.buzzer)), position.multiply(0.5f).plus(new Vector2D(-gap * 2, gap * 2)));
        character = new PhysicsObject(profile, position.multiply(0.5f), 0.1f, 0.5f);
        level2.addPhysicsObject(character);

        levels.add(level1);
        levels.add(level2);

        level1.loadLevel(objects);
        objectiveListener.objectiveReached(level1.getFirstObjective());

        new Thread(new Runnable() {
            @Override
            public void run() {
                drawScene();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                updateScene();
            }
        }).start();
    }

    private void loadLevel(Objective objective) {
        if (currentLevel < levels.size() - 1) {
            currentLevel++;
            levels.get(currentLevel).loadLevel(objects);
            objective.setNextObjective(levels.get(currentLevel).getFirstObjective());
        } else {
            stop();
        }
    }

    private DrawObject getProfile(Context context) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), UserFile.getInstance(context).getCurrentUser().getUri());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Matrix matrix = new Matrix();
        if (bitmap == null)
            bitmap = ((BitmapDrawable) ContextCompat.getDrawable(context, R.drawable.prof)).getBitmap();
        else
            matrix.postRotate(90);
        final Bitmap rotatedImg = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return new DrawObject(Bitmap.createScaledBitmap(rotatedImg, 100, 100, true));
    }

    private void attachGravitySensor(Context context) {
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

        sensorManager.registerListener(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                gravity.x = -event.values[0];
                gravity.y = event.values[1];
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        }, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void updateScene() {
        long previousTime = System.currentTimeMillis();
        boolean isPlaying = false;
        while (isRunning) {
            long currentTime = System.currentTimeMillis();
            float changeInTime = (currentTime - previousTime);

            previousTime = currentTime;

            if (isPaused) continue;
            for (int i = 0; i < objects.size(); i++) {
                GameObject object = objects.get(i);
                if (!(object instanceof PhysicsObject)) continue;
                PhysicsObject phys = ((PhysicsObject) object);
                phys.update(pixelsToMetres * changeInTime / 1000f);
                for (int j = 0; j < objects.size(); j++) {
                    GameObject otherObject = objects.get(j);
                    if (j == i || !(otherObject instanceof PhysicsObject)) continue;
                    boolean hit = phys.checkCollision((PhysicsObject) otherObject, pixelsToMetres * changeInTime / 1000f);
                    //TODO reenable this when collision works properly
//                    if (hit && !isPlaying) {
//                        soundPool.play(collisionSound, 0.2f, 0.2f, 1, 0, 1);
//                        isPlaying = true;
//                    } else {
//                        isPlaying = hit;
//                    }
                }

            }
            for (int i = 0; i < removeObjects.size(); i++) {
                objects.remove(removeObjects.get(i));
            }
            removeObjects.clear();
        }
    }

    private void drawScene() {
        Paint white = new Paint();
        white.setColor(Color.WHITE);
        while (isRunning) {
            if (isPaused) continue;
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


    public void stop() {
        isRunning = false;
    }

    public void pause() {
        isPaused = true;
    }

    public void play() {
        isPaused = false;
        Log.d("RUN", isPaused + " " + isRunning);
    }
}
