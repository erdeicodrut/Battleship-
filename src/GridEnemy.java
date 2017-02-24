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

    boolean hit(PVector pos)
    {
        Cell hitCell = grid[(int) pos.x][(int) pos.y];
        if (localDebug.hit(pos))
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

    void mousePressed()
    {
        PVector mousePos;
        if (p.mouseButton == PConstants.LEFT && (mousePos = getMouseGridPos()) != null)
        {
            hit(mousePos);
        }
    }
}
