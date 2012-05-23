/**
 * Test
 */

package de.htw;

import java.io.Serializable;

import de.uni_trier.jane.basetypes.Address;

public class DeviceRouteData implements Serializable{

	private static final long serialVersionUID = -1674617558682609096L;
	
	private Address nextHop;
	private long distanceToDestination;
	private long sequenceNumber;
	
	public DeviceRouteData(Address nextHop, long distanceToDestination,
			long sequenceNumber) {
		super();
		this.nextHop = nextHop;
		this.distanceToDestination = distanceToDestination;
		this.sequenceNumber = sequenceNumber;
	}

	public Address getNextHop() {
		return nextHop;
	}
	public void setNextHop(Address nextHop) {
		this.nextHop = nextHop;
	}
	public long getDistanceToDestination() {
		return distanceToDestination;
	}
	public void setDistanceToDestination(long distanceToDestination) {
		this.distanceToDestination = distanceToDestination;
	}
	public long getSequenceNumber() {
		return sequenceNumber;
	}
	public void setSequenceNumber(long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "DeviceRouteData [nextHop=" + nextHop
				+ ", distanceToDestination=" + distanceToDestination
				+ ", sequenceNumber=" + sequenceNumber + "]";
	}
	
}
