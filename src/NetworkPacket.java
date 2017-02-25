import processing.core.PVector;

import java.util.ArrayList;

public class NetworkPacket {
    boolean hitAShip;
    ArrayList<PVector> coords;
    boolean completelyHit;

    NetworkPacket(ArrayList<PVector> coords, boolean completelyHit)
    {
        this.hitAShip = true;
        this.coords = coords;
        this.completelyHit = completelyHit;
    }

    NetworkPacket()
    {
        this.hitAShip = false;
        this.coords = null;
        this.completelyHit = false;
    }

    // TODO: Create methods for coding/deconding into string
}
