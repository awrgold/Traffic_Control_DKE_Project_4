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

    public IntersectionSingle(){

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

        outerNodes.get(2).addInEdge(new Edge(innerNodes.get(1), outerNodes.get(2)));
        outerNodes.get(2).addOutEdge(new Edge(innerNodes.get(2), null));

        outerNodes.get(3).addInEdge(new Edge(null, innerNodes.get(3)));
        outerNodes.get(3).addOutEdge(new Edge(outerNodes.get(3), innerNodes.get(1)));

        outerNodes.get(4).addInEdge(new Edge(innerNodes.get(2), outerNodes.get(4)));
        outerNodes.get(4).addOutEdge(new Edge(innerNodes.get(4), null));

        outerNodes.get(5).addInEdge(new Edge(null, innerNodes.get(5)));
        outerNodes.get(5).addOutEdge(new Edge(outerNodes.get(5), innerNodes.get(2)));

        outerNodes.get(6).addInEdge(new Edge(innerNodes.get(3), outerNodes.get(6)));
        outerNodes.get(6).addOutEdge(new Edge(innerNodes.get(6), null));

        outerNodes.get(7).addInEdge(new Edge(null, innerNodes.get(7)));
        outerNodes.get(7).addOutEdge(new Edge(outerNodes.get(7), innerNodes.get(3)));

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

    public void addLight(Stoplight toAdd){
        if (stoplights.size() < 4){
            stoplights.add(toAdd);
        }
    }

    public ArrayList<Stoplight> getStoplights(){
        return stoplights;
    }

    public void setStoplights(ArrayList<Stoplight> stoplights) {
        this.stoplights = stoplights;
    }

    public ArrayList<Node> getInnerNodes(){
        return innerNodes;
    }

    public ArrayList<Node> getOuterNodes(){
        return outerNodes;
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

    public ArrayList<Edge> getNullInEdges(){
        ArrayList<Edge> nullIns = new ArrayList<Edge>();
        for (int i = 0; i < getOuterNodes().size(); i++){
            for (int j = 0; j < getOuterNodes().get(i).getInEdges().size(); j++){
                if (getOuterNodes().get(i).getOutEdges().get(j).getTo() == null){
                    nullIns.add(getOuterNodes().get(i).getInEdges().get(j));
                }
            }
        }
        return nullIns;





        public ArrayList<Edge> getEdges() {
        return edges;
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

    public void connectIntersections(IntersectionSingle a, IntersectionSingle b){
        for (int i = 0; i < a.getNullOutEdges().size(); i++) {
            for (int j = 0; j < b.getNullInEdges().size(); j++) {
                a.getNullOutEdges().get(i).setTo(b.getNullInEdges().get(j).getTo());
            }
        }

        a.getNullOutEdges().get(1).setTo(b.getNullInEdges().get(2));


    }
}
