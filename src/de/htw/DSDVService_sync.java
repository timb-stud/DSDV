package de.htw;

import java.util.Set;

import de.uni_trier.jane.basetypes.Address;

public interface DSDVService_sync {

/**
*
* @return all reachable devices
*/

public Set getAllReachableDevices();

/**
*
*@param destination Address
*@return the next hop in direction of the given destination Address
*/

public Address getNextHop(Address destination);

/**
*
*@param destination Address
*@return the hop count for the given destination Address
*/

public int getHopCount(Address destination);

}
