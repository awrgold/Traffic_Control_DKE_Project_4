package com.mygdx.sim.GameObjects.Controllers;

import com.mygdx.sim.GameObjects.Stoplight;
import javafx.scene.effect.Light;

import java.util.ArrayList;
import java.util.List;

public class DynamicController {

    public List<LightController> controllers = new ArrayList<LightController>();

    public DynamicController(List<LightController> controllers){
        this.controllers = controllers;

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
//        for (LightController lc : controllers){
//            for (Stoplight l : lc.getLights()){
//                a += l.getParent().getInEdges().get(0).
//            }
//        }
    }
}
