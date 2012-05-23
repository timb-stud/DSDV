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
	
	public DeviceRouteData getDeviceRouteDate(Address deviceAddress){
		return map.get(deviceAddress);
	}
	
	public boolean isOwnReachabilityCorrect(HashMap<Address, DeviceRouteData> otherMap, Address deviceId){
		if(otherMap.containsKey(deviceId)){
			DeviceRouteData ownDrd = otherMap.get(deviceId);
			if (ownDrd.getDistanceToDestination() == -1){
				otherMap.put(deviceId, map.get(deviceId));
				return true;
			}
		}
		return false;
	}
	
	public boolean merge(HashMap<Address, DeviceRouteData> otherMap){
		boolean hasChanged = false;
		for(Address a: otherMap.keySet()){
			DeviceRouteData otherDrd = otherMap.get(a);
			if(map.containsKey(a)){
				DeviceRouteData ownDrd = map.get(a);
				if(ownDrd.getSequenceNumber() <= otherDrd.getSequenceNumber() 
						|| ownDrd.getSequenceNumber() == otherDrd.getSequenceNumber() 
						&& ownDrd.getDistanceToDestination() > otherDrd.getDistanceToDestination()){
					 map.put(a, otherDrd);
					 hasChanged = true;
				}
			}else{
				map.put(a, otherDrd);
				hasChanged = true;
			}
		}
		return hasChanged;
	}

	@Override
	public String toString() {
		return "RoutingTable [map=" + map + "]";
	}
}