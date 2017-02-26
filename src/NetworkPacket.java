import processing.core.PVector;

import java.util.ArrayList;

public class NetworkPacket {

    boolean hitAShip;
    ArrayList<PVector> coords;
    boolean shipDestroyed;

    NetworkPacket(ArrayList<PVector> coords, boolean shipDestroyed)
    {
        this.hitAShip = true;
        this.coords = coords;
        this.shipDestroyed = shipDestroyed;
    }

    NetworkPacket()
    {
        this.hitAShip = false;
        this.coords = null;
        this.shipDestroyed = false;
    }

    // TODO: Create methods for coding/decoding strings
}
