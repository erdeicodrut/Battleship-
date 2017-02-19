import processing.core.PApplet;

import java.util.ArrayList;
import java.util.Random;

enum ShipType
{
    SUBMARINE,
    DESTROYER,
    CRUISER,
    BATTLESHIP
//    CARRIER;
};

enum ShipOrientation
{
    H,
    V
}

class Ship implements IShip
{
    PApplet p;
    Random rand = new Random();

    int x, y;
    int size;
    ShipOrientation orientation;

    Ship(PApplet p, ShipType type)
    {
        this.p = p;

        switch (type)
        {
            case SUBMARINE:
                size = 1; break;
            case DESTROYER:
                size = 2; break;
            case CRUISER:
                size = 3; break;
            case BATTLESHIP:
                size = 4; break;
        }

        pickRandomLocation();
        System.out.println(x + " " + y);
        orientation = rand.nextBoolean() ? ShipOrientation.H : ShipOrientation.V;
    }

    Ship(PApplet p, ArrayList<PVector> coords, ShipOrientation orientation)
    {
        this.p = p;

        size = 0;

        x = (int)coords.get(0).x;
        y = (int)coords.get(0).y;
        this.orientation = orientation;
    }

    void pickRandomLocation()
    {
        x = rand.nextInt(10) + 2;
        y = rand.nextInt(10) + 2;
    }

    @Override
    public void show() {

    }
}