package com.mygdx.sim.GameObjects;

import java.util.ArrayList;

import com.mygdx.sim.GameObjects.data.Coordinates;
import com.mygdx.sim.GameObjects.data.Edge;
import com.mygdx.sim.GameObjects.data.Node;

public class IntersectionSingle {

    private ArrayList<Node> innerNodes;
    private ArrayList<Node> outerNodes;
    private Node centerpoint = new Node();
    private ArrayList<Edge> edges;
    private ArrayList<Stoplight> stoplights;

    /**
     * Intersections are hard-coded for now. They are single lanes.
     * There are two sets of vertices, inner and outer nodes.
     * Outer nodes only have one in and one out edge, inner nodes have 3 in and 3 out edges
     * Inner nodes are for making turns
     * Outer nodes are for entering/exiting the intersection
     * When creating the map, the outer edges must be looped over such that a "to" node is added to their outer edge
     */

    public IntersectionSingle(Coordinates centerpoint){

        this.centerpoint.setLocation(centerpoint);

        for (int i = 0; i < 8; i++) {
            outerNodes.add(new Node());
        }

        for (int i = 0; i < 4; i++) {
            innerNodes.add(new Node());
        }

        outerNodes.get(0).addInEdge(new Edge(innerNodes.get(0), outerNodes.get(0)));
        outerNodes.get(0).addOutEdge(new Edge(innerNodes.get(0), null));

        outerNodes.get(1).addInEdge(new Edge(null, innerNodes.get(1)));
        outerNodes.get(1).addOutEdge(new Edge(outerNodes.get(1), innerNodes.get(0)));
        ArrayList<Node> lane1 = new ArrayList<Node>();
        lane1.add(outerNodes.get(1));
        Stoplight stop1 = new Stoplight(lane1, outerNodes.get(0).getLocation());
        addLight(stop1);

        outerNodes.get(2).addInEdge(new Edge(innerNodes.get(1), outerNodes.get(2)));
        outerNodes.get(2).addOutEdge(new Edge(innerNodes.get(2), null));

        outerNodes.get(3).addInEdge(new Edge(null, innerNodes.get(3)));
        outerNodes.get(3).addOutEdge(new Edge(outerNodes.get(3), innerNodes.get(1)));
        ArrayList<Node> lane2 = new ArrayList<Node>();
        lane2.add(outerNodes.get(3));
        Stoplight stop2 = new Stoplight(lane2, outerNodes.get(3).getLocation());
        addLight(stop2);


        outerNodes.get(4).addInEdge(new Edge(innerNodes.get(2), outerNodes.get(4)));
        outerNodes.get(4).addOutEdge(new Edge(innerNodes.get(4), null));

        outerNodes.get(5).addInEdge(new Edge(null, innerNodes.get(5)));
        outerNodes.get(5).addOutEdge(new Edge(outerNodes.get(5), innerNodes.get(2)));
        ArrayList<Node> lane3 = new ArrayList<Node>();
        lane3.add(outerNodes.get(0));
        Stoplight stop3 = new Stoplight(lane3, outerNodes.get(5).getLocation());
        addLight(stop3);


        outerNodes.get(6).addInEdge(new Edge(innerNodes.get(3), outerNodes.get(6)));
        outerNodes.get(6).addOutEdge(new Edge(innerNodes.get(6), null));

        outerNodes.get(7).addInEdge(new Edge(null, innerNodes.get(7)));
        outerNodes.get(7).addOutEdge(new Edge(outerNodes.get(7), innerNodes.get(3)));
        ArrayList<Node> lane4 = new ArrayList<Node>();
        lane4.add(outerNodes.get(0));
        Stoplight stop4 = new Stoplight(lane4, outerNodes.get(7).getLocation());
        addLight(stop4);


        innerNodes.get(0).addInEdge(new Edge(outerNodes.get(1), innerNodes.get(0)));
        innerNodes.get(0).addInEdge(new Edge(innerNodes.get(1), innerNodes.get(0)));
        innerNodes.get(0).addInEdge(new Edge(innerNodes.get(2), innerNodes.get(0)));
        innerNodes.get(0).addOutEdge(new Edge(innerNodes.get(0), outerNodes.get(0)));
        innerNodes.get(0).addOutEdge(new Edge(innerNodes.get(0), innerNodes.get(2)));
        innerNodes.get(0).addOutEdge(new Edge(innerNodes.get(0), innerNodes.get(3)));

        innerNodes.get(1).addInEdge(new Edge(outerNodes.get(3), innerNodes.get(1)));
        innerNodes.get(1).addInEdge(new Edge(innerNodes.get(2), innerNodes.get(1)));
        innerNodes.get(1).addInEdge(new Edge(innerNodes.get(3), innerNodes.get(1)));
        innerNodes.get(1).addOutEdge(new Edge(innerNodes.get(1), outerNodes.get(2)));
        innerNodes.get(1).addOutEdge(new Edge(innerNodes.get(1), innerNodes.get(0)));
        innerNodes.get(1).addOutEdge(new Edge(innerNodes.get(1), innerNodes.get(3)));

        innerNodes.get(2).addInEdge(new Edge(outerNodes.get(5), innerNodes.get(2)));
        innerNodes.get(2).addInEdge(new Edge(innerNodes.get(3), innerNodes.get(2)));
        innerNodes.get(2).addInEdge(new Edge(innerNodes.get(0), innerNodes.get(2)));
        innerNodes.get(2).addOutEdge(new Edge(innerNodes.get(2), outerNodes.get(4)));
        innerNodes.get(2).addOutEdge(new Edge(innerNodes.get(2), innerNodes.get(1)));
        innerNodes.get(2).addOutEdge(new Edge(innerNodes.get(2), innerNodes.get(0)));

        innerNodes.get(3).addInEdge(new Edge(outerNodes.get(7), innerNodes.get(3)));
        innerNodes.get(3).addInEdge(new Edge(innerNodes.get(0), innerNodes.get(3)));
        innerNodes.get(3).addInEdge(new Edge(innerNodes.get(1), innerNodes.get(3)));
        innerNodes.get(3).addOutEdge(new Edge(innerNodes.get(3), outerNodes.get(6)));
        innerNodes.get(3).addOutEdge(new Edge(innerNodes.get(3), innerNodes.get(2)));
        innerNodes.get(3).addOutEdge(new Edge(innerNodes.get(3), innerNodes.get(1)));

    }


    private void addLight(Stoplight toAdd){
        if (stoplights.size() < 4){
            stoplights.add(toAdd);
        }
    }

    public ArrayList<Stoplight> getAllStoplights(){
        return stoplights;
    }

    public Stoplight getStopLightAt(Edge e){
        Stoplight temp = new Stoplight();
        for (int i = 0; i < stoplights.size(); i++) {
            if (e.getTo().equals(stoplights.get(i))){
                temp = stoplights.get(i);
            }
        }
        return temp;
    }

    public void setStoplights(ArrayList<Stoplight> stoplights) {
        if (stoplights.size() <= 4){
            this.stoplights = stoplights;
        }
    }

    public ArrayList<Node> getInnerNodes(){
        return innerNodes;
    }

    public ArrayList<Node> getOuterNodes(){
        return outerNodes;
    }

    public ArrayList<Edge> getInEdges(){
        ArrayList<Edge> inEdges = new ArrayList<Edge>();
        for (int i = 0; i < outerNodes.size(); i++) {
            if (i % 2 == 1){
                inEdges.addAll(outerNodes.get(i).getInEdges());
            }
        }
        return inEdges;
    }

    public ArrayList<Edge> getNullOutEdges(){
        ArrayList<Edge> nullOuts = new ArrayList<Edge>();
        for (int i = 0; i < getOuterNodes().size(); i++){
            for (int j = 0; j < getOuterNodes().get(i).getOutEdges().size(); j++){
                if (getOuterNodes().get(i).getOutEdges().get(j).getTo() == null){
                    nullOuts.add(getOuterNodes().get(i).getOutEdges().get(j));
                }
            }
        }
        return nullOuts;
    }

    public ArrayList<Edge> getOutEdges(){
        ArrayList<Edge> outEdges = new ArrayList<Edge>();
        for (int i = 0; i < outerNodes.size(); i++) {
            if (i % 2 == 0){
                outEdges.addAll(outerNodes.get(i).getInEdges());
            }
        }
        return outEdges;
    }

    public ArrayList<Edge> getNullInEdges(){
        ArrayList<Edge> nullIns = new ArrayList<Edge>();
        for (int i = 0; i < getOuterNodes().size(); i++){
            for (int j = 0; j < getOuterNodes().get(i).getInEdges().size(); j++){
                if (getOuterNodes().get(i).getInEdges().get(j).getTo() == null){
                    nullIns.add(getOuterNodes().get(i).getInEdges().get(j));
                }
            }
        }
        return nullIns;
    }

    public void setEdges(ArrayList<Edge> edges) {
        this.edges = edges;
    }

    public Node getCenterpoint() {
        return centerpoint;
    }

    public void setCenterpoint(Coordinates centerpoint) {
        this.centerpoint.setLocation(centerpoint);
    }

    // Given the edge that a current vehicle is on, return the status of the light at the next intersection
    public Color getCurrentLightStatus(Edge e){
        return getStopLightAt(e).getColor();
    }

}
