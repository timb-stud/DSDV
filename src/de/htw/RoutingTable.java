package de.htw;

import java.util.HashMap;

import de.uni_trier.jane.basetypes.Address;

public class RoutingTable {
	private HashMap<Address, DeviceRouteData> map;
	
	public RoutingTable() {
		map = new HashMap<Address, DeviceRouteData>();
	}
	
	public void put(Address destination, Address nextHop, long distanceToDestination, long sequenceNumber){
		DeviceRouteData drd = new DeviceRouteData(nextHop, distanceToDestination, sequenceNumber);
		map.put(destination, drd);
	}
	
	public void incSeqNum(Address device){
		DeviceRouteData drd = map.get(device);
		long seqNum = drd.getSequenceNumber();
		drd.setSequenceNumber(seqNum + 2);
	}
	
	public RoutingTable copy(){
		RoutingTable rt = new RoutingTable();
		for(Address a : map.keySet()){
			DeviceRouteData drd = map.get(a);
			rt.put(a, drd.getNextHop(), drd.getDistanceToDestination(), drd.getSequenceNumber());
		}
		return rt;
	}
	
	public void setAllNextHop(Address nextHop){
		for(Address a : map.keySet()){
			DeviceRouteData drd = map.get(a);
			drd.setNextHop(nextHop);
		}
	}
	
	public void incAllDistanceToDestination(){
		for(Address a : map.keySet()){
			DeviceRouteData drd = map.get(a);
			long dtd = drd.getDistanceToDestination();
			drd.setDistanceToDestination(dtd + 1);
		}
	}
	
	public HashMap<Address, DeviceRouteData> getMap() {
		return map;
	}

	@Override
	public String toString() {
		return "RoutingTable [map=" + map + "]";
	}
}
