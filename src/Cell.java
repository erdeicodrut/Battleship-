import processing.core.PApplet;
import processing.core.PVector;

import java.awt.*;

/**
 * Created by coddy on 22.02.2017.
 */
public class Cell {

    enum Type {
        SHIP_BLOCK,
        UNDISCOVERED,
        SHIP_BLOCK_HIT,
        EMPTY_BLOCK_HIT,
        UNCLICKABLE
    }

    PApplet p;
    Type type;
    public PVector pos;

    Cell(PApplet p, Type type, PVector pos) {
        this.p = p;
        this.type = type;
        this.pos = pos;
    }

    void show(PVector absGridPos, float size)
    {
        int color = 0;
        switch (type)
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

        // System.out.println(r + " " + g + " " + b);
        p.fill(r, g, b);
        p.rect(absGridPos.x + pos.y * size, absGridPos.y + pos.x * size, size, size);
    }
}
