package com.mygdx.sim.GameObjects;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class Stoplight {

    private int timer;
    private Sprite sprite;

    public Stoplight(int timer){
        this.timer = timer;
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

}
