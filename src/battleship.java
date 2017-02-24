import processing.core.PApplet;
import processing.core.PVector;

public class battleship extends PApplet {

    GridLocal local;
    GridEnemy enemy;

    public void settings()
    {
        size(1600, 800);
    }

    public void setup()
    {
        local = new GridLocal(this, new PVector(0, 0), height);
        enemy = new GridEnemy(this, new PVector(width/2, 0), height, local);

        local.printGrid();
    }

    public void draw()
    {
        background(220);
        local.show();
        enemy.show();
    }

    public void mousePressed()
    {
        local.mousePressed();
        enemy.mousePressed();
    }

    public void mouseDragged()
    {
        local.mouseDragged();
    }

    public void mouseReleased()
    {
        local.mouseReleased();
    }

    public void keyPressed()
    {
        if (key == ' ')
            local.beReady();
    }
}
