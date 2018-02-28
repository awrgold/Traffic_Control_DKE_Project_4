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
	 * @param timeLimit
	 * @param from
	 * @param to
	 * @return
	 */
	public double setHasteValue(double timeLimit, Node from, Node to) {
		double eDistance = Math.abs(Math.sqrt((to.getX() - from.getX()) + to.getY() - from.getY()));
		return eDistance / timeLimit;
	}

}
