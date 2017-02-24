import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

public class GridEnemy extends Grid {

    GridLocal localDebug;

    GridEnemy(PApplet p, PVector pos, float size, GridLocal localDebug)
    {
        super(p, pos, size);
        this.localDebug = localDebug;
    }

    void hit(PVector gridPos)
    {
        Cell hitCell = grid[(int) gridPos.x][(int) gridPos.y];

        if (!(hitCell.type == Cell.Type.SHIP_BLOCK || hitCell.type == Cell.Type.UNDISCOVERED))
            return;

        if (localDebug.hit(gridPos))
            hitCell.type = Cell.Type.SHIP_BLOCK_HIT;
        else
            hitCell.type = Cell.Type.EMPTY_BLOCK_HIT;

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
