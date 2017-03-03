import P2P.Connection;
import processing.core.PApplet;

public class battleship extends PApplet
{
	static Connection network;
	static Game currentGame;

	public void settings()
	{
		size(1000, 500);
	}

	public void setup()
	{

	}

	public void draw()
	{
		if (currentGame == null)
			return;

		for (Object obj : network.receiveObjects())
			currentGame.parse(obj);

		background(220);
		currentGame.show();
	}

	public void mousePressed()
	{
		currentGame.handleEvent(InputEvent.MOUSE_PRESSED);
	}

	public void mouseReleased()
	{
		currentGame.handleEvent(InputEvent.MOUSE_RELEASED);
	}

	public void mouseDragged()
	{
		currentGame.handleEvent(InputEvent.MOUSE_DRAGGED);
	}

	public void keyPressed()
	{
		// Create server
		if (key == 's')
		{
			network = new Connection(this, 12345);
			currentGame = new Game(this, true);
		}
		// Create client
		else if (key == 'c')
		{
			network = new Connection(this, "192.168.1.3", 12345);
			currentGame = new Game(this, false);
		}
		// Game related
		else if (key == ' ')
			currentGame.handleEvent(InputEvent.KEY_PRESSED);
	}
}
