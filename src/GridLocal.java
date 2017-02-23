import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

import java.util.ArrayList;

public class GridLocal extends Grid {

    boolean frozen = false;

    ArrayList<Ship> ships = new ArrayList<>();

    PVector beginDragPos = new PVector();
    PVector endDragPos = new PVector();
    Ship draggedShip;

    GridLocal(PApplet p, PVector pos, float size)
    {
        super(p, pos, size);

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

    void addShip(Ship ship)
    {
        ships.add(ship);

        for (Cell cell : ship.getCells())
            addCell(cell);

        // Debugging
        printGrid();
    }

    void removeShip(Ship ship)
    {
        ships.remove(ship);

        for (Cell cell : ship.getCells())
            // Clone the position but change the type
            addCell(new Cell(p, Cell.Type.UNDISCOVERED, new PVector(cell.pos.x, cell.pos.y)));

        // Debugging
        printGrid();
    }

    Ship getShipAt(PVector targetPos)
    {
        for (Ship ship : ships)
            for (PVector shipPos : ship.getPos())
                if (targetPos.x == shipPos.x && targetPos.y == shipPos.y)
                    return ship;
        return null;
    }

    boolean validShip(Ship ship)
    {
        for (PVector bodyPos : ship.getPos())
            if (!validPos(bodyPos))
                return false;

        ArrayList<PVector> verifPos = new ArrayList<>();
        verifPos.addAll(ship.getPos());
        verifPos.addAll(ship.getNeighboursPos());
        for (PVector pos : verifPos)
            if (validPos(pos))
                if (grid[(int) pos.x][(int) pos.y].type == Cell.Type.SHIP_BLOCK)
                    return false;

        return true;
    }

    void updateGrid()
    {
        clearGrid();

        for (Ship ship : ships)
            for (Cell cell : ship.getCells())
                addCell(cell);
    }

    void freeze()
    {
        frozen = true;
    }

    boolean hit(PVector pos)
    {
        Cell hitCell = grid[(int) pos.x][(int) pos.y];
        if (hitCell.type == Cell.Type.SHIP_BLOCK)
        {
            hitCell.type = Cell.Type.SHIP_BLOCK_HIT;
            return true;
        }
        else
        {
            // Blank cell
            hitCell.type = Cell.Type.EMPTY_BLOCK_HIT;
            return false;
        }
    }

    //
    // Handling mouse events
    //

    void mousePressed()
    {
        if (frozen)
            return;

        if (p.mouseButton == PConstants.LEFT)
        {
            beginDragPos = getMouseGridPos();
            if (beginDragPos == null)
                return;

            System.out.println(beginDragPos);

            draggedShip = getShipAt(beginDragPos);
        }
    }

    void mouseDragged()
    {
        if (frozen)
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
                System.out.println("VALID");
                beginDragPos.set(currentMousePos.x, currentMousePos.y);
            }

            addShip(draggedShip);
        }
    }

    void mouseReleased()
    {
        if (frozen)
            return;

        draggedShip = null;
    }
}
