package com.mygdx.sim.GameObjects.data;

public class Tuple {

    public double distanceToSource;
    public Node node;

    public Tuple(Node node){
        this.node = node;
    }

    public void setDistance(double distance) {
        this.distanceToSource = distance;
    }

    public double getDistance() {
        return distanceToSource;
    }

    public Node getNode() {
        return node;
    }
}
