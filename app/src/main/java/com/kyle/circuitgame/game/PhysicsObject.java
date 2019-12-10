package com.kyle.circuitgame.game;

class PhysicsObject extends GameObject {
    private static final float SCALE = 0.000001f;

    private Vector2D gravity = Vector2D.ZERO.clone();
    private Vector2D velocity = Vector2D.ZERO.clone();
    private float friction;
    private float bounciness;
    private boolean kinematic = false;
    private boolean isTrigger = false;

    PhysicsObject(DrawObject drawObject, Vector2D position, float friction, float bounciness, Vector2D velocity) {
        super(drawObject, position);
        this.friction = friction / 100 + 1;
        this.bounciness = bounciness;
        this.velocity = velocity.clone();
    }

    PhysicsObject(DrawObject drawObject, Vector2D position, float friction, float bounciness) {
        super(drawObject, position);
        this.friction = friction / 100 + 1;
        this.bounciness = bounciness;
    }

    PhysicsObject(DrawObject drawObject, Vector2D position, boolean isTrigger) {
        super(drawObject, position);
        kinematic = true;
        this.isTrigger = isTrigger;
    }

    void setGravity(Vector2D gravity) {
        this.gravity = gravity;
    }

    void update(float changeInTime) {
        if (kinematic) return;
        velocity.translate(gravity.multiply(changeInTime));
        position.translate(velocity.multiply(changeInTime * SCALE));
    }


    private void onCollide(PhysicsObject other, float changeInTime) {
        //TODO use rect for better collision resolution
        if (kinematic) return;
        position.translate(new Vector2D(0, -velocity.y * SCALE * changeInTime));
        if (!getRect().intersect(other.getRect())) {
            velocity.y *= -bounciness;
            velocity.x /= friction;
            return;
        }

        position.translate(new Vector2D(-velocity.x * SCALE * changeInTime, velocity.y * SCALE * changeInTime));
        velocity.x *= -bounciness;
        if (!getRect().intersect(other.getRect())) {
            velocity.y /= friction;
            return;
        }
        position.translate(new Vector2D(0, -velocity.y * SCALE * changeInTime));
        velocity.y *= -bounciness;
    }

    boolean checkCollision(PhysicsObject otherObject, float changeInTime) {
        if (kinematic || !getRect().intersect(otherObject.getRect())) return false;
        if (!otherObject.isTrigger) {
            onCollide(otherObject, changeInTime);
            return true;
        }
        otherObject.trigger(this, changeInTime);
        return false;
    }

    protected void trigger(PhysicsObject other, float changeInTime) {
        //
    }

    @Override
    public String toString() {
        return "Pos: " + position + "\nVel: " + velocity + "\nGrav: " + gravity;
    }

    float getSpeed() {
        return velocity.length();
    }
}
