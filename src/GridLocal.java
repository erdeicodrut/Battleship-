import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

import java.util.ArrayList;

public class GridLocal extends Grid
{
	ArrayList<Ship> ships = new ArrayList<>();

	// Helper vars for managing ship dragging
	PVector beginDragPos = new PVector();
	PVector endDragPos = new PVector();
	Ship draggedShip;

	GridLocal(PApplet p, PVector pos, float size)
	{
		super(p, pos, size);

		// Ship placing
		int numberToSpawn = 4;

		for (Ship.Type type : Ship.Type.values())
		{
			for (int i = 0; i < numberToSpawn; i++)
			{
				Ship ship;
				do
				{
					PVector newPos = new PVector(rand.nextInt(m), rand.nextInt(n));
					Ship.Orientation newOrientation = rand.nextBoolean() ? Ship.Orientation.H : Ship.Orientation.V;

					ship = new Ship(p, type, newPos, newOrientation);
				}
				while (!validShip(ship));

				addShip(ship);
				// updateGrid();
			}

			numberToSpawn--;
		}
	}

	// Refresh the entire grid in case something goes wrong
	// Do not use unless you're Coddy *shots fired*
	void updateGrid()
	{
		clearGrid();

		for (Ship ship : ships)
			for (Cell cell : ship.getCells())
				setCell(cell);
	}

	// The ship must be at least on the grid and the existing neighbours must be also valid (no nearby ship)
	boolean validShip(Ship ship)
	{
		for (PVector bodyPos : ship.getBody())
			if (!validPos(bodyPos))
				return false;

		for (PVector pos : ship.getValidationArea())
			if (validPos(pos))
				if (grid[(int) pos.x][(int) pos.y].type == Cell.Type.SHIP_BLOCK)
					return false;

		return true;
	}

	void addShip(Ship ship)
	{
		ships.add(ship);

		for (Cell cell : ship.getCells())
			setCell(cell);

		// Debugging
		// printGrid();
	}

	void removeShip(Ship ship)
	{
		ships.remove(ship);

		for (Cell cell : ship.getCells())
			// Clone the position but change the type
			setCell(new Cell(p, Cell.Type.UNDISCOVERED, new PVector(cell.gridPos.x, cell.gridPos.y)));

		// Debugging
		// printGrid();
	}

	// Handle, give feedback to the opponent
	Boolean hit(PVector gridPos)
	{
		boolean hitAShip = true;

		Cell cell = getCell(gridPos);

		// If a ship was hit
		if (cell.type == Cell.Type.SHIP_BLOCK)
		{
			// Flag the cell
			flagHitCell(cell);

			// If the entire ship is destroyed
			Ship hitShip = getShipAt(gridPos);
			if (hitShip.destroyed())
			{
				flagHitShip(hitShip);

				battleship.network.sendObject(hitShip);
			}
			else
				// Just a cell is hit
				battleship.network.sendObject(cell);
		}

		// Just an empty cell
		else
		{
			flagEmptyCell(cell);
			hitAShip = false;

			battleship.network.sendObject(cell);
		}

		// Switch turn ?
		return hitAShip;
	}

	// Returns the ship found at a given position / null instead
	Ship getShipAt(PVector targetPos)
	{
		for (Ship ship : ships)
			for (PVector bodyPos : ship.getBody())
				if (targetPos.x == bodyPos.x && targetPos.y == bodyPos.y)
					return ship;
		return null;
	}

	void mousePressed()
	{
		if (p.mouseButton == PConstants.LEFT)
		{
			beginDragPos = getMouseGridPos();
			if (beginDragPos == null)
				return;

			draggedShip = getShipAt(beginDragPos);
		}
	}

	void mouseDragged()
	{
		if (draggedShip != null)
		{
			PVector currentMousePos = getMouseGridPos();
			if (currentMousePos == null)
				return;

			PVector dragVector = new PVector();
			PVector.sub(currentMousePos, beginDragPos, dragVector);

			endDragPos = new PVector();
			PVector.add(draggedShip.pos, dragVector, endDragPos);

			// Small bug on dragging 2 length vertical ship downwards
//            if (beginDragPos.x == endDragPos.x && beginDragPos.y == endDragPos.y)
//                return;

			Ship destShip = new Ship(p, draggedShip.type, endDragPos, draggedShip.orientation);

			removeShip(draggedShip);

			if (validShip(destShip))
			{
				draggedShip = destShip;
				beginDragPos.set(currentMousePos.x, currentMousePos.y);
			}

			addShip(draggedShip);
		}
	}

	void mouseReleased()
	{
		draggedShip = null;
	}
}
