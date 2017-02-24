import processing.core.PApplet;
import processing.core.PVector;

import java.util.Random;

public class Grid {

    final PApplet p;
    static Random rand = new Random();

    final PVector pos;
    final int m = 10;
    final int n = 10;
    Cell[][] grid = new Cell[m][n];

    final float pxSize;
    final float cellSize;

    public Grid(PApplet p, PVector pos, float pxSize) {
        this.p = p;
        this.pxSize = pxSize;
        this.cellSize = this.pxSize / m;
        this.pos = pos;

        clearGrid();
    }

    void clearGrid()
    {
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                grid[i][j] = new Cell(p, Cell.Type.UNDISCOVERED, new PVector(i, j));
    }

    boolean validPos(PVector pos)
    {
        if (pos.x >= 0 && pos.x < m && pos.y >= 0 && pos.y < n)
            return true;
        return false;
    }

    void addCell(Cell cell)
    {
        grid[(int) cell.gridPos.x][(int) cell.gridPos.y] = cell;
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
                showCell(grid[i][j]);
    }

    void showCell(Cell cell)
    {
        int color = 0;
        switch (cell.type)
        {
            case SHIP_BLOCK:
                color = 0x888888; break;
            case UNDISCOVERED:
                color = 0x555555; break;
            case SHIP_BLOCK_HIT:
                color = 0xFF0000; break;
            case EMPTY_BLOCK_HIT:
                color = 0xFFFFFF;
            case UNCLICKABLE:
                // TODO: Implement visual representation of remaining cells
                break;
        }

        int r = (color & 0xFF0000) >> 16;
        int g = (color & 0x00FF00) >> 8;
        int b = color & 0x0000FF;

        p.fill(r, g, b);
        p.rect(pos.x + cell.gridPos.y * cellSize, pos.y + cell.gridPos.x * cellSize, cellSize, cellSize);
    }

    // Returns null if not clicked on the grid
    PVector getMouseGridPos()
    {
        PVector mouseGridPos = new PVector(p.mouseY, p.mouseX);

        if (p.mouseX < pos.x || p.mouseX >= pos.x + pxSize || p.mouseY < pos.y || p.mouseY >= pos.y + pxSize)
            return null;

        PVector gridPos = new PVector(pos.y, pos.x);

        PVector relPos = new PVector();
        PVector.sub(mouseGridPos, gridPos, relPos);

        return new PVector((int)(relPos.x / cellSize), (int)(relPos.y / cellSize));
    }
}
