import processing.core.PApplet;
import processing.core.PVector;

import java.io.Serializable;
import java.util.ArrayList;

public class Cell implements Serializable
{
	transient final PApplet p;

	Type type;
	PVector gridPos;

	enum Type
	{
		SHIP_BLOCK,
		UNDISCOVERED,
		SHIP_BLOCK_HIT,
		EMPTY_BLOCK_HIT,
		UNCLICKABLE
	}

	Cell(PApplet p, Type type, PVector gridPos)
	{
		this.p = p;
		this.type = type;
		this.gridPos = gridPos;
	}

	// Get cell's corners (valid or not)
	ArrayList<PVector> getCorners()
	{
		ArrayList<PVector> corners = new ArrayList<>();

		corners.add(new PVector(gridPos.x - 1, gridPos.y - 1));
		corners.add(new PVector(gridPos.x - 1, gridPos.y + 1));
		corners.add(new PVector(gridPos.x + 1, gridPos.y - 1));
		corners.add(new PVector(gridPos.x + 1, gridPos.y + 1));

		return corners;
	}
}
