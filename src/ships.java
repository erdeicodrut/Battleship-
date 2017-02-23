import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.Random;


class Ship {
    enum Type
    {
        SUBMARINE,
        DESTROYER,
        CRUISER,
        BATTLESHIP
    }

    enum Orientation
    {
        H,
        V
    }

    final PApplet p;

    static Random rand = new Random();

    Type type;
    PVector pos;
    int length;
    Orientation orientation;
    ArrayList<Cell> cells = new ArrayList<>();


    Ship(PApplet p, Type type, PVector pos, Orientation orientation)
    {
        this.p = p;

        switch (type)
        {
            case SUBMARINE:
                length = 1; break;
            case DESTROYER:
                length = 2; break;
            case CRUISER:
                length = 3; break;
            case BATTLESHIP:
                length = 4; break;
        }

        this.type = type;
        this.pos = pos;
        this.orientation = orientation;

        for (PVector cellPos : getPos())
            cells.add(new Cell(p, Cell.Type.SHIP_BLOCK, cellPos));
    }

    ArrayList<PVector> getPos()
    {
        ArrayList<PVector> list = new ArrayList<>();

        if (orientation == Orientation.V)
            for (int i = (int) pos.x; i < pos.x + length; i++)
                list.add(new PVector(i, pos.y));

        else if (orientation == Orientation.H)
            for (int j = (int) pos.y; j < pos.y + length; j++)
                list.add(new PVector(pos.x, j));

        return list;
    }

    ArrayList<Cell> getCells()
    {
        return cells;
    }

    ArrayList<PVector> getNeighboursPos()
    {
        ArrayList<PVector> list = new ArrayList<>();

        if (orientation == Orientation.V)
        {
            for (int i = (int) pos.x-1; i < pos.x+1 + length; i++)
                for (int j = (int) (pos.y - 1); j <= pos.y + 1; j++) {
                    if (i == pos.x && j == pos.y)
                        continue;
                    list.add(new PVector(i, j));
                }
        }

        else if (orientation == Orientation.H)
        {
            for (int j = (int) pos.y-1; j < pos.y+1 + length; j++)
                for (int i = (int) (pos.x - 1); i <= pos.x + 1; i++) {
                    if (i == pos.x && j == pos.y)
                        continue;
                    list.add(new PVector(i, j));
                }
        }

        return list;
    }
}
