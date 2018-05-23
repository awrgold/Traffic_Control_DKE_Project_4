package com.mygdx.sim.GameObjects.data;

import java.util.Comparator;

public class SortNode implements Comparator<Node> {

    @Override
    public int compare(Node n1, Node n2) {
        return n2.getNodePriorityWeight()-n1.getNodePriorityWeight();
    }
}
