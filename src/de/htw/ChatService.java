package de.htw;

import java.util.Observable;
import java.util.Set;

import de.uni_trier.jane.basetypes.Address;
import de.uni_trier.jane.basetypes.ServiceID;
import de.uni_trier.jane.service.EndpointClassID;
import de.uni_trier.jane.service.RuntimeService;
import de.uni_trier.jane.service.neighbor_discovery.NeighborDiscoveryServiceStub;
import de.uni_trier.jane.service.network.link_layer.LinkLayer_async;
import de.uni_trier.jane.service.operatingSystem.RuntimeOperatingSystem;
import de.uni_trier.jane.service.parameter.todo.Parameters;
import de.uni_trier.jane.visualization.shapes.Shape;

public class ChatService extends Observable implements RuntimeService{
	
	private static ServiceID serviceId = new EndpointClassID(ChatService.class.getName());
	private static ServiceID linkLayerId;
	private static ServiceID neighborId;
	private RuntimeOperatingSystem runtimeOperatingSystem;
	private Address address;
	private LinkLayer_async linkLayer;
	private DSDVService_sync dsdvService;
	
	public ChatService(ServiceID linkLayerId, ServiceID neighborId, DSDVService_sync dsdvService) {
		this.linkLayerId = linkLayerId;
		this.neighborId = neighborId;
		this.dsdvService = dsdvService;
	}
	
	@Override
	public ServiceID getServiceID() {
		return serviceId;
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
	public void start(RuntimeOperatingSystem runtimeOperatingSystem) {
		this.runtimeOperatingSystem = runtimeOperatingSystem;
		NeighborDiscoveryServiceStub neighborDiscoveryServiceStub = new NeighborDiscoveryServiceStub(runtimeOperatingSystem, neighborId);
		this.address = neighborDiscoveryServiceStub.getOwnAddress();
		this.linkLayer = (LinkLayer_async)runtimeOperatingSystem.getSignalListenerStub(linkLayerId, LinkLayer_async.class);
		runtimeOperatingSystem.registerAtService(linkLayerId, LinkLayer_async.class);
	}

	public void handleMessage(Address sender, String message, Address originSender, Address destination) {
		if(destination.toString().equals(this.address.toString())){
			String[] messageArr = {originSender.toString(), message, String.valueOf(dsdvService.getHopCount(originSender))};
			this.setChanged();
			this.notifyObservers(messageArr);
		} else {
			ChatMessage chatMessage = new ChatMessage(message, originSender, destination);
			Address receiver = dsdvService.getNextHop(destination);
			linkLayer.sendUnicast(receiver, chatMessage);
		}
	}
	
	private Address getReachbleDeviceAddress(String address){
		Set<Address> reachableAddresses = dsdvService.getAllReachableDevices();
		for(Address a: reachableAddresses){
			if(a.toString().equals(address)){
				return a;
			}
		}
		throw new RuntimeException("Device address unknown.");
	}
	
//	public Address getOwnAddress(){
//		throw new RuntimeException("Own Address not found.");
//	}
	
	public void sendMessage(String message, String destination) {
		Address sender = this.address;
		Address destinationAddress = getReachbleDeviceAddress(destination);
		ChatMessage msg = new ChatMessage(message, sender, destinationAddress);
		linkLayer.sendUnicast(dsdvService.getNextHop(destinationAddress), msg);
	}

}
