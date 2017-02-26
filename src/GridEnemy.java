import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

import java.util.ArrayList;

public class GridEnemy extends Grid {

    // Testing locally for now
    GridLocal localDebug;

    GridEnemy(PApplet p, PVector pos, float size, GridLocal localDebug)
    {
        super(p, pos, size);
        this.localDebug = localDebug;
    }

    // Ask the opponent, receive the feedback and handle
    void hit(PVector gridPos)
    {
        // Get respective cell
        Cell hitCell = getCell(gridPos);

        // Stop if the cell is already evaluated(other than ship block or undiscovered)
        if (!(hitCell.type == Cell.Type.SHIP_BLOCK || hitCell.type == Cell.Type.UNDISCOVERED))
            return;

        // Get information from the opponent
        NetworkPacket packet = localDebug.hit(gridPos);

        // If we hit a ship
        if (packet.hitAShip)
        {
            // Hit it
            hitCell.type = Cell.Type.SHIP_BLOCK_HIT;

            // If the entire ship is destroyed, surround it with unclickable cells
            if (packet.shipDestroyed)
            {
                ArrayList<PVector> hitCells = packet.coords;

                for (PVector cellPos : hitCells)
                    for (PVector neighbourPos : getCell(cellPos).getNeighbours())
                        if (validPos(neighbourPos))
                            if (getCell(neighbourPos).type != Cell.Type.SHIP_BLOCK_HIT)
                            {
                                Cell neighbour = getCell(neighbourPos);
                                neighbour.type = Cell.Type.UNCLICKABLE;
                                setCell(neighbour);
                            }
            }

            // We didn't destroy the entire ship
            else
            {
                // Flag corners as unclickable
                for (PVector cornerPos : hitCell.getCorners())
                    if (validPos(cornerPos))
                    {
                        Cell corner = getCell(cornerPos);
                        corner.type = Cell.Type.UNCLICKABLE;
                        setCell(corner);
                    }
            }
        }

        // We hit an empty block
        else
            hitCell.type = Cell.Type.EMPTY_BLOCK_HIT;
    }

    //
    // Handling mouse events (hit)
    // (block any event if the *opponent* hasn't finished placing the ships (not ready))
    //

    void mousePressed()
    {
        PVector mouseGridPos;
        if (p.mouseButton == PConstants.LEFT && (mouseGridPos = getMouseGridPos()) != null)
        {
            if (!localDebug.ready)
                return;

            hit(mouseGridPos);
        }
    }
}
