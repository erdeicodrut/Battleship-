import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public class Cell {

    enum Type
    {
        SHIP_BLOCK,
        UNDISCOVERED,
        SHIP_BLOCK_HIT,
        EMPTY_BLOCK_HIT,
        UNCLICKABLE
    }

    final PApplet p;

    Type type;
    PVector gridPos;

    Cell(PApplet p, Type type, PVector gridPos)
    {
        this.p = p;
        this.type = type;
        this.gridPos = gridPos;
    }

    ArrayList<PVector> getCorners()
    {
        ArrayList<PVector> corners = new ArrayList<>();

        corners.add(new PVector(gridPos.x-1, gridPos.y-1));
        corners.add(new PVector(gridPos.x-1, gridPos.y+1));
        corners.add(new PVector(gridPos.x+1, gridPos.y-1));
        corners.add(new PVector(gridPos.x+1, gridPos.y+1));

        return corners;
    }

    ArrayList<PVector> getNeighbours()
    {
        ArrayList<PVector> neighbours = new ArrayList<>();

        for (int i = (int) (gridPos.x-1); i <= gridPos.x+1; i++)
            for (int j = (int) (gridPos.y-1); j <= gridPos.y+1; j++)
                if (!(i == gridPos.x && j == gridPos.y))
                    neighbours.add(new PVector(i, j));

        return neighbours;
    }
}
