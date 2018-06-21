package com.mygdx.sim.GameObjects.Controllers;


import com.mygdx.sim.GameObjects.Stoplight;
import java.util.List;

public class LightController {

    private int interval;
    private List<Stoplight> lights;
    private ControlScheme controller;

    public LightController(List<Stoplight> lights, int interval){
        this.interval = interval;
    }

    public void setInterval(int interval){
        this.interval = interval;
    }

    public int getInterval(){
        return interval;
    }

    public List<Stoplight> getLights(){
        return lights;
    }

    public void setLights(List<Stoplight> lights){
        this.lights = lights;
    }

    public int getTimeRemaining(int currentTimestep){
        return interval - (currentTimestep%interval);
    }

    public void switchLights(){
        for (Stoplight l : lights){
            l.switchLight();
        }
    }


}