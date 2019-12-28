package com.kyle.circuitgame.game;

import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

class Level {
    private List<GameObject> mObjects;
    private Objective mHeadObjective;
    private Objective mCurrentObjective;
    private Paint mWallPaint;
    private ObjectiveEvent mEvent;
    private Vector2D mGravity;

    Level(int wallColor, ObjectiveEvent event, Vector2D gravity) {
        mObjects = new ArrayList<>();
        mWallPaint = new Paint();
        mWallPaint.setColor(wallColor);
        mEvent = event;
        mGravity = gravity;
    }

    void addObject(GameObject object) {
        mObjects.add(object);
    }

    void addPhysicsObject(PhysicsObject object) {
        object.setGravity(mGravity);
        addObject(object);
    }

    void addWall(Vector2D position, float width, float height) {
        GameObject object = new PhysicsObject(new DrawObject(mWallPaint, width, height), position, false);
        mObjects.add(object);
    }

    void addObjective(String name, DrawObject drawObject, Vector2D position) {
        Objective object = new Objective(name, drawObject, position, mEvent);

        if (mHeadObjective == null) mHeadObjective = object;
        if (mCurrentObjective != null) mCurrentObjective.setNextObjective(object);
        mCurrentObjective = object;
        addObject(object);
    }

    Objective getFirstObjective() {
        return mHeadObjective;
    }

    void loadLevel(List<GameObject> objects) {
        objects.clear();
        objects.addAll(mObjects);
    }

    interface ObjectiveEvent {
        void trigger(Objective objective);
    }
}
