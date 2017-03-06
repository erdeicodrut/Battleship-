import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

public class GridRemote extends Grid
{
	GridRemote(PApplet p, PVector pos, float size)
	{
		super(p, pos, size);
	}

	// Ask the opponent, receive the feedback and handle
	void requestHit(PVector gridPos)
	{
		// Get respective cell
		Cell hitCell = getCell(gridPos);

		// Stop if the cell is already evaluated(other than ship block or undiscovered)
		if (!(hitCell.type == Cell.Type.SHIP_BLOCK || hitCell.type == Cell.Type.UNDISCOVERED))
			return;

		// Prevent spam clicking
		Game.allowInput = false;

		battleship.network.sendObject(hitCell);
	}

	void flagCell(Cell cell)
	{
		if (cell.type == Cell.Type.SHIP_BLOCK_HIT)
			flagHitCell(cell);
		else
			flagEmptyCell(cell);
	}

	void flagShip(Ship ship)
	{
		flagHitShip(ship);
	}

	// Returns if we clicked on the grid
	boolean mousePressed()
	{
		PVector mouseGridPos;
		if (p.mouseButton == PConstants.LEFT && (mouseGridPos = getMouseGridPos()) != null)
		{
			requestHit(mouseGridPos);
			return true;
		}
		return false;
	}
}
