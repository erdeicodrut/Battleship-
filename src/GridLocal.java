import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

import java.util.ArrayList;

public class GridLocal extends Grid {

    ArrayList<Ship> ships = new ArrayList<>();

    // Helper vars for managing ship dragging
    PVector beginDragPos = new PVector();
    PVector endDragPos = new PVector();
    Ship draggedShip;

    // If the player finished placing the ships
    boolean ready = false;

    GridLocal(PApplet p, PVector pos, float size)
    {
        super(p, pos, size);

        // Ship placing
        int numberToSpawn = 4;

        for (Ship.Type type : Ship.Type.values())
        {
            for (int i = 0; i < numberToSpawn; i++)
            {
                Ship ship;
                do {
                    PVector newPos = new PVector(rand.nextInt(m), rand.nextInt(n));
                    Ship.Orientation newOrientation = rand.nextBoolean() ? Ship.Orientation.H : Ship.Orientation.V;

                    ship = new Ship(p, type, newPos, newOrientation);
                }
                while (!validShip(ship));

                addShip(ship);
                // updateGrid();
            }

            numberToSpawn--;
        }
    }

    // Finished placing the ships
    void beReady()
    {
        ready = true;
    }

    void addShip(Ship ship)
    {
        ships.add(ship);

        for (Cell cell : ship.getCells())
            setCell(cell);

        // Debugging
        // printGrid();
    }

    void removeShip(Ship ship)
    {
        ships.remove(ship);

        for (Cell cell : ship.getCells())
            // Clone the position but change the type
            setCell(new Cell(p, Cell.Type.UNDISCOVERED, new PVector(cell.gridPos.x, cell.gridPos.y)));

        // Debugging
        // printGrid();
    }

    // Returns the ship found at a given position / null instead
    Ship getShipAt(PVector targetPos)
    {
        for (Ship ship : ships)
            for (PVector bodyPos : ship.getBody())
                if (targetPos.x == bodyPos.x && targetPos.y == bodyPos.y)
                    return ship;
        return null;
    }

    // The ship must be at least on the grid and the existing neighbours must be also valid (no nearby ship)
    boolean validShip(Ship ship)
    {
        for (PVector bodyPos : ship.getBody())
            if (!validPos(bodyPos))
                return false;

        for (PVector pos : ship.getValidationArea())
            if (validPos(pos))
                if (grid[(int) pos.x][(int) pos.y].type == Cell.Type.SHIP_BLOCK)
                    return false;

        return true;
    }

    // Refresh the entire grid in case something goes wrong
    // Do not use unless you're Coddy *shots fired*
    void updateGrid()
    {
        clearGrid();

        for (Ship ship : ships)
            for (Cell cell : ship.getCells())
                setCell(cell);
    }

    // Handle, give feedback to the opponent
    Boolean hit(PVector gridPos)
    {
        boolean hitAShip = true;

        Cell cell = getCell(gridPos);

        // If a ship was hit
        if (cell.type == Cell.Type.SHIP_BLOCK)
        {
            // Flag the cell
            flagHitCell(cell);

            // If the entire ship is destroyed
            Ship hitShip = getShipAt(gridPos);
            if (shipDestroyed(hitShip))
            {
                flagHitShip(hitShip);

                battleship.network.sendObject(hitShip);
            }
            else
                // Just a cell is hit
                battleship.network.sendObject(cell);
        }

        // Just an empty cell
        else
        {
            flagEmptyCell(cell);
            hitAShip = false;

            battleship.network.sendObject(cell);
        }

        // Switch turn ?
        return hitAShip;
    }

    // If every cell of the ship is requestHit
    boolean shipDestroyed(Ship ship) {
        boolean destroyed = true;
        for (Cell cell : ship.getCells())
            if (cell.type != Cell.Type.SHIP_BLOCK_HIT)
            {
                destroyed = false;
                break;
            }
        return destroyed;
    }

    //
    // Handling mouse events (drag)
    // (block any event if the *player* hasn't finished placing the ships (not ready))
    //

    void mousePressed()
    {
        if (ready)
            return;

        if (p.mouseButton == PConstants.LEFT)
        {
            beginDragPos = getMouseGridPos();
            if (beginDragPos == null)
                return;

            draggedShip = getShipAt(beginDragPos);
        }
    }

    void mouseDragged()
    {
        if (ready)
            return;

        if (draggedShip != null)
        {
            PVector currentMousePos = getMouseGridPos();
            if (currentMousePos == null)
                return;

            PVector dragVector = new PVector();
            PVector.sub(currentMousePos, beginDragPos, dragVector);

            endDragPos = new PVector();
            PVector.add(draggedShip.pos, dragVector, endDragPos);

            // Small bug on dragging 2 length vertical ship downwards
//            if (beginDragPos.x == endDragPos.x && beginDragPos.y == endDragPos.y)
//                return;

            Ship destShip = new Ship(p, draggedShip.type, endDragPos, draggedShip.orientation);

            removeShip(draggedShip);

            if (validShip(destShip))
            {
                draggedShip = destShip;
                beginDragPos.set(currentMousePos.x, currentMousePos.y);
            }

            addShip(draggedShip);
        }
    }

    void mouseReleased()
    {
        if (ready)
            return;

        draggedShip = null;
    }
}
