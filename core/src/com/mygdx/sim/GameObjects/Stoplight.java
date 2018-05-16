package com.mygdx.sim.GameObjects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.sim.GameObjects.data.Coordinates;
import com.mygdx.sim.GameObjects.data.Edge;
import com.mygdx.sim.GameObjects.data.Node;
import com.mygdx.sim.GameObjects.pathfinding.Pathfinder;

import java.util.ArrayList;

public class Stoplight {

    private ArrayList<Node> lanes;
    private Sprite sprite;
    private Color color;
    private int[] timer;
    private Stoplight linked;
    private Coordinates location;

    /**
     * The stoplight is an array of Enumerable Colors, where position N (# of lanes) is the left-most lane, and position 0 is the right most lane.
     * @param lanes: the number of lanes on the current road
     * @param coords: the location of the stoplight in 2D Euclidean space
     */

    public Stoplight(ArrayList<Node> lanes, Coordinates coords){
        this.lanes = lanes;
        this.location = coords;
        timer = new int[lanes.size()];
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
//        Node temp = new Node();
//        for (Node m : getLanes()){
//            if (m.equals(n)){
//                temp = m;
//            }
//        }
//        return temp;
//    }

    public void setLinked(Stoplight neighbor){
        this.linked = neighbor;
    }

    public Stoplight getLinked(){
        return linked;
    }

    public int getNumLanes() {
        return lanes.size();
    }

    public void setTimer(int[] timer) {
        this.timer = timer;
    }

    public int[] getTimer(){
        return timer;
    }

//    public void synchronize(){
//        setLinked(this);
//    }

    public Sprite getSprite() {
        return sprite;
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

}
