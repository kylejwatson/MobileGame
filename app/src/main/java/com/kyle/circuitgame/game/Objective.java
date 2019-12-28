package com.kyle.circuitgame.game;

public class Objective extends PhysicsObject{
    private String mName;
    private Level.ObjectiveEvent mEvent;
    private Objective mNextObjective;

    Objective(String name, DrawObject drawObject, Vector2D position, Level.ObjectiveEvent event) {
        super(drawObject, position, true);
        this.mName = name;
        this.mEvent = event;
    }

    @Override
    protected void trigger(PhysicsObject other, float changeInTime){
        mEvent.trigger(this);
    }

    void setNextObjective(Objective nextObjective) {
        this.mNextObjective = nextObjective;
    }

    public Objective getNextObjective() {
        return mNextObjective;
    }

    public String getName() {
        return mName;
    }
}