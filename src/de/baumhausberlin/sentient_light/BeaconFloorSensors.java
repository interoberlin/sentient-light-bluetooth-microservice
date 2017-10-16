package de.baumhausberlin.sentient_light;

/**
 * This class facilitates access to a BLE floor sensor node.
 * A sensor node itself connects to 3-15 sensors
 * and publishes their values via BLE.
 */
public class BeaconFloorSensors
{
    /**
     * Object to access beacon i.e. BLE GATT server
     */
    private Beacon beacon;
    
    public BeaconFloorSensors(Beacon b)
    {
        setBeacon(b);
    }
    
    /**
     * Retrieve the current floor sensor values
     * of all sensors attached to this node.
     */
    public void pollSensorValues()
    {
        // TODO
        // if not connected: connect
        // read characteristic
        // split values
    }
    
    public void setBeacon(Beacon b)
    {
        this.beacon = b;
    }
}
