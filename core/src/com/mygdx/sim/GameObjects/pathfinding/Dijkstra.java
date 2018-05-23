package com.mygdx.sim.GameObjects.pathfinding;

import java.util.ArrayList;
import java.util.List;

import com.mygdx.sim.GameObjects.data.Edge;
import com.mygdx.sim.GameObjects.data.Map;
import com.mygdx.sim.GameObjects.data.Node;
import com.mygdx.sim.GameObjects.data.Tuple;
import com.mygdx.sim.GameObjects.vehicle.Vehicle;

import static java.lang.Double.MAX_VALUE;
import static java.lang.Double.MIN_VALUE;

public class Dijkstra extends Pathfinder {


    public Dijkstra (Map graph){
        super(graph);

    }

    public void search(Map map, Node start){

        List<Node> vertexes = map.getNodes();
        List<Tuple> explored = new ArrayList<Tuple>();
        explored.add(new Tuple(start));
        explored.get(0).setDistance(0);


        for (Node n : map.getNodes()){

        }
    }


    public List<Edge> findPath(Vehicle vehicle, int timestep, boolean findDifferentPathOnFail) {
        // TODO Auto-generated method stub

        return vehicle.getEdgePath();
    }
}
