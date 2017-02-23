import processing.core.PApplet;
import processing.core.PVector;

/**
 * Created by coddy on 22.02.2017.
 */
public class Cell {
    enum Type {
        UNDISCOVERED,
        SHIP_BLOCK,
        SHIP_BLOCK_HIT,
        EMPTY_BLOCK_HIT,
        UNCLICKABLE
    }

    PApplet p;
    Type type;
    PVector pos;

    Cell(PApplet p, Type type, PVector pos) {
        this.p = p;
        this.type = type;
        this.pos = pos;
    }

    void show()
    {

    }
}
