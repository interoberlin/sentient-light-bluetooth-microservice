package de.interoberlin.iot.baumhaus;

import java.util.ArrayList;
import java.util.List;

import de.interoberlin.iot.Logging;
import tinyb.BluetoothDevice;
import tinyb.BluetoothGattCharacteristic;
import tinyb.BluetoothGattService;
import tinyb.BluetoothManager;

/**
 * This class facilitates access to a BLE floor sensor node. A sensor node
 * itself connects to 3-15 sensors and publishes their values via BLE.
 */
public class FloorSensorNode
{
    static String ServiceUUD = "00002011-0000-1000-8000-00805f9b34fb";
    static String ValueCharacteristicUUID = "00002014-0000-1000-8000-00805f9b34fb";
    
    /**
     * Physical bluetooth beacon address i.e. six bytes in hexadecimal
     * representation separated by colons
     */
    private String          address = null;
    /**
     * Object provided by tiny to facilitate bluetooth actions
     */
    private BluetoothDevice beacon  = null;

    /**
     * Initialize an object by providing a hardware address string
     * 
     * @param address
     *            Hardware address as string to identify a beacon, e.g.
     *            "25:6B:08:A6:2E:C7"
     */
    public FloorSensorNode(String address)
    {
        this.address = address.toLowerCase();
        getBluetoothDevice();
        connect();
    }

    /**
     * Attempt to get a BluetoothDevice object for this node's address from
     * tinyb
     */
    public BluetoothDevice getBluetoothDevice()
    {
        if (beacon != null)
            return beacon;

        BluetoothManager manager = BluetoothManager.getBluetoothManager();
        for (BluetoothDevice d : manager.getDevices())
        {
            if (d.getAddress().toLowerCase().equals(address))
            {
                beacon = d;
                return beacon;
            }
        }

        return null;
    }

    /**
     * Connect to floor sensor beacon
     */
    public void connect()
    {
        BluetoothDevice beacon = getBluetoothDevice();
        if (beacon == null)
        {
            Logging.error("Unable to get bluetooth device");
        }
        Logging.debug("Connecting to beacon...");
        beacon.connect();
    }

    /**
     * Retrieve the current floor sensor values of all sensors attached to this
     * node.
     */
    public List<Integer> pollSensorValues()
    {
        List<Integer> values = new ArrayList<>();
        
        BluetoothDevice beacon = getBluetoothDevice();
        if (beacon == null)
        {
            // Logging.error("Unable to get bluetooth device");
            return values;
        }

        if (!beacon.getConnected())
        {
            // System.out.println("Not connected to beacon.");
            connect();
            if (!beacon.getConnected())
            {
                // System.out.println("Failed to connect to beacon.");
                return values;
            }
        }

        if (!beacon.getServicesResolved())
        {
            // System.out.println("Services not yet resolved.");
            return values;
        }

//        try
//        {
            for (BluetoothGattService service : beacon.getServices())
            {
                if (service.getUUID().equals(ServiceUUD))
                {
        //            System.out.println("Service " + service.getUUID());
                    for (BluetoothGattCharacteristic characteristic : service.getCharacteristics())
                    {
    //                  System.out.println("Characteristic " + characteristic.getUUID());
                        if (characteristic.getUUID().equals(ValueCharacteristicUUID))
                        {
                            byte[] b = characteristic.readValue();
                            if (b.length < 30)
                                return values;
                            for (int i=0; i<30; i+=2)
                            {
                                Integer value = (Integer) (b[i+1] << 8 | b[i]);
                                values.add(value);
                            }
                        }
                    }
                }
            }
//        }
//        except RuntimeException(e)
//        {
//            pass;
//        }
        return values;
    }
}
