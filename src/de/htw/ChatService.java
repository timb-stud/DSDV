package de.htw;

import de.uni_trier.jane.basetypes.Address;
import de.uni_trier.jane.basetypes.ServiceID;
import de.uni_trier.jane.service.RuntimeService;
import de.uni_trier.jane.service.operatingSystem.RuntimeOperatingSystem;
import de.uni_trier.jane.service.parameter.todo.Parameters;
import de.uni_trier.jane.visualization.shapes.Shape;

public class ChatService implements RuntimeService{

	@Override
	public ServiceID getServiceID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Shape getShape() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void getParameters(Parameters parameters) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start(RuntimeOperatingSystem runtimeOperatingSystem) {
		// TODO Auto-generated method stub
		
	}

	public void handleMessage(Address sender, String message, Address originSender,
			Address destination) {
		// TODO Auto-generated method stub
		
	}

}
