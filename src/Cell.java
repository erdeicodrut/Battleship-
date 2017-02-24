import processing.core.PApplet;
import processing.core.PVector;

public class Cell {

    enum Type {
        SHIP_BLOCK,
        UNDISCOVERED,
        SHIP_BLOCK_HIT,
        EMPTY_BLOCK_HIT,
        UNCLICKABLE
    }

    final PApplet p;

    Type type;
    PVector gridPos;

    Cell(PApplet p, Type type, PVector gridPos)
    {
        this.p = p;
        this.type = type;
        this.gridPos = gridPos;
    }
}
