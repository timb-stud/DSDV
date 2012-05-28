package de.htw;

import java.util.Set;

import de.uni_trier.jane.basetypes.Address;

public interface DSDVService_sync {

/**
*
* @return Gibt alle erreichbaren Geräte zurück
*/

public Set getAllReachableDevices();

/**
*
*@param Adresse des Zielhops
*@return Gibt den nächsten Hop in Richtung des gewünschten Zieles zurück
*/

public Address getNextHop(Address destination);

/**
*
*@param Adresse des Zielhops
*@return Gibt die Anzahl der Hops zu einem Ziel zurück
*/

public int getHopCount(Address destination);

}
