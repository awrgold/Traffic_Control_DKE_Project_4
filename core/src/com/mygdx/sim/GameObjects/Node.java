package com.mygdx.sim.GameObjects;

import java.util.ArrayList;

public class Node {

    // Do we need to differentiate between the two edge types?
    private ArrayList<Edge> inEdges;
    private ArrayList<Edge> outEdges;
    private double x;
    private double y;

    public Node(double xCoordinate, double yCoordinate){
        this.x = xCoordinate;
        this.y = yCoordinate;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public void addInEdge(Edge toAdd){
        inEdges.add(toAdd);
    }

    public void addOutEdge(Edge toAdd){
        outEdges.add(toAdd);
    }

    public ArrayList<Edge> getInEdges(){
        return inEdges;
    }

    public ArrayList<Edge> getOutEdges(){
        return outEdges;
    }



}
