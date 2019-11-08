package com.example.circuitgame;

import android.graphics.drawable.Drawable;

public class PhysicsObject extends GameObject {
    private static final float SCALE = 0.000001f;

    public Vector2D gravity = new Vector2D(0,0);
    private Vector2D velocity;
    private float friction;
    private float bounciness;

    public PhysicsObject(Drawable image, Vector2D position, float friction, float bounciness, Vector2D velocity) {
        super(image, position);
        this.friction = friction/100 + 1;
        this.bounciness = bounciness;
        this.velocity = velocity.clone();
    }

    public PhysicsObject(Drawable image, Vector2D position, float friction, float bounciness) {
        super(image, position);
        this.friction = friction/100 + 1;
        this.bounciness = bounciness;
        velocity = new Vector2D(0, 0);
    }

    public void update(float changeInTime){
        velocity.translate(gravity.multiply(changeInTime));
        position.translate(velocity.multiply(changeInTime * SCALE));

        if (position.y < 0) {
            velocity.y *= -bounciness;
            position.y = 0;
            velocity.x /= friction;
        } else if (position.y > 1800) {
            velocity.y *= -bounciness;
            position.y = 1800;
            velocity.x /= friction;
        }

        if (position.x < 0) {
            velocity.x *= -bounciness;
            position.x = 0;
            velocity.y /= friction;
        } else if (position.x > 1000) {
            velocity.x *= -bounciness;
            position.x = 1000;
            velocity.y /= friction;
        }
    }
}
