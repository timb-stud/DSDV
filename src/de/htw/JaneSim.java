package de.htw;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.uni_trier.jane.basetypes.Extent;
import de.uni_trier.jane.basetypes.Position;
import de.uni_trier.jane.basetypes.Rectangle;
import de.uni_trier.jane.basetypes.ServiceID;
import de.uni_trier.jane.gui.ExtendedClickAndPlaySimulationGUI;
import de.uni_trier.jane.service.neighbor_discovery.NeighborDiscoveryService;
import de.uni_trier.jane.service.neighbor_discovery.OneHopNeighborDiscoveryService;
import de.uni_trier.jane.service.network.link_layer.LinkLayer;
import de.uni_trier.jane.service.network.link_layer.collision_free.CollisionFreeNetwork;
import de.uni_trier.jane.service.unit.ServiceUnit;
import de.uni_trier.jane.simulation.Simulation;
import de.uni_trier.jane.simulation.SimulationParameters;
import de.uni_trier.jane.simulation.dynamic.mobility_source.ClickAndPlayMobilitySource;
import de.uni_trier.jane.simulation.dynamic.mobility_source.MobilitySource;
import de.uni_trier.jane.simulation.dynamic.mobility_source.campus.ClickAndPlayMobilitySourceLocation;
import de.uni_trier.jane.simulation.dynamic.mobility_source.campus.FixedPositionLocation;
import de.uni_trier.jane.simulation.kernel.TimeExceeded;


public class JaneSim extends Simulation implements Runnable{

	private ArrayList<DsdvService> testServiceList = new ArrayList<DsdvService>();
	
	public ArrayList<DsdvService> getDsdvServiceList(){
		return testServiceList;
	}
	
	@Override
	public void initGlobalServices(ServiceUnit serviceUnit) {
		CollisionFreeNetwork.createInstance(serviceUnit, 1024 * 100,true, true, true);
		
	}

	@Override
	public void initServices(ServiceUnit serviceUnit) {
		//Ref zum LinkLayer
		ServiceID linkLayerID = serviceUnit.getService(LinkLayer.class);
		
		//Nachbarschaftsservice anlegen
		OneHopNeighborDiscoveryService.createInstance(serviceUnit, false);
		
		//Ref zum Nachbarschaftsservice
		ServiceID neighborID = serviceUnit.getService(NeighborDiscoveryService.class);

		DsdvService testService = new DsdvService(linkLayerID, neighborID);
		serviceUnit.addService(testService);
		testServiceList.add(testService);
	}

	@Override
	public void initSimulation(SimulationParameters parameters) {
		FixedPositionLocation fixed = new FixedPositionLocation
		(new Position[]
		              {
						new Position(10,10),
						new Position(60,10),
						new Position(110,10),
						new Position(50, 100),
						new Position(100, 100)
					  },
						new Rectangle(new Extent(200,200))
		);
		
		MobilitySource mobilitySource = null;
		mobilitySource = new ClickAndPlayMobilitySourceLocation(fixed,
				20.0,
				50.0, 
				5, 
				1.0);
		
		ExtendedClickAndPlaySimulationGUI gui = new ExtendedClickAndPlaySimulationGUI(
				(ClickAndPlayMobilitySource) mobilitySource );
		
		parameters.useVisualisation(gui);
		parameters.setMobilitySource( mobilitySource );
		parameters.setTerminalCondition(
				new TimeExceeded(parameters.getEventSet(),1000));	

	}
	
	public static void main(String[] args) {
		Simulation simulation = new JaneSim();
		simulation.run();
	}

}
