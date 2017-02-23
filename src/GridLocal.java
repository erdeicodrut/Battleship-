import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.Random;

public class GridLocal {
    final PApplet p;

    final PVector pos;
    final int m = 10, n = 10;
    final float w;

    final float size;

    Cell[][] grid = new Cell[m][n];
    ArrayList<Ship> ships = new ArrayList<>();

    static Random rand = new Random();

    PVector beginDragPos = new PVector();
    PVector endDragPos = new PVector();
    Ship draggedShip;

    GridLocal(PApplet p, PVector pos, float size)
    {
        this.p = p;
        this.pos = pos;

        this.size = size;
        this.w = size / m;

        clearGrid();

        int numberToSpawn = 4;

        for (Ship.Type type : Ship.Type.values())
        {
            for (int i = 0; i < numberToSpawn; i++)
            {
                Ship ship;
                do {
                    PVector newPos = new PVector(rand.nextInt(m), rand.nextInt(n));
//                    PVector newPos = new PVector(0, 0);
                    Ship.Orientation newOrientation = rand.nextBoolean() ? Ship.Orientation.H : Ship.Orientation.V;

                    ship = new Ship(p, type, newPos, newOrientation);
                    // System.out.println("BUG");
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

        for (PVector pos : ship.getPos())
            grid[(int) pos.x][(int) pos.y] = true;

        // Debugging
        // printGrid();
    }

    void removeShip(Ship ship)
    {
        ships.remove(ship);

        for (PVector pos : ship.getPos())
            grid[(int) pos.x][(int) pos.y] = false;

        // Debugging
        // printGrid();
    }

    Ship getShipAt(PVector targetPos)
    {
        for (Ship ship : ships)
            for (PVector shipPos : ship.getPos())
                if (targetPos.x == shipPos.x && targetPos.y == shipPos.y)
                    return ship;
        return null;
    }

    boolean validPos(PVector pos)
    {
        if (pos.x >= 0 && pos.x < m && pos.y >= 0 && pos.y < n)
            return true;
        return false;
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
                if (grid[(int) pos.x][(int) pos.y] == true)
                    return false;

        return true;
    }

    void clearGrid()
    {
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                grid[i][j] = new Cell(p, Cell.Type.UNDISCOVERED);
    }

    void updateGrid()
    {
        clearGrid();

        for (Ship ship : ships)
            for (PVector pos : ship.getPos())
                grid[(int) pos.x][(int) pos.y] = true;
    }

    void printGrid()
    {
        System.out.println("---------------------");
        for (int i = 0; i < m; i++)
        {
            for (int j = 0; j < n; j++)
                System.out.print(grid[i][j] ? " ■" : "  ");

            System.out.println();
        }
        System.out.println("---------------------");
    }

    void show() {
        p.strokeWeight(5);
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++) {
                p.fill(grid[i][j] ? 200 : 100);
                p.rect(pos.x + j * w, pos.y + i * w, w, w);
            }
    }

    PVector getMousePos()
    {
        PVector mousePos = new PVector(p.mouseY, p.mouseX);
        System.out.println("mousePos : " + mousePos);


        if (p.mouseX < pos.x || p.mouseX >= pos.x + size || p.mouseY < pos.y || p.mouseY >= pos.y + size)
            return null;

        PVector relPos = new PVector();
        PVector recalcPos = new PVector(pos.y, pos.x);
        System.out.println("pos : " + pos);

        PVector.sub(mousePos, recalcPos, relPos);
        System.out.println("relPos(after) : " + relPos);


        return new PVector((int)(relPos.x / w), (int)(relPos.y / w));
    }

    void mousePressed()
    {
        if (p.mouseButton == PConstants.LEFT)
        {
//            beginDragPos = new PVector(p.mouseY / w, p.mouseX / w);
            beginDragPos = getMousePos();
            if (beginDragPos == null)
                return;

            System.out.println(beginDragPos);

            draggedShip = getShipAt(beginDragPos);
        }
    }

    void mouseDragged()
    {
        if (draggedShip != null)
        {
//            PVector currentMousePos = new PVector(p.mouseY / w, p.mouseX / w);
            PVector currentMousePos = getMousePos();
            if (currentMousePos == null)
                return;
//            System.out.println(currentMousePos);


            PVector dragVector = new PVector();
            PVector.sub(currentMousePos, beginDragPos, dragVector);

            endDragPos = new PVector();
            PVector.add(draggedShip.pos, dragVector, endDragPos);

            // Small bug on dragging 2 size vertical ship downwards
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
        draggedShip = null;
    }
}
