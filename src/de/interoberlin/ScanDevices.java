
package de.interoberlin;

import tinyb.*;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ScanDevices {
    private static final int SCAN_DURATION = 10;

    // --------------------
    // Methods
    // --------------------

    public static void main(String[] args) throws InterruptedException {
        BluetoothManager manager = BluetoothManager.getBluetoothManager();

        manager.startDiscovery();
        System.out.println("Started scan");

        for (int i = 0; i < SCAN_DURATION; i++) {
            System.out.print(".");
            Thread.sleep(1000);
        }


        try {
            manager.stopDiscovery();
        } catch (BluetoothException e) {
            System.err.println("Discovery could not be stopped.");
        }

        for (BluetoothDevice device : manager.getDevices()) {
            System.out.println("Found " + device.getAddress() + " " + device.getName());
        }
    }
}
