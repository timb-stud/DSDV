package
de.htw;

import java.util.Set;

import de.uni_trier.jane.basetypes.Address;

public interface TwoHopService_sync {
	
	/**
	 * 
	 * @return all the direct neighbors of the mobile device
	 */
	public Set<Address> getOneHopNeighbors();
	
	/**
	 * 
	 * @param device Address of the required device
	 * @return all direct neighbors of the the given device address
	 */
	public Set<Address> getTwoHopNeighborsFrom(Address device);
}
