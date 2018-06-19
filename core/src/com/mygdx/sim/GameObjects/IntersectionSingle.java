package com.mygdx.sim.GameObjects;

import java.util.ArrayList;
import java.util.List;

import com.mygdx.sim.GameObjects.data.Coordinates;
import com.mygdx.sim.GameObjects.data.Edge;
import com.mygdx.sim.GameObjects.data.Map;
import com.mygdx.sim.GameObjects.data.Node;

public class IntersectionSingle {

    private ArrayList<Node> innerNodes;
    private ArrayList<Node> outerNodes;
    private Node centerNode;
    private ArrayList<Edge> edges;
    private ArrayList<Stoplight> stoplights;

    public void constructIntersection(Node centerNode, boolean hasBeenChecked[][], Map map) {

        List<IntersectionSingle> intersections = map.getIntersectionObjects();

        IntersectionSingle intersectionOne = null;
        IntersectionSingle intersectionTwo = null;

        // Check if we have not already generated the intersection
        for (IntersectionSingle intersection : intersections) {
            if (intersection.getCenterNode().equals(centerNode)) {
                intersectionOne = intersection;
                break;
            }
        }

        if (intersectionOne == null) {
            intersectionOne = new IntersectionSingle(centerNode);
        }

        List<Node> nodes = map.getNodes();
        // Lets start with incoming neighbours
        for (Node neighbourNode : centerNode.getIncomingNeighbors()) {

            // Check if we have not already generated the intersection
            for (IntersectionSingle intersection : intersections) {
                if (intersection.getCenterNode().equals(centerNode)) {
                    intersectionOne = intersection;
                    break;
                }
            }

            if (intersectionOne == null) {
                intersectionOne = new IntersectionSingle(centerNode);
            }

            Node outerNode1 = null;
            Node outerNode2 = null;
            Node outerNode3 = null;
            Node outerNode4 = null;

            if (!hasBeenChecked[nodes.indexOf(neighbourNode)][nodes.indexOf(centerNode)]
                    && !hasBeenChecked[nodes.indexOf(centerNode)][nodes.indexOf(neighbourNode)]) {

                // Node 1
                float distanceX = neighbourNode.getX() - centerNode.getX();
                float distanceY = neighbourNode.getY() - centerNode.getY();

                int offset = (int) (map.euclideanDistance(neighbourNode, centerNode) / 10);
                int offsetX = 0;
                int offsetY = 0;

                if (distanceX != 0) {
                    offsetY = (distanceX > 0) ? offset : -offset;
                }

                if (distanceY != 0) {
                    offsetX = (distanceY > 0) ? -offset : offset;
                }

                map.getNodes().add((outerNode1 = new Node(centerNode.getX() + distanceX / 5 + offsetX,
                        centerNode.getY() + distanceY / 5 + offsetY)));

                // Node 2
                distanceX = centerNode.getX() - neighbourNode.getX();
                distanceY = centerNode.getY() - neighbourNode.getY();

                offsetX = 0;
                offsetY = 0;

                if (distanceX != 0) {
                    offsetY = (distanceX > 0) ? -offset : offset;
                }

                if (distanceY != 0) {
                    offsetX = (distanceY > 0) ? offset : -offset;
                }

                map.getNodes().add((outerNode2 = new Node(neighbourNode.getX() + distanceX / 5 + offsetX,
                        neighbourNode.getY() + distanceY / 5 + offsetY)));

                // Node 3
                distanceX = neighbourNode.getX() - centerNode.getX();
                distanceY = neighbourNode.getY() - centerNode.getY();

                offset = (int) (map.euclideanDistance(neighbourNode, centerNode) / 10);
                offsetX = 0;
                offsetY = 0;

                if (distanceX != 0) {
                    offsetY = (distanceX > 0) ? -offset : offset;
                }

                if (distanceY != 0) {
                    offsetX = (distanceY > 0) ? offset : -offset;
                }

                map.getNodes().add((outerNode3 = new Node(centerNode.getX() + distanceX / 5 + offsetX,
                        centerNode.getY() + distanceY / 5 + offsetY)));

                // Node 4
                distanceX = centerNode.getX() - neighbourNode.getX();
                distanceY = centerNode.getY() - neighbourNode.getY();

                offset = (int) (map.euclideanDistance(neighbourNode, centerNode) / 10);
                offsetX = 0;
                offsetY = 0;

                if (distanceX != 0) {
                    offsetY = (distanceX > 0) ? offset : -offset;
                }

                if (distanceY != 0) {
                    offsetX = (distanceY > 0) ? -offset : offset;
                }

                map.getNodes().add((outerNode4 = new Node(neighbourNode.getX() + distanceX / 5 + offsetX,
                        neighbourNode.getY() + distanceY / 5 + offsetY)));

                hasBeenChecked[nodes.indexOf(neighbourNode)][nodes.indexOf(centerNode)] = true;
                hasBeenChecked[nodes.indexOf(centerNode)][nodes.indexOf(neighbourNode)] = true;
            }

            if (outerNode1 != null && outerNode2 != null) {
                map.getEdges().add(new Edge(outerNode2, outerNode1));
                map.getEdges().add(new Edge(outerNode3, outerNode4));
            }

            // Remove edges we don't
            ArrayList<Edge> edgesToRemove = new ArrayList<Edge>();
            for (Edge edge : centerNode.getInEdges()) {
                if ((edge.getFrom() == centerNode && edge.getTo() == neighbourNode)
                        || edge.getFrom().equals(neighbourNode) && edge.getTo().equals(centerNode)) {
                    edgesToRemove.add(edge);
                }
            }

            map.getEdges().removeAll(edgesToRemove);
        }

    }

    public IntersectionSingle(Node centerNode) {
        this.centerNode = centerNode;
    }

//    /**
//     * Intersections are hard-coded for now. They are single lanes. There are two
//     * sets of vertices, inner and outer nodes. Outer nodes only have one in and one
//     * out edge, inner nodes have 3 in and 3 out edges Inner nodes are for making
//     * turns Outer nodes are for entering/exiting the intersection When creating the
//     * map, the outer edges must be looped over such that a "to" node is added to
//     * their outer edge
//     */
//
//    public IntersectionSingle(Coordinates centerpoint) {
//
//        this.centerNode.setLocation(centerpoint);
//
//        for (int i = 0; i < 8; i++) {
//            outerNodes.add(new Node());
//        }
//
//        for (int i = 0; i < 4; i++) {
//            innerNodes.add(new Node());
//        }
//
//        outerNodes.get(0).addInEdge(new Edge(innerNodes.get(0), outerNodes.get(0)));
//        outerNodes.get(0).addOutEdge(new Edge(innerNodes.get(0), null));
//
//        outerNodes.get(1).addInEdge(new Edge(null, innerNodes.get(1)));
//        outerNodes.get(1).addOutEdge(new Edge(outerNodes.get(1), innerNodes.get(0)));
//        ArrayList<Node> lane1 = new ArrayList<Node>();
//        lane1.add(outerNodes.get(1));
//        Stoplight stop1 = new Stoplight(outerNodes.get(0).getLocation(), lane1);
//        addLight(stop1);
//
//        outerNodes.get(2).addInEdge(new Edge(innerNodes.get(1), outerNodes.get(2)));
//        outerNodes.get(2).addOutEdge(new Edge(innerNodes.get(2), null));
//
//        outerNodes.get(3).addInEdge(new Edge(null, innerNodes.get(3)));
//        outerNodes.get(3).addOutEdge(new Edge(outerNodes.get(3), innerNodes.get(1)));
//        ArrayList<Node> lane2 = new ArrayList<Node>();
//        lane2.add(outerNodes.get(3));
//        Stoplight stop2 = new Stoplight(outerNodes.get(3).getLocation(), lane2);
//        addLight(stop2);
//
//        outerNodes.get(4).addInEdge(new Edge(innerNodes.get(2), outerNodes.get(4)));
//        outerNodes.get(4).addOutEdge(new Edge(innerNodes.get(4), null));
//
//        outerNodes.get(5).addInEdge(new Edge(null, innerNodes.get(5)));
//        outerNodes.get(5).addOutEdge(new Edge(outerNodes.get(5), innerNodes.get(2)));
//        ArrayList<Node> lane3 = new ArrayList<Node>();
//        lane3.add(outerNodes.get(0));
//        Stoplight stop3 = new Stoplight(outerNodes.get(5).getLocation(), lane3);
//        addLight(stop3);
//
//        outerNodes.get(6).addInEdge(new Edge(innerNodes.get(3), outerNodes.get(6)));
//        outerNodes.get(6).addOutEdge(new Edge(innerNodes.get(6), null));
//
//        outerNodes.get(7).addInEdge(new Edge(null, innerNodes.get(7)));
//        outerNodes.get(7).addOutEdge(new Edge(outerNodes.get(7), innerNodes.get(3)));
//        ArrayList<Node> lane4 = new ArrayList<Node>();
//        lane4.add(outerNodes.get(0));
//        Stoplight stop4 = new Stoplight(outerNodes.get(7).getLocation(), lane4);
//        addLight(stop4);
//
//        innerNodes.get(0).addInEdge(new Edge(outerNodes.get(1), innerNodes.get(0)));
//        innerNodes.get(0).addInEdge(new Edge(innerNodes.get(1), innerNodes.get(0)));
//        innerNodes.get(0).addInEdge(new Edge(innerNodes.get(2), innerNodes.get(0)));
//        innerNodes.get(0).addOutEdge(new Edge(innerNodes.get(0), outerNodes.get(0)));
//        innerNodes.get(0).addOutEdge(new Edge(innerNodes.get(0), innerNodes.get(2)));
//        innerNodes.get(0).addOutEdge(new Edge(innerNodes.get(0), innerNodes.get(3)));
//
//        innerNodes.get(1).addInEdge(new Edge(outerNodes.get(3), innerNodes.get(1)));
//        innerNodes.get(1).addInEdge(new Edge(innerNodes.get(2), innerNodes.get(1)));
//        innerNodes.get(1).addInEdge(new Edge(innerNodes.get(3), innerNodes.get(1)));
//        innerNodes.get(1).addOutEdge(new Edge(innerNodes.get(1), outerNodes.get(2)));
//        innerNodes.get(1).addOutEdge(new Edge(innerNodes.get(1), innerNodes.get(0)));
//        innerNodes.get(1).addOutEdge(new Edge(innerNodes.get(1), innerNodes.get(3)));
//
//        innerNodes.get(2).addInEdge(new Edge(outerNodes.get(5), innerNodes.get(2)));
//        innerNodes.get(2).addInEdge(new Edge(innerNodes.get(3), innerNodes.get(2)));
//        innerNodes.get(2).addInEdge(new Edge(innerNodes.get(0), innerNodes.get(2)));
//        innerNodes.get(2).addOutEdge(new Edge(innerNodes.get(2), outerNodes.get(4)));
//        innerNodes.get(2).addOutEdge(new Edge(innerNodes.get(2), innerNodes.get(1)));
//        innerNodes.get(2).addOutEdge(new Edge(innerNodes.get(2), innerNodes.get(0)));
//
//        innerNodes.get(3).addInEdge(new Edge(outerNodes.get(7), innerNodes.get(3)));
//        innerNodes.get(3).addInEdge(new Edge(innerNodes.get(0), innerNodes.get(3)));
//        innerNodes.get(3).addInEdge(new Edge(innerNodes.get(1), innerNodes.get(3)));
//        innerNodes.get(3).addOutEdge(new Edge(innerNodes.get(3), outerNodes.get(6)));
//        innerNodes.get(3).addOutEdge(new Edge(innerNodes.get(3), innerNodes.get(2)));
//        innerNodes.get(3).addOutEdge(new Edge(innerNodes.get(3), innerNodes.get(1)));
//
//    }



    private void addLight(Stoplight toAdd) {
        if (stoplights.size() < 4) {
            stoplights.add(toAdd);
        }
    }

    public ArrayList<Stoplight> getAllStoplights() {
        return stoplights;
    }

    public Stoplight getStopLightAt(Edge e) {
        Stoplight temp = new Stoplight();
        for (int i = 0; i < stoplights.size(); i++) {
            if (e.getTo().equals(stoplights.get(i))) {
                temp = stoplights.get(i);
            }
        }
        return temp;
    }

    public void setStoplights(ArrayList<Stoplight> stoplights) {
        if (stoplights.size() <= 4) {
            this.stoplights = stoplights;
        }
    }

    public ArrayList<Node> getInnerNodes() {
        return innerNodes;
    }

    public ArrayList<Node> getOuterNodes() {
        return outerNodes;
    }

    public ArrayList<Edge> getInEdges() {
        ArrayList<Edge> inEdges = new ArrayList<Edge>();
        for (int i = 0; i < outerNodes.size(); i++) {
            if (i % 2 == 1) {
                inEdges.addAll(outerNodes.get(i).getInEdges());
            }
        }
        return inEdges;
    }

    public ArrayList<Edge> getNullOutEdges() {
        ArrayList<Edge> nullOuts = new ArrayList<Edge>();
        for (int i = 0; i < getOuterNodes().size(); i++) {
            for (int j = 0; j < getOuterNodes().get(i).getOutEdges().size(); j++) {
                if (getOuterNodes().get(i).getOutEdges().get(j).getTo() == null) {
                    nullOuts.add(getOuterNodes().get(i).getOutEdges().get(j));
                }
            }
        }
        return nullOuts;
    }

    public ArrayList<Edge> getOutEdges() {
        ArrayList<Edge> outEdges = new ArrayList<Edge>();
        for (int i = 0; i < outerNodes.size(); i++) {
            if (i % 2 == 0) {
                outEdges.addAll(outerNodes.get(i).getInEdges());
            }
        }
        return outEdges;
    }

    public ArrayList<Edge> getNullInEdges() {
        ArrayList<Edge> nullIns = new ArrayList<Edge>();
        for (int i = 0; i < getOuterNodes().size(); i++) {
            for (int j = 0; j < getOuterNodes().get(i).getInEdges().size(); j++) {
                if (getOuterNodes().get(i).getInEdges().get(j).getTo() == null) {
                    nullIns.add(getOuterNodes().get(i).getInEdges().get(j));
                }
            }
        }
        return nullIns;
    }

    public void setEdges(ArrayList<Edge> edges) {
        this.edges = edges;
    }

    public Node getCenterNode() {
        return centerNode;
    }


    public void setCenterPoint(Coordinates centerPoint) {
        this.centerNode.setLocation(centerPoint);
    }

    // Given the edge that a current vehicle is on, return the status of the light
    // at the next intersection
    public Color getCurrentLightStatus(Edge e) {
        return getStopLightAt(e).getColor();
    }

}
