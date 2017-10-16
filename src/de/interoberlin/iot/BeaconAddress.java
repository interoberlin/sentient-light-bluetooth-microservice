package de.interoberlin.iot;

/**
 * This class stores the unique, physical address
 * by which a bluetooth beacon can be identified
 */
public class BeaconAddress
{
    private int[] address = {0, 0, 0, 0, 0, 0};
    
    /**
     * Initialize an address object by setting the six address bytes
     */
    public BeaconAddress(int a0, int a1, int a2, int a3, int a4, int a5)
    {
        this.set(a0, a1, a2, a3, a4, a5);
    }
    
    /**
     * Define address by setting all six address bytes
     */
    public void set(int a0, int a1, int a2, int a3, int a4, int a5)
    {
        address[0] = a0;
        address[1] = a1;
        address[2] = a2;
        address[3] = a3;
        address[4] = a4;
        address[5] = a5;
    }
}
