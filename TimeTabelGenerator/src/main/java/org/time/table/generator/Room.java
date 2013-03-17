package org.time.table.generator;

public class Room {

	private Integer floorDistanceValue=null;//value to check distance between rooms used by student groups

	public Room (Integer distanceCount)
	{
		this.floorDistanceValue=distanceCount;
	}
	/**
	 * @return the floorDistanceValue
	 */
	public Integer getFloorDistanceValue() {
		return floorDistanceValue;
	}

	/**
	 * @param floorDistanceValue the floorDistanceValue to set
	 */
	public void setFloorDistanceValue(Integer floorDistanceValue) {
		this.floorDistanceValue = floorDistanceValue;
	}
	
	
}
