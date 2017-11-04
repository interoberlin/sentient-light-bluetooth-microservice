package de.interoberlin.iot;

import de.interoberlin.iot.baumhaus.FloorSensorNode;

public class Main
{
    public static void main(String[] args)
    {
        Logging.debug("Hello World!");

        // Instantiate all desired beacons
        FloorSensorNode f1 = new FloorSensorNode("6D:EF:89:E0:8A:DB");

        // broker.addNode(f1);
//        Broker.startup();

        while (true)
        {
            // Query all the floor sensors
            System.out.println( f1.pollSensorValues() );
        }

    }
}
