package com.mygdx.sim.GameObjects.Controllers;

import java.util.ArrayList;
import java.util.List;

import com.mygdx.sim.GameObjects.Stoplight;
import com.mygdx.sim.GameObjects.data.Map;

public class DynamicController {

    public List<LightController> controllers = new ArrayList<LightController>();
    public Map map;

    public DynamicController(List<LightController> controllers, Map map){
        this.controllers = controllers;
        this.map = map;
    }

    public List<LightController> getControllers() {
        return controllers;
    }

    public void setControllers(List<LightController> controllers) {
        this.controllers = controllers;
    }

    public void updateTimers(){
        int a = 0;
        int b = 0;
        for (LightController lc : controllers){
            for (Stoplight l : lc.getLights()){

            }
        }
    }
}
