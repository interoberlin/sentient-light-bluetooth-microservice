package de.baumhausberlin.sentient_light;

import de.interoberlin.iot.Beacon;
import tinyb.BluetoothDevice;
import tinyb.BluetoothGattCharacteristic;
import tinyb.BluetoothGattService;
import tinyb.BluetoothManager;

/**
 * This class facilitates access to a BLE floor sensor node.
 * A sensor node itself connects to 3-15 sensors
 * and publishes their values via BLE.
 */
public class FloorSensorNode
{
    /**
     * Object to access beacon i.e. BLE GATT server
     */
    private BluetoothDevice beacon;

    public FloorSensorNode(String address)
    {
        BluetoothManager manager = BluetoothManager.getBluetoothManager();
        for (BluetoothDevice d : manager.getDevices())
        {
            if (d.getAddress().toLowerCase().equals(address.toLowerCase()))
            {
                beacon = d;
                break;
            }
        }
    }

    /**
     * Connect to floor sensor beacon
     */
    public void connect()
    {
        System.out.println("Connecting to beacon...");
        beacon.connect();
    }

    /**
     * Retrieve the current floor sensor values
     * of all sensors attached to this node.
     */
    public void pollSensorValues()
    {
        if (!beacon.getConnected())
        {
            System.out.println("Not connected to beacon.");
            connect();
            if (!beacon.getConnected())
            {
                System.out.println("Failed to connect to beacon.");
                return;
            }
        }

        if (!beacon.getServicesResolved())
        {
            System.out.println("Services not yet resolved.");
            return;
        }

        System.out.println("\nShow service (" + beacon.getServices().size() + ")");
        for (BluetoothGattService service : beacon.getServices()) {
            System.out.println("Service " + service.getUUID());
            for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
                System.out.println("Characteristic " + characteristic.getUUID());
            }
        }
    }
}
