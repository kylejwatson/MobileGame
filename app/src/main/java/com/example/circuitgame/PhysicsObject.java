package com.example.circuitgame;

import android.graphics.drawable.Drawable;

public class PhysicsObject extends GameObject {

    private static final Vector2D GRAVITY = new Vector2D(0,9.81f);
    private Vector2D velocity;
    private float mass;
    private float area;

    public PhysicsObject(Drawable image, Vector2D position, float mass, float area, Vector2D velocity) {
        super(image, position);
        this.mass = mass;
        this.area = area;
        this.velocity = velocity;
    }

    public void update(){
        velocity.add(GRAVITY);
        position.add(velocity);
    }
}
