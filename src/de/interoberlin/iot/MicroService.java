package de.interoberlin.iot;

import de.baumhausberlin.sentient_light.FloorSensorNode;

public class MicroService
{
    public static void main(String[] args)
    {
        // Instantiate all desired beacons
        FloorSensorNode f1 = new FloorSensorNode("25:6B:08:A6:2E:C7");

        while (true)
        {
            // Query all the floor sensors
            f1.pollSensorValues();
        }
    }
}
