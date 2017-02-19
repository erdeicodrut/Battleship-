import processing.core.PApplet;

public class battleship extends PApplet{
    GridLocal local;

    public void settings()
    {
        size(800, 800);
    }

    public void setup()
    {
        local = new GridLocal(this);

        local.printGrid();
    }

    public void draw()
    {
        background(51);
        local.show();
    }

    public void mouseClicked()
    {
        if (mouseButton == LEFT)
        {
            int y = mouseX / 100 + 2;
            int x = mouseY / 100 + 2;

            System.out.println(x + " " + y + "\n" + local.grid[x][y]);

            local.moveShip(x, y);


        }
    }


}
