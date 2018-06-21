package com.mygdx.sim.GameObjects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.sim.GameObjects.data.Coordinates;
import com.mygdx.sim.GameObjects.data.Edge;
import com.mygdx.sim.GameObjects.data.Node;
import com.mygdx.sim.GameObjects.trafficObject.InvisibleCar;
import com.mygdx.sim.GameObjects.trafficObject.TrafficObject;
import com.mygdx.sim.GameObjects.trafficObject.TrafficObjectState;
import com.mygdx.sim.GameObjects.trafficObject.vehicle.Car;
import com.mygdx.sim.GameObjects.trafficObject.vehicle.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class Stoplight {

    private int timer;
    private Sprite sprite;
    private Color color;
    private Edge lane;
    private Node parent;
    private ArrayList<Stoplight> linked;
    private Coordinates location;
    private InvisibleCar v;
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

    public void setParent(Node parent){
        this.parent = parent;
    }

    public Node getParent(){
        return parent;
    }

    public Edge getLane(){
        return lane;
    }

    public void setLinked(ArrayList<Stoplight> neighbors){
        this.linked = neighbors;
    }

    public void addLinked(Stoplight neighbor){
        linked.add(neighbor);
    }

    public void addLinkedAt(int index, Stoplight neighbor){
        linked.add(index, neighbor);
    }

    public ArrayList<Stoplight> getLinked(){
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

    /**
     * This only controls the single stoplight.
     * Traffic controller must switch lights for all linked.
     * Use forEach in getLinked() to switch lights
     */
    public void switchLight(){
        switch (color) {
            case RED: setColor(Color.GREEN); break;
            case GREEN: setColor(Color.RED);
        }
        //TODO Place invisible cars
        if(getColor().equals(Color.RED)){
            v = new InvisibleCar(getParent());
        }
        if(getColor().equals(Color.GREEN)){
            v = null;
        }
    }

}

