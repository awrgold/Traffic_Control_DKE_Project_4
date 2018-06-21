package com.mygdx.sim.GameObjects.Controllers;


import com.mygdx.sim.GameObjects.Stoplight;
import java.util.List;

import static com.mygdx.sim.GameObjects.Controllers.ControlScheme.BASIC;

public class LightController {

    private int interval;
    private final int FINALINTERVAL = 100;
    private ControlScheme scheme;
    private List<Stoplight> lights;

    public LightController(List<Stoplight> lights){
        this.lights = lights;
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

    public void setScheme(ControlScheme scheme){
        this.scheme = scheme;
        if (scheme.equals(ControlScheme.BASIC)) {
            this.interval = FINALINTERVAL;
        }
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

    public void update(int currentTimestep){
        if (getTimeRemaining(currentTimestep) == 0){
            switchLights();
        }
    }

}