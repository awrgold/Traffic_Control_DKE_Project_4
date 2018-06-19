package com.mygdx.sim.GameObjects;

import java.util.ArrayList;

import com.mygdx.sim.GameObjects.data.Edge;
import com.mygdx.sim.GameObjects.data.Node;
import com.mygdx.sim.GameObjects.pathfinding.Pathfinder;

public class Intersection {

    private ArrayList<Node> points;
    private ArrayList<Stoplight> stoplights;
    private ArrayList<ArrayList<Edge>> roads;

    public Intersection(){
        points = new ArrayList<Node>();
        stoplights = new ArrayList<Stoplight>();
        roads = new ArrayList<ArrayList<Edge>>();
    }

    public void addLight(Stoplight toAdd){
        if (stoplights.size() < 4){
            stoplights.add(toAdd);
        }
    }

    public void addRoad(ArrayList<Edge> road){
        roads.add(road);
    }

    public ArrayList<ArrayList<Edge>> getRoads() {
        return roads;
    }

    public void setRoads(ArrayList<ArrayList<Edge>> roads){
        this.roads = roads;
    }

    public ArrayList<Node> getPoints(){
        return points;
    }

    public void initializeLinked(){
        for (ArrayList<Edge> edges : getRoads()){
            ArrayList<Stoplight> toSet = new ArrayList<Stoplight>();
            for (Edge e : edges){
                toSet.add(e.getTo().getLight());
            }
            for (Stoplight s : toSet){
                for (Stoplight t : toSet){
                    if (!s.equals(t)){
                        s.addLinked(t);
                    }
                }
            }
        }
    }

    public ArrayList<Stoplight> getStoplights(){
        return stoplights;
    }

    public void addPoint(Node toAdd){
        points.add(toAdd);
    }



}
