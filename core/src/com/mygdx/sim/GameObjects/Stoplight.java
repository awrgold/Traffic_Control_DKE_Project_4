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
    private List<Edge> lanes;
    private ArrayList<Node> nodeLanes;
    private Stoplight linked;
    private Coordinates location;
    private Node parent;
    private int timestepsRemainingUntilChange;
        /**
         * The stoplight is an array of Enumerable Colors, where position N (# of lanes) is the left-most lane, and position 0 is the right most lane.
         * @param lanes: the number of lanes on the current road
         * @param coords: the location of the stoplight in 2D Euclidean space
         */

    public Stoplight(List<Edge> lanes, Coordinates coords){
        this.lanes = lanes;
        this.location = coords;
    }

    public Stoplight(Coordinates coords, ArrayList<Node> nodeLanes){
        this.nodeLanes = nodeLanes;
        this.location = coords;
    }

    public Stoplight(){}

    public void addLanes(int index, Edge toAdd){
        if (index >= 0){
            lanes.add(index, toAdd);
        }
        else{
            lanes.add(toAdd);
        }
    }

    public void setParent(Node parent){
        this.parent = parent;
    }

    public Node getParent(){
        return parent;
    }

    public void addLanes(List<Edge> toAdd){
        lanes.addAll(toAdd);
    }

    public List<Edge> getLanes(){
        return lanes;
    }

    public ArrayList<Node> getNodeLanes(){ return nodeLanes; }

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

