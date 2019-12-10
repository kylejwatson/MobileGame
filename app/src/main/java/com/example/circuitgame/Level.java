package com.example.circuitgame;

import android.graphics.Paint;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Level {

    public interface ObjectiveEvent{
        void trigger(Objective objective);
    }
    private List<GameObject> objects;
    private Objective headObjective;
    private Objective currentObjective;
    private Paint wallPaint;
    private ObjectiveEvent event;
    private Vector2D gravity;

    public Level(int wallColor, ObjectiveEvent event, Vector2D gravity) {
        objects = new ArrayList<>();
        wallPaint = new Paint();
        wallPaint.setColor(wallColor);
        this.event = event;
        this.gravity = gravity;
    }

    public void addObject(GameObject object) {
        objects.add(object);
    }

    public void addPhysicsObject(PhysicsObject object) {
        object.setGravity(gravity);
        addObject(object);
    }

    public void addWall(Vector2D position, float width, float height){
        GameObject object = new PhysicsObject(new DrawObject(wallPaint, width, height), position, false);
        objects.add(object);
    }

    public void addObjective(String name, DrawObject drawObject, Vector2D position){
        Objective object = new Objective(name, drawObject, position, event);

        if (headObjective == null) headObjective = object;
        if (currentObjective != null) currentObjective.setNextObjective(object);
        currentObjective = object;
        addObject(object);
    }

    public Objective getFirstObjective(){
        return headObjective;
    }

    public void loadLevel(List<GameObject> objects){
        objects.clear();
        objects.addAll(this.objects);
    }
}
