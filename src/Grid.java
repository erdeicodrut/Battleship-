import processing.core.PApplet;
import processing.core.PVector;

import java.util.Random;

public class Grid
{
	static Random rand = new Random();
	final PApplet p;

	// Absolute position of the grid
	final PVector pos;

	final int m = 10;
	final int n = 10;
	Cell[][] grid = new Cell[m][n];

	// Size of the grid in pixels
	final float pxSize;
	// Size of a cell also in pixels
	final float cellSize;

	public Grid(PApplet p, PVector pos, float pxSize)
	{
		this.p = p;
		this.pxSize = pxSize;
		this.cellSize = this.pxSize / m;
		this.pos = pos;

		clearGrid();
	}

	// Set all the cells to blank(undiscovered)
	void clearGrid()
	{
		for (int i = 0; i < m; i++)
			for (int j = 0; j < n; j++)
				grid[i][j] = new Cell(p, Cell.Type.UNDISCOVERED, new PVector(i, j));
	}

	// Replace a cell in a given position with another one
	Cell setCell(Cell cell)
	{
		grid[(int) cell.gridPos.x][(int) cell.gridPos.y] = cell;
		return getCell(cell.gridPos);
	}

	// Return the cell in the given position
	Cell getCell(PVector gridPos)
	{
		return grid[(int) gridPos.x][(int) gridPos.y];
	}

	// Flag the cell and the corners if a ship was hit
	void flagHitCell(Cell cell)
	{
		Cell gridCell = getCell(cell.gridPos);

		// Flag the cell
		gridCell.type = Cell.Type.SHIP_BLOCK_HIT;

		// Flag the corners
		for (PVector corner : gridCell.getCorners())
			if (validPos(corner))
				getCell(corner).type = Cell.Type.UNCLICKABLE;
	}

	// If the given position(i, j) is in the grid
	boolean validPos(PVector pos)
	{
		if (pos.x >= 0 && pos.x < m && pos.y >= 0 && pos.y < n)
			return true;
		return false;
	}

	void flagEmptyCell(Cell cell)
	{
		getCell(cell.gridPos).type = Cell.Type.EMPTY_BLOCK_HIT;
	}

	void flagHitShip(Ship ship)
	{
		// Flag ship cells
		for (PVector body : ship.getBody())
			getCell(body).type = Cell.Type.SHIP_BLOCK_HIT;

		// Flag neighbours
		for (PVector neighbour : ship.getNeighbours())
			if (validPos(neighbour))
				getCell(neighbour).type = Cell.Type.UNCLICKABLE;
	}

	// For the sake of debugging
	void printGrid()
	{
		System.out.println("---------------------");
		for (int i = 0; i < m; i++)
		{
			for (int j = 0; j < n; j++)
				System.out.print(grid[i][j].type == Cell.Type.SHIP_BLOCK ? " ■" : "  ");

			System.out.println();
		}
		System.out.println("---------------------");
	}

	// Show all the cells
	void show()
	{
		p.strokeWeight(5);
		for (int i = 0; i < m; i++)
			for (int j = 0; j < n; j++)
				showCell(grid[i][j]);
	}

	// Show a single cell
	void showCell(Cell cell)
	{
		int color = 0;
		switch (cell.type)
		{
			case SHIP_BLOCK:
				color = 0x888888;
				break;
			case UNDISCOVERED:
				color = 0x555555;
				break;
			case SHIP_BLOCK_HIT:
				color = 0xFF0000;
				break;
			case EMPTY_BLOCK_HIT:
				color = 0xFFFFFF;
				break;
			case UNCLICKABLE:
				color = 0xFFFFFF;
				break;
		}

		int r = (color & 0xFF0000) >> 16;
		int g = (color & 0x00FF00) >> 8;
		int b = color & 0x0000FF;

		p.fill(r, g, b);
		p.rect(pos.x + cell.gridPos.y * cellSize, pos.y + cell.gridPos.x * cellSize, cellSize, cellSize);
	}

	// Returns the grid position(i, j) or null if the user didn't click on the grid
	PVector getMouseGridPos()
	{
		PVector mouseGridPos = new PVector(p.mouseY, p.mouseX);

		if (p.mouseX < pos.x || p.mouseX >= pos.x + pxSize || p.mouseY < pos.y || p.mouseY >= pos.y + pxSize)
			return null;

		PVector gridPos = new PVector(pos.y, pos.x);

		PVector relPos = new PVector();
		PVector.sub(mouseGridPos, gridPos, relPos);

		return new PVector((int) (relPos.x / cellSize), (int) (relPos.y / cellSize));
	}
}
