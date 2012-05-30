package de.htw;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

import de.uni_trier.jane.basetypes.Address;
import de.uni_trier.jane.basetypes.Dispatchable;
import de.uni_trier.jane.basetypes.Extent;
import de.uni_trier.jane.service.network.link_layer.LinkLayerInfo;
import de.uni_trier.jane.service.network.link_layer.LinkLayerMessage;
import de.uni_trier.jane.signaling.SignalListener;
import de.uni_trier.jane.visualization.Color;
import de.uni_trier.jane.visualization.shapes.RectangleShape;
import de.uni_trier.jane.visualization.shapes.Shape;


public class RouteTableMessage implements LinkLayerMessage, Serializable {

	private static final long serialVersionUID = -4389492289287873189L;

	private HashMap<Address, DeviceRouteData> routeTable;
	
	public RouteTableMessage(HashMap<Address, DeviceRouteData> routeTable) {
		super();
		this.routeTable = routeTable;
	}

	@Override
	public void handle(LinkLayerInfo info, SignalListener listener) {
		//System.out.println("Sende Nachricht von: "+ sender);
		
		((DsdvService)listener).handleMessage(info.getSender(), routeTable);

	}

	@Override
	public Dispatchable copy() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public Class getReceiverServiceClass() {
		// TODO Auto-generated method stub
		return DsdvService.class;
	}

	@Override
	public Shape getShape() {
		return new RectangleShape(new Extent(10,10), Color.RED,false);
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 1024 * 100;
	}

}
