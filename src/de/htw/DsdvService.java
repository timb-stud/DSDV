package de.htw;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import de.uni_trier.jane.basetypes.Address;
import de.uni_trier.jane.basetypes.ServiceID;
import de.uni_trier.jane.service.EndpointClassID;
import de.uni_trier.jane.service.RuntimeService;
import de.uni_trier.jane.service.neighbor_discovery.NeighborDiscoveryData;
import de.uni_trier.jane.service.neighbor_discovery.NeighborDiscoveryListener;
import de.uni_trier.jane.service.neighbor_discovery.NeighborDiscoveryService;
import de.uni_trier.jane.service.neighbor_discovery.NeighborDiscoveryService_sync;
import de.uni_trier.jane.service.network.link_layer.LinkLayer_async;
import de.uni_trier.jane.service.operatingSystem.RuntimeOperatingSystem;
import de.uni_trier.jane.service.parameter.todo.Parameters;
import de.uni_trier.jane.visualization.shapes.Shape;



public class DsdvService implements RuntimeService, NeighborDiscoveryListener {

	public static ServiceID serviceID;
	private ServiceID linkLayerID;
	private ServiceID neighborID;
	private Address deviceId;
	
	
	private LinkLayer_async linkLayer;
	private NeighborDiscoveryService_sync neighborService;
	private RuntimeOperatingSystem runtimeOperatingSystem;
	
	private RoutingTable table = new RoutingTable();
	
	public DsdvService(ServiceID linkLayerID, ServiceID neighborID) {
		super();
		
		serviceID = new EndpointClassID(DsdvService.class.getName());
		
		this.linkLayerID = linkLayerID;
		this.neighborID = neighborID;
		
		//beacon = new BeaconContent();
	}	
	
	@Override
	public ServiceID getServiceID() {
		return serviceID;
	}

	@Override
	public void finish() {

	}

	@Override
	public Shape getShape() {
		return null;
	}

	@Override
	public void getParameters(Parameters parameters) {
	}



	@Override
	public void setNeighborData(NeighborDiscoveryData neighborData) {
		Address neighborAddress = neighborData.getSender();
		table.put(neighborAddress, neighborAddress, 1, 2); //TODO seqNum really == 2 ???
		table.incSeqNum(deviceId);
		RoutingTable tableCopy = table.copy();
		table.setAllNextHop(deviceId);
		table.incAllDistanceToDestination();
		RouteTableMessage msg = new RouteTableMessage(tableCopy.getMap());
		linkLayer.sendBroadcast(msg);
		System.out.println("Table " + deviceId);
		System.out.println(table);
	}

	@Override
	public void updateNeighborData(NeighborDiscoveryData neighborData) {
	}

	@Override
	public void removeNeighborData(Address linkLayerAddress) {
		/*
		oneHopNeighbors.remove(linkLayerAddress);
		twoHopNeighbors.remove(linkLayerAddress);
		System.out.println(runtimeOperatingSystem.getDeviceID() + " #> " + linkLayerAddress);
		System.out.println("1h(" + runtimeOperatingSystem.getDeviceID() + ") = " + oneHopNeighbors);
		System.out.println("2h(" + runtimeOperatingSystem.getDeviceID() + ") = " + twoHopNeighbors);
		AddressSetMessage message = new AddressSetMessage(oneHopNeighbors);
		linkLayer.sendBroadcast(message);
		*/
	}

	@Override
	public void start(RuntimeOperatingSystem runtimeOperatingSystem) {
		this.runtimeOperatingSystem = runtimeOperatingSystem;
		this.deviceId = runtimeOperatingSystem.getDeviceID();
		
		//Am LinkLayer registrieren, um diesen aus TestService heraus nutzen zu k�nnen
		linkLayer=(LinkLayer_async)runtimeOperatingSystem.getSignalListenerStub(linkLayerID, LinkLayer_async.class);
				
		runtimeOperatingSystem.registerAtService(linkLayerID, LinkLayer_async.class);
				
		//Am Nachbarschaftsservice registrieren, um diesen aus TestService heraus nutzen zu k�nnen
		neighborService = (NeighborDiscoveryService_sync)runtimeOperatingSystem.getSignalListenerStub(neighborID, NeighborDiscoveryService_sync.class);	
		runtimeOperatingSystem.registerAtService(neighborID, NeighborDiscoveryService.class);
		
		table.put(deviceId, deviceId, 0, 0);
	}
	

	public void handleMessage(Address sender, HashMap<Address, DeviceRouteData> routeTable) {
		if(sender.toString().equals(deviceId.toString())){
			return ;
		}
		
		if (table.merge(routeTable) || table.isOwnReachabilityCorrect(routeTable, deviceId)){
			RouteTableMessage msg = new RouteTableMessage(table.getMap());
			linkLayer.sendBroadcast(msg);
		}
	}
}
