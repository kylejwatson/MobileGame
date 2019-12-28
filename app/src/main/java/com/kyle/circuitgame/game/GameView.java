package com.kyle.circuitgame.game;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.SoundPool;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.core.content.ContextCompat;

import com.kyle.circuitgame.R;
import com.kyle.circuitgame.utils.UserFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameView extends SurfaceView {
    private static GameView sInstance;
    private boolean mIsPaused = false;
    private List<Level> mLevels;
    private SoundPool mSoundPool;
    private float mPixelsToMetres;

    private boolean mIsRunning = true;
    private List<GameObject> mObjects;

    private List<GameObject> mRemoveObjects;
    private Vector2D mGravity;
    private int mCurrentLevel = 0;

    public GameView(Context context) {
        super(context);
    }

    public static GameView getInstance(final Context context, final ObjectiveListener objectiveListener, SoundPool soundPool) {
        if (sInstance != null) sInstance.stop();
        sInstance = new GameView(context, objectiveListener, soundPool);
        return sInstance;
    }

    public static GameView getInstance() {
        return sInstance;
    }

    private GameView(Context context, ObjectiveListener objectiveListener, SoundPool soundPool) {
        super(context);

        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mPixelsToMetres = metrics.densityDpi * 39.37008f;
        mSoundPool = soundPool;

        mObjects = new ArrayList<>();
        mRemoveObjects = new ArrayList<>();
        mGravity = Vector2D.ZERO.clone();
        attachGravitySensor(context);
        mLevels = new ArrayList<>();
        makeLevels(context, objectiveListener);

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

    private void makeLevels(final Context context, final ObjectiveListener objectiveListener) {
        final int pickupSound = mSoundPool.load(getContext(), R.raw.pickup, 1);
        Vector2D position = new Vector2D(1050, 1400);
        DrawObject profile = getProfile(context);
        Level.ObjectiveEvent event = new Level.ObjectiveEvent() {
            @Override
            public void trigger(final Objective objective) {
                mRemoveObjects.add(objective);
                GameView.this.mSoundPool.play(pickupSound, 0.5f, 0.5f, 1, 0, 1);
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        objectiveListener.objectiveReached(objective);
                    }
                });
                if (objective.getNextObjective() == null) loadLevel(objective);
            }
        };
        Level level1 = makeLevel1(context, event, position, profile);

        mLevels.add(level1);
        mLevels.add(makeLevel2(context, event, position, profile));
        mLevels.add(makeLevel3(context, event, position, profile));

        level1.loadLevel(mObjects);
        objectiveListener.objectiveReached(level1.getFirstObjective());
    }

    private Level makeLevel1(Context context, Level.ObjectiveEvent event, Vector2D position, DrawObject profile) {
        Level level1 = new Level(Color.RED, event, mGravity);
        level1.addWall(Vector2D.ZERO, 10, position.y);
        level1.addWall(Vector2D.ZERO, position.x, 10);
        level1.addWall(new Vector2D(position.x, 0), 10, position.y);
        level1.addWall(new Vector2D(0, position.y), position.x, 10);
        level1.addWall(new Vector2D(position.x / 2, position.y / 2), 10, position.y / 2);
        level1.addWall(new Vector2D(position.x / 2, position.y / 2), position.x / 4, 10);
        level1.addWall(new Vector2D((position.x * 3) / 4, position.y / 2), 10, position.y / 4);
        Drawable battery = ContextCompat.getDrawable(context, R.drawable.battery);
        if (battery != null) {
            level1.addObjective("Cell", new DrawObject(battery),
                    new Vector2D(position.x * 0.2f, position.y * 0.8f));
        }
        Drawable bulb = ContextCompat.getDrawable(context, R.drawable.bulb);
        if (bulb != null) {
            level1.addObjective("Bulb", new DrawObject(bulb),
                    new Vector2D((position.x * 5) / 8, (position.y * 5) / 8));
        }
        PhysicsObject character = new PhysicsObject(profile,
                new Vector2D((position.x * 5) / 8, position.y / 4), 0.5f, 0.5f);
        level1.addPhysicsObject(character);
        return level1;
    }

    private Level makeLevel2(Context context, Level.ObjectiveEvent event, Vector2D position, DrawObject profile) {
        int gap = 150;
        Level level2 = new Level(Color.RED, event, mGravity);
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
        level2.addWall(new Vector2D(position.x / 2 + gap, position.y / 2 + gap / 2f), 10, gap / 2);
        level2.addWall(new Vector2D(gap, position.y / 2), position.x / 2 - gap * 2, 10);

        level2.addWall(new Vector2D(position.x / 2 + gap * 2, position.y / 2 - gap), position.x / 2 - gap * 2, 10);
        level2.addWall(new Vector2D(position.x / 2 + gap * 2, position.y / 2 + gap), position.x / 2 - gap * 2, 10);

        Drawable motor = ContextCompat.getDrawable(context, R.drawable.motor);
        if (motor != null) {
            level2.addObjective("Motor", new DrawObject(motor),
                    position.multiply(0.5f).plus(new Vector2D(-gap * 2, -gap * 2)));
        }
        Drawable switchoff = ContextCompat.getDrawable(context, R.drawable.switchoff);
        if (switchoff != null) {
            level2.addObjective("Switch", new DrawObject(switchoff),
                    position.multiply(0.5f).plus(new Vector2D(gap * 2, gap * 2)));
        }
        Drawable buzzer = ContextCompat.getDrawable(context, R.drawable.buzzer);
        if (buzzer != null) {
            level2.addObjective("Buzzer", new DrawObject(buzzer),
                    position.multiply(0.5f).plus(new Vector2D(-gap * 2, gap * 2)));
        }
        PhysicsObject character = new PhysicsObject(profile,
                position.multiply(0.5f), 0.1f, 0.5f);
        level2.addPhysicsObject(character);
        return level2;
    }

    private Level makeLevel3(Context context, Level.ObjectiveEvent event, Vector2D position, DrawObject profile) {
        int gap = 150;
        Level level3 = new Level(Color.RED, event, mGravity);
        level3.addWall(Vector2D.ZERO, 10, position.y);
        level3.addWall(Vector2D.ZERO, position.x, 10);
        level3.addWall(new Vector2D(position.x, 0), 10, position.y);
        level3.addWall(new Vector2D(0, position.y), position.x, 10);

        level3.addWall(new Vector2D(0, position.y / 2 - 3 * gap), 6 * gap, 10);
        level3.addWall(new Vector2D(0, position.y / 2 + 3 * gap), 6 * gap, 10);
        level3.addWall(new Vector2D(6 * gap, position.y / 2 - 3 * gap), 10, 2.5f * gap);
        level3.addWall(new Vector2D(6 * gap, position.y / 2 + 0.5f * gap), 10, 2.5f * gap);

        level3.addWall(new Vector2D(gap * 5, position.y / 2 - 2 * gap), 10, 4 * gap);
        level3.addWall(new Vector2D(gap, position.y / 2 - 2 * gap), 4 * gap, 10);
        level3.addWall(new Vector2D(gap, position.y / 2 + 2 * gap), 4 * gap, 10);
        level3.addWall(new Vector2D(gap, position.y / 2 - 2 * gap), 10, 1.5f * gap);
        level3.addWall(new Vector2D(gap, position.y / 2 + 0.5f * gap), 10, 1.5f * gap);

        level3.addWall(new Vector2D(gap * 2, position.y / 2 - gap), 10, 2 * gap);
        level3.addWall(new Vector2D(gap * 2, position.y / 2 - gap), 2 * gap, 10);
        level3.addWall(new Vector2D(gap * 2, position.y / 2 + gap), 2 * gap, 10);
        level3.addWall(new Vector2D(gap * 4, position.y / 2 - gap), 10, 0.5f * gap);
        level3.addWall(new Vector2D(gap * 4, position.y / 2 + 0.5f * gap), 10, 0.5f * gap);

        PhysicsObject character = new PhysicsObject(profile,
                new Vector2D(gap * 3, position.y / 2), 0.1f, 0.5f);
        Drawable bulb = ContextCompat.getDrawable(context, R.drawable.bulb);
        if (bulb != null) {
            level3.addObjective("Bulb", new DrawObject(bulb),
                    new Vector2D(gap * 3, position.y - gap));
        }
        Drawable switchoff = ContextCompat.getDrawable(context, R.drawable.switchoff);
        if (switchoff != null) {
            level3.addObjective("Switch", new DrawObject(switchoff),
                    new Vector2D(gap * 3, position.y / 2 + 1.5f * gap - 50));
        }
        Drawable battery = ContextCompat.getDrawable(context, R.drawable.battery);
        if (battery != null) {
            level3.addObjective("Cell", new DrawObject(battery),
                    new Vector2D(gap * 3, position.y / 2 + 2.5f * gap - 50));
        }
        level3.addPhysicsObject(character);
        return level3;
    }

    public boolean isPaused() {
        return mIsPaused;
    }

    private void loadLevel(Objective objective) {
        if (mCurrentLevel >= mLevels.size() - 1) {
            stop();
            return;
        }
        mCurrentLevel++;
        mLevels.get(mCurrentLevel).loadLevel(mObjects);
        objective.setNextObjective(mLevels.get(mCurrentLevel).getFirstObjective());
    }

    private DrawObject getProfile(Context context) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(),
                    UserFile.getInstance(context).getCurrentUser().getUri());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Matrix matrix = new Matrix();
        if (bitmap == null) {
            BitmapDrawable drawable =
                    (BitmapDrawable) ContextCompat.getDrawable(context, R.drawable.prof);
            if (drawable != null) bitmap = drawable.getBitmap();
        } else matrix.postRotate(90);

        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ALPHA_8);
        }

        final Bitmap rotatedImg = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
        return new DrawObject(Bitmap.createScaledBitmap(
                rotatedImg, 100, 100, true));
    }

    private void attachGravitySensor(Context context) {
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager == null) return;
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

        sensorManager.registerListener(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                mGravity.x = -event.values[0];
                mGravity.y = event.values[1];
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        }, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void updateScene() {
        long previousTime = System.currentTimeMillis();
        while (mIsRunning) {
            long currentTime = System.currentTimeMillis();
            float changeInTime = (currentTime - previousTime);
            previousTime = currentTime;

            if (mIsPaused) continue;
            for (int i = 0; i < mObjects.size(); i++) {
                GameObject object = mObjects.get(i);
                if (!(object instanceof PhysicsObject)) continue;
                PhysicsObject phys = ((PhysicsObject) object);
                phys.update(mPixelsToMetres * changeInTime / 1000f);
                for (int j = 0; j < mObjects.size(); j++) {
                    GameObject otherObject = mObjects.get(j);
                    if (j == i || !(otherObject instanceof PhysicsObject)) continue;
                    phys.checkCollision((PhysicsObject) otherObject, mPixelsToMetres * changeInTime / 1000f);
                }
            }
            for (int i = 0; i < mRemoveObjects.size(); i++) mObjects.remove(mRemoveObjects.get(i));
            mRemoveObjects.clear();
        }
    }

    private void drawScene() {
        Paint white = new Paint();
        white.setColor(Color.WHITE);
        SurfaceHolder surfaceHolder = getHolder();
        while (mIsRunning) {
            if (mIsPaused) continue;
            if (!surfaceHolder.getSurface().isValid()) continue;
            Canvas canvas = surfaceHolder.lockCanvas();
            if (canvas == null) continue;
            canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), white);
            for (int i = 0; i < mObjects.size(); i++) mObjects.get(i).draw(canvas);
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void stop() {
        mIsRunning = false;
    }

    public void pause() {
        mIsPaused = true;
    }

    public void play() {
        mIsPaused = false;
    }

    public interface ObjectiveListener {
        void objectiveReached(Objective objective);
    }
}
