package de.htw;

import java.util.HashMap;

import de.uni_trier.jane.basetypes.Address;

public class AddressMap extends HashMap<Address, DeviceRouteData>{
	private static final long serialVersionUID = 1L;
	
	public AddressMap(){
		super();
	}

	@Override
	public DeviceRouteData get(Object device) {
		return (super.get(device) != null) ? 
				super.get(device) : manualFindDrd(device);
	}
	
	private DeviceRouteData manualFindDrd(Object device){
		for (Address a : super.keySet()){
			if(a.toString().equals(device.toString())){
				return super.get(a);
			}
		}
		return null;
	}
}
