package com.mygdx.sim.GameObjects;

public class Edge {

    private Node from;
    private Node to;
    private int speedLimit;
    private double length;

    public Edge(Node from, Node to){
        this.from = from;
        this.to = to;
        this.length = Math.abs(Math.sqrt((to.getX() - from.getX()) + to.getY() - from.getY()));
    }

    public Node getFrom(){
        return from;
    }

    public Node getTo(){
        return to;
    }

    public void setSpeedLimit(int speedLimit){
        this.speedLimit = speedLimit;
    }

    public int getSpeedLimit(){
        return speedLimit;
    }

    public double getLength(){
        return length;
    }






}
