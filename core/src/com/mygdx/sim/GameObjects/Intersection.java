package com.mygdx.sim.GameObjects;

import java.util.ArrayList;

public class Intersection {

    private ArrayList<Node> points;
    private ArrayList<Stoplight> stoplights;

    public Intersection(){

    }

    public void addLight(Stoplight toAdd){
        if (stoplights.size() < 4){
            stoplights.add(toAdd);
        }
    }

    public ArrayList<Stoplight> getStoplights(){
        return stoplights;
    }

    public void addPoint(Node toAdd){
        points.add(toAdd);
    }



}
