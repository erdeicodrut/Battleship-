import processing.core.PApplet;
import processing.core.PVector;

import java.io.Serializable;
import java.util.ArrayList;

class Ship implements Serializable
{
	transient final PApplet p;

	Type type;
	PVector pos;
	Orientation orientation;
	int length;
	ArrayList<Cell> cells = new ArrayList<>();

	enum Type
	{
		SUBMARINE,
		DESTROYER,
		CRUISER,
		BATTLESHIP
	}

	enum Orientation
	{
		H, V
	}

	Ship(PApplet p, Type type, PVector pos, Orientation orientation)
	{
		// Setup
		this.p = p;
		this.type = type;
		this.pos = pos;
		this.orientation = orientation;

		switch (this.type)
		{
			case SUBMARINE:
				length = 1;
				break;
			case DESTROYER:
				length = 2;
				break;
			case CRUISER:
				length = 3;
				break;
			case BATTLESHIP:
				length = 4;
				break;
		}

		// Creation of cells
		for (PVector cellPos : getBody())
			cells.add(new Cell(p, Cell.Type.SHIP_BLOCK, cellPos));
	}

	boolean destroyed()
	{
		boolean destroyed = true;
		for (Cell cell : getCells())
			if (cell.type != Cell.Type.SHIP_BLOCK_HIT)
			{
				destroyed = false;
				break;
			}
		return destroyed;
	}

	// Returns occupied positions
	ArrayList<PVector> getBody()
	{
		ArrayList<PVector> list = new ArrayList<>();

		if (orientation == Orientation.V)
			for (int i = (int) pos.x; i < pos.x + length; i++)
				list.add(new PVector(i, pos.y));

		else if (orientation == Orientation.H)
			for (int j = (int) pos.y; j < pos.y + length; j++)
				list.add(new PVector(pos.x, j));

		return list;
	}

	ArrayList<Cell> getCells()
	{
		return cells;
	}

	// getNeighbours = getValidationArea - getBody
	ArrayList<PVector> getNeighbours()
	{
		ArrayList<PVector> neighbours = getValidationArea();
		for (PVector pos : getBody())
			neighbours.remove(pos);

		return neighbours;
	}

	// Returns an array of all validation positions(possible or not)
	ArrayList<PVector> getValidationArea()
	{
		ArrayList<PVector> list = new ArrayList<>();

		int start_i = (int) pos.x - 1;
		int end_i = (int) ((orientation == Orientation.V) ? pos.x + length : pos.x + 1);
		int start_j = (int) pos.y - 1;
		int end_j = (int) ((orientation == Orientation.V) ? pos.y + 1 : pos.y + length);

		for (int i = start_i; i <= end_i; i++)
			for (int j = start_j; j <= end_j; j++)
				list.add(new PVector(i, j));

		return list;
	}
}
