package com.mygdx.sim.GameObjects;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class Car implements Vehicle {

    private Sprite sprite;
    private double weight;
    private double timeLimit;
    private Node from;
    private Node to;
    private double hasteValue;


    public Car(Sprite sprite, double timeLimit, Node from, Node to){
        this.sprite = sprite;
        this.timeLimit = timeLimit;
        this.from = from;
        this.to = to;

        // depending on type of car, assign weight
        if (sprite.toString().equals("truck.png")){
            this.weight = 1.0;
        }
        if (sprite.toString().equals("car.png")){
            this.weight = 0.25;
        }
        if (sprite.toString().equals("Black_viper.png")){
            this.weight = 0.5;
        }

        setHasteValue(timeLimit, from, to);
    }


    // This function can be used as a multiplier to the car's speed so that he is willing to go X percent faster than the speed limit. Not 100% necessary.
    public void setHasteValue(double timeLimit, Node from, Node to){
        double eDistance = Math.abs(Math.sqrt((to.getX() - from.getX()) + to.getY() - from.getY()));
        this.hasteValue = eDistance/timeLimit;
    }


}
