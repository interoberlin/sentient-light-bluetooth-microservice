package de.interoberlin.iot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import tinyb.BluetoothDevice;
import tinyb.BluetoothException;
import tinyb.BluetoothGattCharacteristic;
import tinyb.BluetoothGattService;
import tinyb.BluetoothManager;

/**
 * The IoT broker manages the connections and data flow from and to all
 * configured Bluetooth nodes
 */
public class Broker
{
    public static int scanDuration = 3;
    
    /**
     * Initialize all variables, setup regular Bluetooth scanning
     * @throws InterruptedException 
     * @throws IOException 
     */
    public static void startup()
    {
        BluetoothManager manager = BluetoothManager.getBluetoothManager();

        // Scan devices

        List<BluetoothDevice> devices = null;
        try
        {
            devices = scanDevices(manager);
        } catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        showDevices(devices);

        BluetoothDevice device = null;
        try
        {
            device = selectDevice(devices);
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        connectDevice(device);

        boolean discoveryStarted = manager.startDiscovery();
        System.out.println("The discovery " + (discoveryStarted ? "started." : "did not start!"));
        try
        {
            Thread.sleep(500);
        } catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        showServices(device);
    }

    public static List<BluetoothDevice> scanDevices(BluetoothManager manager) throws InterruptedException
    {
        manager.startDiscovery();
        System.out.println("Start scan");

        for (int i = 0; i < scanDuration; i++)
        {
            System.out.print(".");
            Thread.sleep(1000);
        }

        try
        {
            manager.stopDiscovery();
        } catch (BluetoothException e)
        {
            System.err.println("Discovery could not be stopped.");
        }

        System.out.println("");
        return manager.getDevices();
    }

    public static void showDevices(List<BluetoothDevice> devices)
    {
        int i = 0;
        for (BluetoothDevice device : devices)
        {
            if (device.getConnected())
            {
                Logging.debug(Logging.ANSI_GREEN + "#" + ++i + Logging.ANSI_RESET + " " + device.getAddress() + " "
                        + device.getName() + " " + device.getConnected());
            } else
            {
                Logging.debug(Logging.ANSI_RED + "#" + ++i + Logging.ANSI_RESET + " " + device.getAddress() + " "
                        + device.getName() + " " + device.getConnected());
            }
        }
    }

    public static BluetoothDevice selectDevice(List<BluetoothDevice> devices) throws IOException
    {
        System.out.println("\nSelect a device");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int pickedDeviceID = -1;

        while (true)
        {
            try
            {
                pickedDeviceID = Integer.parseInt(br.readLine());
            } catch (NumberFormatException e)
            {
                System.out.println(".. not a number");
                continue;
            }

            if (pickedDeviceID > devices.size())
            {
                System.out.println(".. not in range");
                continue;
            }

            return devices.get(pickedDeviceID - 1);
        }
    }

    public static void connectDevice(BluetoothDevice device)
    {
        System.out.print("\nConnect device " + Logging.ANSI_GREEN + device.getAddress() + Logging.ANSI_RESET + " " + device.getName());
        if (device.connect())
            System.out.println(".. sensor with the provided address connected");
        else
        {
            System.out.println(".. could not connect device");
            System.exit(-1);
        }
    }

    public static void showServices(BluetoothDevice device)
    {
        System.out.println("\nShow service (" + device.getServices().size() + ")");
        for (BluetoothGattService service : device.getServices())
        {
            System.out.println("Service " + service.getUUID());
            for (BluetoothGattCharacteristic characteristic : service.getCharacteristics())
            {
                System.out.println("Characteristic " + Logging.ANSI_PURPLE + characteristic.getUUID() + Logging.ANSI_RESET);
            }
        }
    }
}
