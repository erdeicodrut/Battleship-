import processing.core.PApplet;

import java.util.ArrayList;
import java.util.Random;

public class GridLocal {
    PApplet p;

    boolean[][] grid = new boolean[14][14];

    Random rand = new Random();

    GridLocal(PApplet p)
    {
        this.p = p;

        for (int i = 0; i < 14; i++) {
            grid[i][0] = true;
            grid[i][13] = true;
        }
        for (int j = 0; j < 14; j++) {
            grid[13][j] = true;
            grid[0][j] = true;
        }

        // printGrid();

        int number = 4;
        for (ShipType type : ShipType.values())
        {
            for (int n = 0; n < number; n++)
            {
                Ship ship;
                do {
                    ship = new Ship(p, type);
                    // System.out.println("BUG");
                }
                while (!isShipValid(ship));

                placeShip(ship, true);
            }

            number--;
        }

    }

    boolean isShipValid(Ship ship)
    {
        try {
            if (ship.orientation == ShipOrientation.H) {
                // Extremities
                if (grid[ship.x - 1][ship.y] || grid[ship.x + ship.size][ship.y])
                    return false;

                // Edges
                for (int i = 0; i < ship.size; i++) {
                    for (int vOffset = -1; vOffset <= 1; vOffset++) {
                        if (grid[ship.x + i][ship.y + vOffset]) {
                            return false;
                        }
                    }
                }
            } else if (ship.orientation == ShipOrientation.V) {
                // Extremities
                if (grid[ship.x][ship.y - 1] || grid[ship.x][ship.y + ship.size])
                    return false;

                // Edges
                for (int i = 0; i < ship.size; i++) {
                    for (int hOffset = -1; hOffset <= 1; hOffset++) {
                        if (grid[ship.x + hOffset][ship.y + i]) {
                            return false;
                        }
                    }
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    void placeShip(Ship ship, boolean state)
    {
        if (ship.orientation == ShipOrientation.H)
        {
            for (int i = 0; i < ship.size; i++)
            {
                grid[ship.x + i][ship.y] = state;
            }
        }

        if (ship.orientation == ShipOrientation.V)
        {
            for (int j = 0; j < ship.size; j++)
            {
                grid[ship.x][ship.y + j] = state;
            }
        }
    }

    void show()
    {
        float w = p.width / 10;
//        p.color g = new Color(84, 255, 94);
//        Color r = new Color(255, 86, 94);

        p.strokeWeight(5);
        for (int i = 2; i < 12; i++)
        {
            for (int j = 2; j < 12; j++)
            {
                p.fill(grid[i][j] ? 200 : 100);

                p.rect((i-2)*w, (j-2)*w, w, w);
            }
        }
    }

    static ArrayList<PVector> neighbours = new ArrayList<>();
    static ShipOrientation selectedOrientation;

    void moveShip(int i, int j)
    {
        if (grid[i][j]) {

        }
    }

    void printGrid()
    {
        for (int i = 0; i < 14; i++) {
            for (int j = 0; j < 14; j++)
                System.out.print(grid[i][j] ? " ■" : "  ");

            System.out.println();
        }
    }
}

class PVector
{
    float x;
    float y;

    PVector(float x, float y)
    {
        this.x = x;
        this.y = y;
    }
}