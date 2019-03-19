package cavelikegen;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

/*
 * Grid Panel
 * 
 * A panel used to render a grid of cells. 
 * Every cell has a color, and a border color. 
 * Also, the panel has a background color.
 */

public class GridPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private Color background; // background color behind the grid of cells
	private Color[][] cellColors; // colors of the cells of the cells in the grid
	private Color[][] borderColors; // border colors of the cells in the grid
	
	private int cellSize; // size of a cell in pixels (size = width and height: width and height are the same)
	
	/*
	 * Grid Panel Constructor: constructs new GridPanel object.
	 * widthInCells = width of grid in cells.
	 * heightInCells = height of grid in cells.
	 * cellSize = size of a cell in pixels (size = width and height: width and height are the same).
	 * background = background color behind grid of cells which are rendered.
	 */
	GridPanel(int widthInCells, int heightInCells, int cellSize, Color background) {
		this.setSize(widthInCells * cellSize, heightInCells * cellSize);
		this.background = background;
		this.cellSize = cellSize;
		cellColors = new Color[widthInCells][heightInCells];
		borderColors = new Color[widthInCells][heightInCells];
	}
	
	
	/*
	 * Clears the grid of all cells.
	 */
	public void clearGrid() {
		for(int x = 0; x < cellColors.length; x++) {
			for(int y = 0; y < cellColors[0].length; y++) {
				cellColors[x][y] = null;
				borderColors[x][y] = null;
			}
		}
	}
	
	/*
	 * Renders the grid panel.
	 */
	public void paint(Graphics g) {	
		// fill background with background (color)
		g.setColor(background);
		g.fillRect(getX(), getY(), getWidth(), getHeight());
		// render the grid of cells
		for(int x = 0; x < cellColors.length; x++) {
			for(int y = 0; y < cellColors[0].length; y++) {
				Color cellColor = cellColors[x][y]; // a cell's color
				Color borderColor = borderColors[x][y]; // a cell's border color
				int xPos = x * cellSize; // top left position of cell (x)
				int yPos = y * cellSize; // top left position of cell (y)
				//fill cell with cellColor
				if(cellColor != null) {
					g.setColor(cellColor);
					g.fillRect(xPos, yPos, cellSize, cellSize);
				}
				//draw border (of color borderColor) around cell
				if(borderColor != null) {
					g.setColor(borderColor);
					g.drawRect(xPos, yPos, cellSize, cellSize);	
				}
			}
		}
		
	}
	
	/*
	 * Set cell color at cell (x, y) to Color color.
	 * If Color color is null, cell will be 100% transparent (it will have no color).
	 */
	public void setCellColor(int x, int y, Color color) {
		cellColors[x][y] = color;
	}
	
	
	/*
	 * Get cell color at cell (x, y)
	 * Returns a Color object, or null if the cell at (x, y) is 100% transparent (it has no color).
	 */
	public Color getCellColor(int x, int y) {
		return cellColors[x][y];
	}
	
	
	/*
	 * Set cell border color at (x, y)
	 * If Color color is null, cell border will be 100% transparent (it will have no border).
	 */
	public void setCellBorderColor(int x, int y, Color color) {
		borderColors[x][y] = color;
	}
	
	
	/*
	 * Get cell border color at (x, y)
	 * Returns a Color object, or null if the cell at (x, y) has a 100% transparent border (it has no border).
	 */
	public Color getCellBorderColor(int x, int y) {
		return borderColors[x][y];
	}
	
}
