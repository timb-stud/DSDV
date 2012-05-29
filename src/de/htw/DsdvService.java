package de.htw;


import java.util.HashMap;
import java.util.Set;

import de.uni_trier.jane.basetypes.Address;
import de.uni_trier.jane.basetypes.ServiceID;
import de.uni_trier.jane.service.EndpointClassID;
import de.uni_trier.jane.service.RuntimeService;
import de.uni_trier.jane.service.neighbor_discovery.NeighborDiscoveryData;
import de.uni_trier.jane.service.neighbor_discovery.NeighborDiscoveryListener;
import de.uni_trier.jane.service.neighbor_discovery.NeighborDiscoveryService;
import de.uni_trier.jane.service.neighbor_discovery.NeighborDiscoveryServiceStub;
import de.uni_trier.jane.service.neighbor_discovery.NeighborDiscoveryService_sync;
import de.uni_trier.jane.service.network.link_layer.LinkLayer_async;
import de.uni_trier.jane.service.operatingSystem.RuntimeOperatingSystem;
import de.uni_trier.jane.service.parameter.todo.Parameters;
import de.uni_trier.jane.visualization.shapes.Shape;



public class DsdvService implements RuntimeService, NeighborDiscoveryListener, DSDVService_sync {

	public static ServiceID serviceID;
	private ServiceID linkLayerID;
	private ServiceID neighborID;
	private Address address;
	
	
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
		System.out.println(this.address + " --> " + neighborData);
//		Address neighborAddress = neighborData.getSender();
//		table.put(neighborAddress, neighborAddress, 1, 2); //TODO seqNum really == 2 ???
		table.incSeqNum(this.address, 2);
		RoutingTable tableCopy = table.copy();
		tableCopy.setAllNextHop(this.address);
		tableCopy.incAllDistanceToDestination();
		RouteTableMessage msg = new RouteTableMessage(tableCopy.getMap());
		linkLayer.sendBroadcast(msg);
		
		System.out.println("Table" + this.address);
		System.out.println(table);
		
		System.out.println("Sending updated Table from: " + this.address);
		System.out.println(tableCopy);
	}

	@Override
	public void updateNeighborData(NeighborDiscoveryData neighborData) {
	}

	@Override
	public void removeNeighborData(Address linkLayerAddress) { //TODO check
		System.out.println(this.address + " ##> " + linkLayerAddress);
		//table.incSeqNum(linkLayerAddress, 1);
		//table.setDistanceToDestinationToInfinity(linkLayerAddress);
		table.updateMapWithRemoveInformations(linkLayerAddress);
		RoutingTable tableCopy = table.copy();
		RouteTableMessage msg = new RouteTableMessage(tableCopy.getMap());
		linkLayer.sendBroadcast(msg);
		
		System.out.println("After remove Table " + this.address);
		System.out.println(table);
	}

	@Override
	public void start(RuntimeOperatingSystem runtimeOperatingSystem) {
		this.runtimeOperatingSystem = runtimeOperatingSystem;
		NeighborDiscoveryServiceStub neighborDiscoveryServiceStub = new NeighborDiscoveryServiceStub(runtimeOperatingSystem, neighborID);
		this.address = neighborDiscoveryServiceStub.getOwnAddress();
		
		//Am LinkLayer registrieren, um diesen aus TestService heraus nutzen zu k�nnen
		linkLayer=(LinkLayer_async)runtimeOperatingSystem.getSignalListenerStub(linkLayerID, LinkLayer_async.class);
				
		runtimeOperatingSystem.registerAtService(linkLayerID, LinkLayer_async.class);
				
		//Am Nachbarschaftsservice registrieren, um diesen aus TestService heraus nutzen zu k�nnen
		neighborService = (NeighborDiscoveryService_sync)runtimeOperatingSystem.getSignalListenerStub(neighborID, NeighborDiscoveryService_sync.class);	
		runtimeOperatingSystem.registerAtService(neighborID, NeighborDiscoveryService.class);
		
		table.put(this.address, this.address, 0, 0);
	}
	

	public void handleMessage(Address sender, HashMap<Address, DeviceRouteData> routeTable) {
		if(sender.toString().equals(this.address.toString())){
			return ;
		}
		//System.out.println("ownTable: \n" + table + "\n");
		//System.out.println("otherTable: \n" + routeTable + "\n");
		boolean isOwnReachabilityCorrect = table.isOwnReachabilityCorrect(routeTable, this.address);
		boolean merge = table.merge(routeTable, this.address);
		System.out.println("merge: " + merge + "  isOwn: " + isOwnReachabilityCorrect);
		if (merge || !isOwnReachabilityCorrect){
			RoutingTable tableCopy = table.copy();
			tableCopy.setAllNextHop(this.address);	//TODO maybe not necessary
			tableCopy.incAllDistanceToDestination();
			System.out.println("Sending updated Table from: " + this.address);
			System.out.println(tableCopy);
			RouteTableMessage msg = new RouteTableMessage(tableCopy.getMap());
			linkLayer.sendBroadcast(msg);
		}
		System.out.println("Table " + this.address + " after handling MSG from " + sender);
		System.out.println(table);
	}

	@Override
	public Set getAllReachableDevices() {
		return table.getMap().entrySet();
	}

	@Override
	public Address getNextHop(Address destination) {
		return table.getNextHop(destination);
	}

	@Override
	public int getHopCount(Address destination) {
		return table.getHopCount(destination);
	}
}
