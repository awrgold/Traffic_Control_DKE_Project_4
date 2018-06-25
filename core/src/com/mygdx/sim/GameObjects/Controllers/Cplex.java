package com.mygdx.sim.GameObjects.Controllers;
import com.mygdx.sim.GameObjects.data.Edge;
import com.mygdx.sim.GameObjects.data.Map;
import ilog.cplex.*;
import ilog.concert.*;

import java.util.ArrayList;
import java.util.List;


public class Cplex {

    private static Map map;

    public Cplex(Map map) {
        this.map = map;
    }

    public static void cPlexMain(String[] args) {
        if ( args.length < 1 ) {
            System.err.println("Usage: java Transport <type>");
            System.err.println("  type = 0 -> convex  piecewise linear model");
            System.err.println("  type = 1 -> concave piecewise linear model");
            return;
        }

        try {

            List<Edge> edgeList = map.getEdges();
            int numEdges = edgeList.size();
            List<Double> weightList = new ArrayList<Double>();
            List<Double> capacityList = new ArrayList<Double>();

            for(Edge e : edgeList) {
                weightList.add(e.getWeight());
                capacityList.add(e.getCapacity());
            }

            IloCplex cplex = new IloCplex();

            IloNumVar[][] var = new IloNumVar[1][];
            IloRange[][] rng = new IloRange[1][];

            populateModel(cplex, var, rng);

            cplex.end();
        }
        catch (IloException exc) {
            System.out.println(exc);
        }
    }

    static void populateModel(IloMPModeler model,
                         IloNumVar[][] var,
                         IloRange[][] rng) throws IloException {

    }

    public static void main(String[] args) {
        cPlexMain(args);
    }
}
