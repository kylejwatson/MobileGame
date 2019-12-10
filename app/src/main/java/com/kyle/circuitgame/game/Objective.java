package com.kyle.circuitgame.game;

public class Objective extends PhysicsObject{
    private String name;
    private Level.ObjectiveEvent event;
    private Objective nextObjective;
    Objective(String name, DrawObject drawObject, Vector2D position, Level.ObjectiveEvent event) {
        super(drawObject, position, true);
        this.name = name;
        this.event = event;
    }

    void setNextObjective(Objective nextObjective) {
        this.nextObjective = nextObjective;
    }

    public Objective getNextObjective() {
        return nextObjective;
    }

    @Override
    protected void trigger(PhysicsObject other, float changeInTime){
        event.trigger(this);
    }

    public String getName() {
        return name;
    }
}