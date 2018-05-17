package com.mygdx.sim.GameObjects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.sim.GameObjects.data.Coordinates;
import com.mygdx.sim.GameObjects.data.Node;

public class Stoplight {

    private int timer;
    private Sprite sprite;
    private Coordinates location;

    public Stoplight(int timer){
        this.timer = timer;

    }
    public Stoplight(Node node){
        this.location = node.getLocation();
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

}
