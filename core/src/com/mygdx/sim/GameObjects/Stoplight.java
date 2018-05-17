package com.mygdx.sim.GameObjects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.sim.GameObjects.data.Coordinates;
import com.mygdx.sim.GameObjects.data.Node;

import java.util.ArrayList;

public class Stoplight {

    private int timer;
    private Sprite sprite;
    private Color color;
    private ArrayList<Node> lanes;
    private Stoplight linked;
    private Coordinates location;
    private int timestepsRemainingUntilChange;
        /**
         * The stoplight is an array of Enumerable Colors, where position N (# of lanes) is the left-most lane, and position 0 is the right most lane.
         * @param lanes: the number of lanes on the current road
         * @param coords: the location of the stoplight in 2D Euclidean space
         */

    public Stoplight(ArrayList<Node> lanes, Coordinates coords){
            this.lanes = lanes;
            this.location = coords;
        }

    public Stoplight(){}

    public void addLane(int index, Node toAdd){
        if (index >= 0){
            lanes.add(index, toAdd);
        }
        else{
            lanes.add(toAdd);
        }
    }

    public ArrayList<Node> getLanes(){
        return lanes;
    }

//    public Node getLane(Node n){
//        for (Node m : getLanes()){
//            if (m.equals(n)){
//                return m;
//            }
//        }
//        return null;
//    }

    public int getTimestepsRemainingUntilChange() {
        return timestepsRemainingUntilChange;
    }

    public void setTimestepsRemainingUntilChange(int timestepsRemainingUntilChange) {
        this.timestepsRemainingUntilChange = timestepsRemainingUntilChange;
    }

    public void updateTimeStepsRemainingUntilChange(){
        if(timestepsRemainingUntilChange > 0){
            timestepsRemainingUntilChange--;
        }
        if(timestepsRemainingUntilChange == 0){
            switchLight();
        }
    }

    public void setLinked(Stoplight neighbor){
        this.linked = neighbor;
    }

    public Stoplight getLinked(){
        return linked;
    }

    public int getNumLanes() {
        return lanes.size();
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

