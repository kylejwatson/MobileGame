package com.example.circuitgame;

import android.graphics.drawable.Drawable;
import android.util.Log;

public class PhysicsObject extends GameObject {
    private static final float SCALE = 0.000001f;

    public Vector2D gravity = new Vector2D(0,0);
    private Vector2D velocity = new Vector2D(0, 0);
    private float friction;
    private float bounciness;
    private boolean kinematic = false;
    private boolean isTrigger = false;

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
    }

    public PhysicsObject(Drawable image, Vector2D position, boolean isTrigger){
        super(image, position);
        kinematic = true;
        this.isTrigger = isTrigger;
    }

    public void update(float changeInTime){
        if (kinematic) return;
        velocity.translate(gravity.multiply(changeInTime));
        position.translate(velocity.multiply(changeInTime * SCALE));
    }

    private float getRight(){
        return position.x + getWidth();
    }

    private float getBottom(){
        return position.y + getHeight();
    }

    private void onCollide(PhysicsObject other, float changeInTime){
        if (kinematic) return;
        position.translate(new Vector2D(0, -velocity.y * SCALE * changeInTime));
        if (!collides(other)) {
            velocity.y *= -bounciness;
            velocity.x /= friction;
            return;
        }

        position.translate(new Vector2D(-velocity.x * SCALE * changeInTime, velocity.y * SCALE * changeInTime));
        velocity.x *= -bounciness;
        if (!collides(other)) {
            velocity.y /= friction;
            return;
        }
        position.translate(new Vector2D(0, -velocity.y * SCALE * changeInTime));
        velocity.y *= -bounciness;
    }

    private boolean collides(PhysicsObject otherObject){
        boolean leftOverlap = position.x > otherObject.position.x && position.x < otherObject.getRight();
        boolean rightOverlap = getRight() > otherObject.position.x && getRight() < otherObject.getRight();
        boolean topOverlap = position.y > otherObject.position.y && position.y < otherObject.getBottom();
        boolean bottomOverlap = getBottom() > otherObject.position.y && getBottom() < otherObject.getBottom();
        return (leftOverlap || rightOverlap) && (topOverlap || bottomOverlap);
    }

    public void checkCollision(PhysicsObject otherObject, float changeInTime) {
        if (collides(otherObject)){
            if (otherObject.isTrigger) otherObject.trigger(this, changeInTime);
            else onCollide(otherObject, changeInTime);
        }
    }

    protected void trigger(PhysicsObject other, float changeInTime){
        //
    }
}
