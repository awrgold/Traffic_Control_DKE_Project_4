package com.mygdx.sim.GameObjects.data;

import com.mygdx.sim.GameObjects.TrafficManager;

import java.util.ArrayList;
import java.util.List;

public class DataCollection {

    public List<TrafficManager> managers = new ArrayList<TrafficManager>();

    public DataCollection(){

    }

    public List<TrafficManager> getManagers() {
        return managers;
    }


}
