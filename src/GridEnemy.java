import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

import java.util.ArrayList;

public class GridEnemy extends Grid {

    GridLocal localDebug;

    GridEnemy(PApplet p, PVector pos, float size, GridLocal localDebug)
    {
        super(p, pos, size);
        this.localDebug = localDebug;
    }

    void hit(PVector gridPos)
    {
        Cell hitCell = get(gridPos);

        if (!(hitCell.type == Cell.Type.SHIP_BLOCK || hitCell.type == Cell.Type.UNDISCOVERED))
            return;

        // Is ship block or undiscovered block
        NetworkPacket packet = localDebug.hit(gridPos);
        // System.out.println(hitCells);

        if (packet.hitAShip == false)
            hitCell.type = Cell.Type.EMPTY_BLOCK_HIT;

        else if (packet.completelyHit == false)
        {
            hitCell.type = Cell.Type.SHIP_BLOCK_HIT;

            // Flag corners as unclickable
            // System.out.println(hitCell.getCorners());

            for (PVector cornerPos : hitCell.getCorners())
                if (validPos(cornerPos))
                {
                    Cell cell = get(cornerPos);
                    cell.type = Cell.Type.UNCLICKABLE;
                    set(cornerPos, cell);
                }
        }

        else if (packet.completelyHit)
        {
            hitCell.type = Cell.Type.SHIP_BLOCK_HIT;

            ArrayList<PVector> hitCells = packet.coords;

            for (PVector cellPos : hitCells)
                for (PVector neighbour : get(cellPos).getNeighbours())
                    if (validPos(neighbour))
                        if (get(neighbour).type != Cell.Type.SHIP_BLOCK_HIT)
                        {
                            Cell cell = get(neighbour);
                            cell.type = Cell.Type.UNCLICKABLE;
                            set(neighbour, cell);
                        }
        }
    }

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
