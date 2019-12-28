package com.kyle.circuitgame.game;

class PhysicsObject extends GameObject {
    private static final float SCALE = 0.000001f;
    private Vector2D mGravity = Vector2D.ZERO.clone();
    private Vector2D mVelocity = Vector2D.ZERO.clone();
    private float mFriction;
    private float mBounciness;
    private boolean mKinematic = false;
    private boolean mTrigger = false;

    PhysicsObject(DrawObject drawObject, Vector2D position, float friction, float bounciness) {
        super(drawObject, position);
        mFriction = friction / 100 + 1;
        mBounciness = bounciness;
    }

    PhysicsObject(DrawObject drawObject, Vector2D position, boolean isTrigger) {
        super(drawObject, position);
        mKinematic = true;
        mTrigger = isTrigger;
    }

    void setGravity(Vector2D gravity) {
        mGravity = gravity;
    }

    void update(float changeInTime) {
        if (mKinematic) return;
        mVelocity.translate(mGravity.multiply(changeInTime));
        position.translate(mVelocity.multiply(changeInTime * SCALE));
    }


    private void onCollide(PhysicsObject other, float changeInTime) {
        //TODO use rect for better collision resolution
        if (mKinematic) return;
        position.translate(new Vector2D(0, -mVelocity.y * SCALE * changeInTime));
        if (!getRect().intersect(other.getRect())) {
            mVelocity.y *= -mBounciness;
            mVelocity.x /= mFriction;
            return;
        }

        position.translate(new Vector2D(-mVelocity.x * SCALE * changeInTime, mVelocity.y * SCALE * changeInTime));
        mVelocity.x *= -mBounciness;
        if (!getRect().intersect(other.getRect())) {
            mVelocity.y /= mFriction;
            return;
        }
        position.translate(new Vector2D(0, -mVelocity.y * SCALE * changeInTime));
        mVelocity.y *= -mBounciness;
    }

    boolean checkCollision(PhysicsObject otherObject, float changeInTime) {
        if (mKinematic || !getRect().intersect(otherObject.getRect())) return false;
        if (!otherObject.mTrigger) {
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
        return "Pos: " + position + "\nVel: " + mVelocity + "\nGrav: " + mGravity;
    }

    float getSpeed() {
        return mVelocity.length();
    }
}
