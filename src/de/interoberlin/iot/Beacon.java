package de.interoberlin.iot;

/**
 * This class facilitates all operations
 * possible with a BLE beacon.
 */
public class Beacon
{
    private BeaconAddress address;
    
    /**
     * Initialize a beacon object by providing it's address
     * 
     * @param address   Address of the bluetooth beacon to interact with
     */
    public Beacon(BeaconAddress address)
    {
        this.address = address;
    }
}
