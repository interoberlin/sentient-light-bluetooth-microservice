
package de.interoberlin;

import tinyb.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DiscoverServices {
    private static final int SCAN_DURATION = 3;

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    private static boolean running = true;

    private static List<BluetoothDevice> devices;
    private static BluetoothDevice device;

    // --------------------
    // Methods
    // --------------------

    public static void main(String[] args) throws InterruptedException, IOException {
        BluetoothManager manager = BluetoothManager.getBluetoothManager();

        // Scan devices

        devices = scanDevices(manager);

        showDevices(devices);

        device = selectDevice(devices);

        connectDevice(device);

        boolean discoveryStarted = manager.startDiscovery();
        System.out.println("The discovery " + (discoveryStarted ? "started." : "did not start!"));
        Thread.sleep(500);

        Lock lock = new ReentrantLock();
        Condition cv = lock.newCondition();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                running = false;
                lock.lock();
                try {
                    cv.signalAll();
                } finally {
                    lock.unlock();
                }

            }
        });

        showServices(device);
    }

    private static List<BluetoothDevice> scanDevices(BluetoothManager manager) throws InterruptedException {
        manager.startDiscovery();
        System.out.println("Start scan");

        for (int i = 0; i < SCAN_DURATION; i++) {
            System.out.print(".");
            Thread.sleep(1000);
        }

        try {
            manager.stopDiscovery();
        } catch (BluetoothException e) {
            System.err.println("Discovery could not be stopped.");
        }

        System.out.println("");
        return manager.getDevices();
    }

    private static void showDevices(List<BluetoothDevice> devices) {
        int i = 0;
        for (BluetoothDevice device : devices) {
            if (device.getConnected()) {
                System.out.println(ANSI_GREEN + "#" + ++i + ANSI_RESET + " " + device.getAddress() + " " + device.getName() + " " + device.getConnected());
            } else {
                System.out.println(ANSI_RED + "#" + ++i + ANSI_RESET + " " + device.getAddress() + " " + device.getName() + " " + device.getConnected());
            }
        }
    }

    private static BluetoothDevice selectDevice(List<BluetoothDevice> devices) throws IOException {
        System.out.println("\nSelect a device");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int pickedDeviceID = -1;

        while (true) {
            try {
                pickedDeviceID = Integer.parseInt(br.readLine());
            } catch (NumberFormatException e) {
                System.out.println(".. not a number");
                continue;
            }

            if (pickedDeviceID > devices.size()) {
                System.out.println(".. not in range");
                continue;
            }

            return devices.get(pickedDeviceID - 1);
        }
    }

    private static void connectDevice(BluetoothDevice device) {
        System.out.print("\nConnect device " + ANSI_GREEN + device.getAddress() + ANSI_RESET + " " + device.getName());
        if (device.connect())
            System.out.println(".. sensor with the provided address connected");
        else {
            System.out.println(".. could not connect device");
            System.exit(-1);
        }
    }

    private static void showServices(BluetoothDevice device) {
        System.out.println("\nShow service (" + device.getServices().size() + ")");
        for (BluetoothGattService service : device.getServices()) {
            System.out.println("Service " + service.getUUID());
            for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
                System.out.println("Characteristic " + ANSI_PURPLE + characteristic.getUUID() + ANSI_RESET);
            }
        }
    }
}
