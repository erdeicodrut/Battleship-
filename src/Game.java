import processing.core.PApplet;
import processing.core.PVector;

enum InputEvent
{
	MOUSE_PRESSED,
	MOUSE_DRAGGED,
	MOUSE_RELEASED,
	KEY_PRESSED
}

public class Game
{
	PApplet p;

	GridLocal local;
	GridRemote remote;

	static boolean ready, opponentReady;
	// Spam prevention
	static boolean allowInput;
	static boolean myTurn;

	Game(PApplet p, boolean myTurn)
	{
		this.p = p;

		local = new GridLocal(p, new PVector(0, 0), p.height);
		remote = new GridRemote(p, new PVector(p.width / 2 + 25, 0), p.height);

		this.myTurn = myTurn;
		this.allowInput = myTurn;

		// Debugging purposes
		local.printGrid();
	}

	public void show()
	{
		local.show();
		remote.show();

		// Workaround for transparent foreground
		if (ready && (!opponentReady || !myTurn))
		{
			p.pushStyle();
			p.fill(0, 75);
			p.noStroke();
			p.rect(0, 0, p.width, p.height);
			p.popStyle();
		}
	}

	public void handleEvent(InputEvent e)
	{
		if (e == InputEvent.MOUSE_PRESSED)
		{
			if (!ready)
				local.mousePressed();

			if (ready && opponentReady && allowInput)
				remote.mousePressed();
		}
		else if (e == InputEvent.MOUSE_DRAGGED)
		{
			if (!ready)
				local.mouseDragged();

			else if (e == InputEvent.MOUSE_RELEASED)
				if (!ready)
					local.mouseReleased();
		}

		if (e == InputEvent.KEY_PRESSED)
			if (p.key == ' ')
				beReady();
	}

	public void beReady()
	{
		ready = true;
		battleship.network.sendObject(new Ready());
	}

	public void parse(Object obj)
	{
		if (obj.getClass() == Ready.class)
			opponentReady = true;

		if (obj.getClass() == Cell.class)
		{
			Cell cell = (Cell) obj;

			if (myTurn)
				remote.flagCell(cell);

			else
			{
				if (!local.hit(cell.gridPos))
				{
					// Switch turn
					allowInput = true;
					myTurn = true;

					battleship.network.sendObject(new LostTurn(true));
				}
				else
					battleship.network.sendObject(new LostTurn(false));
			}
		}
		else if (obj.getClass() == Ship.class)
		{
			Ship ship = (Ship) obj;

			if (myTurn)
				remote.flagShip(ship);

			else
			{
				// The remote never asks for an entire ship
			}
		}

		// Switch turn
		else if (obj.getClass() == LostTurn.class)
		{
			LostTurn lostTurn = (LostTurn) obj;

			if (myTurn)
			{
				if (lostTurn.value == true)
				{
					allowInput = false;
					myTurn = false;
				}
				else
				{
					allowInput = true;
					myTurn = true;
				}
			}
		}
	}
}
