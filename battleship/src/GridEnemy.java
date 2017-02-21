import processing.core.PApplet;
import processing.core.PVector;

public class GridEnemy {
    final PApplet p;

    final PVector pos;
    final int m = 10, n = 10;
    final float w;

    final float size;

    boolean[][] grid = new boolean[m][n];

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

//    boolean flag(PVector pos)
//    {
//
//    }

    void mousePressed()
    {

    }
}
