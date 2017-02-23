import processing.core.PApplet;
import processing.core.PVector;

import java.util.Random;

/**
 * Created by roscale on 2/23/17.
 */
public class Grid {

    static Random rand = new Random();
    final PApplet p;
    final PVector pos;
    final int m = 10;
    final int n = 10;
    final float size;
    final float cellSize;
    Cell[][] grid = new Cell[m][n];

    public Grid(PApplet p, PVector pos, float size) {
        this.p = p;
        this.size = size;
        this.cellSize = this.size / m;
        this.pos = pos;

        clearGrid();
    }

    void addCell(Cell cell)
    {
        grid[(int) cell.pos.x][(int) cell.pos.y] = cell;
    }

    boolean validPos(PVector pos)
    {
        if (pos.x >= 0 && pos.x < m && pos.y >= 0 && pos.y < n)
            return true;
        return false;
    }

    void clearGrid()
    {
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                grid[i][j] = new Cell(p, Cell.Type.UNDISCOVERED, new PVector(i, j));
    }

    void printGrid()
    {
        System.out.println("---------------------");
        for (int i = 0; i < m; i++)
        {
            for (int j = 0; j < n; j++)
                System.out.print(grid[i][j].type == Cell.Type.SHIP_BLOCK ? " ■" : "  ");

            System.out.println();
        }
        System.out.println("---------------------");
    }

    void show()
    {
        p.strokeWeight(5);
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                grid[i][j].show(pos, cellSize);
    }

    PVector getMouseGridPos()
    {
        PVector mousePos = new PVector(p.mouseY, p.mouseX);
        // System.out.println("mousePos : " + mousePos);

        if (p.mouseX < pos.x || p.mouseX >= pos.x + size || p.mouseY < pos.y || p.mouseY >= pos.y + size)
            return null;

        PVector relPos = new PVector();
        PVector recalcPos = new PVector(pos.y, pos.x);
        // System.out.println("pos : " + pos);

        PVector.sub(mousePos, recalcPos, relPos);
        // System.out.println("relPos(after) : " + relPos);

        return new PVector((int)(relPos.x / cellSize), (int)(relPos.y / cellSize));
    }
}
