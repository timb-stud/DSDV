package de.htw;

import java.util.HashMap;

import de.uni_trier.jane.basetypes.Address;

public class RoutingTable {
	private AddressMap map;
	
	public RoutingTable() {
		map = new AddressMap();
	}
	
	public void put(Address destination, Address nextHop, long distanceToDestination, long sequenceNumber){
		DeviceRouteData drd = new DeviceRouteData(nextHop, distanceToDestination, sequenceNumber);
		map.put(destination, drd);
	}
	
	public void incSeqNum(Address device, long step){
		DeviceRouteData drd = map.get(device);
		long seqNum = drd.getSequenceNumber();
		drd.setSequenceNumber(seqNum + step);
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
			if(dtd == -1)
				continue;
			drd.setDistanceToDestination(dtd + 1);
		}
	}
	
	public void setDistanceToDestinationToInfinity(Address deviceAddress){
		DeviceRouteData drd = map.get(deviceAddress);
		drd.setDistanceToDestination(-1);
	}
	
	public void updateMapWithRemoveInformations(Address deviceAddress){
		for (Address a : map.keySet()){
			DeviceRouteData drd = map.get(a);
			
			if (drd.getNextHop() == null)
				continue;
			
			if (drd.getNextHop().toString().equals(deviceAddress.toString())){
				drd.setDistanceToDestination(-1);
				drd.setNextHop(null);
				incSeqNum(a, 1);
			}
		}
	}
	
	public Address getNextHop(Address deviceId){
		return map.get(deviceId).getNextHop();
	}
	
	public int getHopCount(Address destination) {
		return (int) map.get(destination).getDistanceToDestination();
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
				this.incSeqNum(deviceId, 2);
				otherMap.put(deviceId, map.get(deviceId));
				return false;
			}
		}
		return true;
	}
	
	public boolean merge(HashMap<Address, DeviceRouteData> otherMap, Address deviceId){
		boolean hasChanged = false;
		for(Address a: otherMap.keySet()){
			if(a.toString().equals(deviceId.toString()))
				continue;
			
			DeviceRouteData otherDrd = otherMap.get(a);
			if(map.containsKey(a)){
				DeviceRouteData ownDrd = map.get(a);
				if(ownDrd.getSequenceNumber() < otherDrd.getSequenceNumber() 
						|| (ownDrd.getSequenceNumber() == otherDrd.getSequenceNumber()
						&& ownDrd.getDistanceToDestination() > otherDrd.getDistanceToDestination())){
					System.out.println("put: " + otherDrd);
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
		StringBuilder sb = new StringBuilder();
		sb.append("| Destination | NexthHop | distToDest | seqNum |\n");
		sb.append("|-------------|----------|------------|--------|\n");
		for(Address a: map.keySet()){
			DeviceRouteData drd = map.get(a);
			sb.append("|");
			sb.append(String.format("%7s      ", a));
			sb.append("|");
			sb.append(String.format("%6s    ", drd.getNextHop()));
			sb.append("|");
			sb.append(String.format("%7s     ", drd.getDistanceToDestination()));
			sb.append("|");
			sb.append(String.format("%5s   ", drd.getSequenceNumber()));
			sb.append("|\n");
		}
		return sb.toString();
	}
}
