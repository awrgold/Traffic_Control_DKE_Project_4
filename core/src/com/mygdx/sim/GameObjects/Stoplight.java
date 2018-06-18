package com.mygdx.sim.GameObjects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.sim.GameObjects.data.Coordinates;
import com.mygdx.sim.GameObjects.data.Edge;
import com.mygdx.sim.GameObjects.data.Node;

import java.util.ArrayList;
import java.util.List;

public class Stoplight {

    private int timer;
    private Sprite sprite;
    private Color color;
    private Edge lane;
    private Node parent;
    private Stoplight linked;
    private Coordinates location;
    private int timestepsRemainingUntilChange;
    private int greenTimer = 30;
    private int yellowTimer = 10;
    private int currentTimeStep;

        /**
         * The stoplight is an array of Enumerable Colors, where position N (# of lanes) is the left-most lane, and position 0 is the right most lane.
         * @param coords: the location of the stoplight in 2D Euclidean space
         */

    public Stoplight(Edge lane, Coordinates coords){
        this.lane = lane;
        this.parent = lane.getTo();
        this.location = coords;
        this.color = Color.GREEN;
    }

    public Stoplight(){}

    public void incrementTimeStep(){
        currentTimeStep++;
        updateTimeStepsRemainingUntilChange();
    }

    public void setParent(Node parent){
        this.parent = parent;
    }

    public Node getParent(){
        return parent;
    }

    public Edge getLane(){
        return lane;
    }

    public int getTimestepsRemainingUntilChange() {
        return timestepsRemainingUntilChange;
    }

    public void setTimestepsRemainingUntilChange(int timestepsRemainingUntilChange) {
        this.timestepsRemainingUntilChange = timestepsRemainingUntilChange;
    }

    public void updateTimeStepsRemainingUntilChange(){
        if(this.getColor().equals(Color.GREEN) && timestepsRemainingUntilChange > 0){
            timestepsRemainingUntilChange--;
        }
        if(timestepsRemainingUntilChange == 0){
            switchLight();
            // TODO: Change sprite so we can see the lights switching
            if (this.getColor().equals(Color.YELLOW)) timestepsRemainingUntilChange = yellowTimer;
            if (this.getColor().equals(Color.GREEN)) timestepsRemainingUntilChange = greenTimer;
        }
    }

    public void setLinked(Stoplight neighbor){
        this.linked = neighbor;
    }

    public Stoplight getLinked(){
        return linked;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void switchLight(){
        switch (color) {
            case RED: setColor(Color.GREEN); break;
            case GREEN: setColor(Color.YELLOW);
            case YELLOW: setColor(Color.RED);
        }
    }

}

