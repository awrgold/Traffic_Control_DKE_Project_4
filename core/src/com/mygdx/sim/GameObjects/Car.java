package com.mygdx.sim.GameObjects;

public class Car extends Vehicle {

	Edge locationEdge;
	double distanceTraveledOnEdge;

	public Car(Node startNode, Node goalNode, int maxSpeed) {
		super(startNode, goalNode);

		// Set Max Speed
		this.maxSpeed = maxSpeed;

		// Set Sprite Name
		spriteName = "car";
	}

	/**
	 *  This function can be used as a multiplier to the car's speed so that he is
	 *  willing to go X percent faster than the speed limit. Not 100% necessary.
	 *  Utilizing the Euclidean distance to the target, determine the multiplier at which driver should speed.
	 * @param timeLimit - the fixed amount of time driver has to go from start to finish.
	 * @param from - the starting position that the driver originates at when he "spawns"
	 * @param to - the target or goal that the driver is heading towards.
	 * @return
	 */


//	public double setHasteValue(double timeLimit, Node from, Node to) {
//
//		double eDistance = Math.abs(Math.sqrt((to.getX() - from.getX()) + to.getY() - from.getY()));
//		// double meanDist = total mean distance our cars must travel in the current map to reach their goals
//		// double standardDev = standard deviation of each car's travel distance
//
//		return (timeLimit * (eDistance - meanDist) / standardDev);
//	}


}
