import processing.core.PApplet;
import processing.core.PVector;
import processing.net.*;

public class GridEnemy {
    final PApplet p;

    // Client

    final PVector pos;
    final int m = 10, n = 10;
    final float w;

    final float size;

    Cell[][] grid = new Cell[m][n];

    GridEnemy(PApplet p, PVector pos, float size)
    {
        this.p = p;
        this.pos = pos;

        this.size = size;
        this.w = size / m;
    }

    void show() {
        p.strokeWeight(5);
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++) {
                p.fill(grid[i][j] ? 200 : 100);
                p.rect(pos.x + j * w, pos.y + i * w, w, w);
            }
    }

    boolean flag(PVector pos)
    {
        if (isHit(pos))
        {
            grid[pos.x][pos.y] = true;
        }
    }

    void mousePressed()
    {
        PVector currentMousePos = getMousePos();


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
}
