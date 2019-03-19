package cavelikegen;

import java.awt.Point;
import java.util.ArrayList;

/*
 * Cellular Automaton
 * 
 * A cellular automaton used for generating cave-like levels.
 * 
 * A cell is either a WALL or a PASSAGE.
 * boolean[][] cellGrid stores the state of the cells in the cellular automaton, where WALL = true, PASSAGE = false.
 * 
 * How to generate cave levels:
 * 1. Create a new CellularAutomaton object.
 * 2. Call fillCellGridWithNoise(double chanceOfWall).
 * 3. Call applyRules() i times.
 * 4. A cave system will form.
 * 
 * Fine-tuning cave level generation:
 * o Use cullSmallerCaverns() to fill in all but the largest cavern with walls. 
 * o Use isBigEnough(int desiredArea) to check if a cave system is as big as you want it to be.
 */

public class CellularAutomaton {

	public static final boolean WALL = true;
	public static final boolean PASSAGE = false;
	
	private boolean[][] cellGrid; // stores the state of the cells in the cellular automaton
	private int width; //width of cell grid in cells
	private int height; //height of cell grid in cells
	
	/*
	 * Cellular Automaton Constructor: total number of cells = width * height. Width, height are in cells.
	 */
	public CellularAutomaton(int width, int height) {
		this.width = width;
		this.height = height;
		cellGrid = new boolean[width][height];
		fillCellGridWithWalls();
	}
	
	/*
	 * Fills the cell grid with walls.
	 */
	public void fillCellGridWithWalls() {
		for(int x = 0; x < width; x++)
			for(int y = 0; y < height; y++)
				cellGrid[x][y] = WALL;
	}
	
	/*
	 * Fills the cell grid with random noise (the chance of a cell being a wall is chanceOfWall, e.g. 0.5 = 50% wall, 0.45 = %45 wall).
	 */
	public void fillCellGridWithNoise(double chanceOfWall){
		for(int x = 0; x < width; x++)
			for(int y = 0; y < height; y++)
				cellGrid[x][y] = Math.random() <= chanceOfWall;
	}
	
	/*
	 * Returns the state of cell at (x, y).
	 * Point coordOfCell must be within the boundaries of the cell grid (i.e. 0 <= x < width, 0 <= y < height).
	 */
	public boolean getStateOfCell(Point coordOfCell) {
		return cellGrid[coordOfCell.x][coordOfCell.y];
	}
	
	/*
	 * Applies the rules of the cellular automaton to every cell one time.
	 * THE RULES:
	 * 1. If a wall has less than 4 wall neighbors, then it becomes a passage.
	 * 2. If a passage has 5 or more wall neighbors, then it becomes a wall.
	 * Otherwise, the cell remains the same.
	 * EXCEPTION: If a cell is on the border, break the rules and make it a wall.
	 */
	public void applyRules() {
		// determine how many wall neighbors surround each cell (IMPORTANT: do this to all cells before applying rules to any of them)
		int[][] numWallNeighbors = new int[width][height]; //numWallNeighbors[x][y] = number of wall neighbors of a cell at (x, y)
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				numWallNeighbors[x][y] = numWallNeighbors(new Point(x, y));
			}
		}
		// now apply the rules to each cell
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				boolean currentState = cellGrid[x][y]; // a cell's current state
				boolean newState; // a cell's state will change to this
				int numNeighbors = numWallNeighbors[x][y]; // how many neighbors a cell has
				// if a wall has less than 4 wall neighbors, then it becomes a passage.
				if(currentState == WALL && numNeighbors < 4) {
					newState = PASSAGE;
				}
				// if a passage has 5 or more wall neighbors, then it becomes a wall
				else if(currentState == PASSAGE && numNeighbors >= 5) {
					newState = WALL;
				}
				// otherwise its state remains the same
				else {
					newState = currentState;
				}
				// EXCEPTION: if the cell is on the grid's border, break the rules and make it a wall.
				if(isOnBorder(new Point(x, y))) {
					newState = WALL;
				}
				cellGrid[x][y] = newState;
			}
		}
	}
	
	/*
	 * Determines the number of neighboring walls a cell at (x, y) has.
	 * Cells on the cell grid's border don't have 8 neighbors; all missing neighbors are counted as walls.
	 * Point coordOfCell must be within the boundaries of the cell grid (i.e. 0 <= x < width, 0 <= y < height).
	 */
	public int numWallNeighbors(Point coordOfCell) {
		int x = coordOfCell.x;
		int y = coordOfCell.y;
		int numWallNeighbors = 0;
		// a cell and its neighbors:
		// 123
		// 4C5
		// 678
		// cell: C.
		// neighbors 1-8: 1-8.
		// determine if the following neighbors are walls, increase numWallNeighbors accordingly:
		// neighbor 1 @ (x - 1, y - 1)
		if((x - 1 >= 0 && y - 1 >= 0 && cellGrid[x - 1][y - 1] == WALL) || (x - 1 < 0 && y - 1 < 0)) {
			numWallNeighbors += 1;
		}
		// neighbor 2 @ (x, y - 1)
		if((y - 1 >= 0 && cellGrid[x][y - 1] == WALL) || (y - 1 < 0)) {
			numWallNeighbors += 1;
		}
		// neighbor 3 @ (x + 1, y - 1)
		if((x + 1 < width && y - 1 >= 0 && cellGrid[x + 1][y - 1] == WALL) || (x + 1 >= width && y - 1 < 0)) {
			numWallNeighbors += 1;
		}
		// neighbor 4 @ (x - 1, y)
		if((x - 1 >= 0 && cellGrid[x - 1][y] == WALL) || (x - 1 < 0)) {
			numWallNeighbors += 1;
		}
		// neighbor 5 @ (x + 1, y)
		if((x + 1 < width && cellGrid[x + 1][y] == WALL) || (x + 1 >= width)) {
			numWallNeighbors += 1;
		}
		// neighbor 6 @ (x - 1, y + 1)
		if((x - 1 >= 0 && y + 1 < height && cellGrid[x - 1][y + 1] == WALL) || (x - 1 < 0 && y + 1 >= height)) {
			numWallNeighbors += 1;
		}
		// neighbor 7 @ (y + 1)
		if((y + 1 < height && cellGrid[x][y + 1] == WALL) || (y + 1 >= height)) {
			numWallNeighbors += 1;
		}
		// neighbor 8 @ (x + 1, y + 1)
		if((x + 1 < width && y + 1 < height && cellGrid[x + 1][y + 1] == WALL) || (x + 1 >= width && y + 1 >= height)) {
			numWallNeighbors += 1;
		}
		return numWallNeighbors;
	}

	/*
	 * Determines whether a point is on the border of the cell grid. Returns true if on border, false if not on border.
	 * If a cell is on the border, at least 1 of the following is true: x = 0, y = 0, x = width, x = height.
	 * Point coordOfCell must be within the boundaries of the cell grid (i.e. 0 <= x < width, 0 <= y < height)
	 */
	public boolean isOnBorder(Point coordOfCell) {
		return coordOfCell.x == 0 || coordOfCell.y == 0 || coordOfCell.x == width - 1 || coordOfCell.y == height - 1;
	}
	
	/*
	 * Fills in all caverns in the cell grid with walls, except for the biggest one.
	 * If no caverns exist in the cell grid, nothing happens.
	 */
	public void cullSmallerCaverns() {
		ArrayList<Point> biggestCavern = findBiggestCavern();
		if(biggestCavern != null) {
			// fill in cell grid with walls
			for(int x = 0; x < width; x++){
				for(int y = 0; y < height; y++){
					cellGrid[x][y] = WALL;
				}
			}
			// fill in points of biggest cavern with passageways
			for(Point point : biggestCavern) {
				int x = point.x;
				int y = point.y;
				cellGrid[x][y] = PASSAGE;
			}
			// now only the biggest cavern will remain in the cell grid
		}
	}
	
	/*
	 * Determines the area (total number of passageways) of the cave system in the cell grid.
	 */
	public int findArea() {
		int area = 0;
		ArrayList<ArrayList<Point>> allCaverns = findAllCaverns();
		for(ArrayList<Point> cavern : allCaverns) {
			area += cavern.size();
		}
		return area;
	}
	
	/*
	 * Determines whether the area of the cave system is greater than or equal to the desired area.
	 * Returns true if big enough, false if not big enough.
	 */
	public boolean isBigEnough(int desiredArea) {
		return findArea() >= desiredArea;
	}
	
	/*
	 * Finds all caverns in the cell grid. Returns a list of caverns (represented by lists of points).
	 * If there are no caverns, then the list will be empty.
	 */
	public ArrayList<ArrayList<Point>> findAllCaverns() {
		ArrayList<Point> flooded = new ArrayList<Point>(); // list of points already flooded by the flood fill algorithm (points in found caverns)
		ArrayList<ArrayList<Point>> caverns = new ArrayList<ArrayList<Point>>(); // list of found caverns
		// loop through every point in the cell grid
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				// if cell at (x, y) is a passage, and it has not already been flooded (i.e. is not in a cavern that's already been found) then find the cavern it's connected to
				if(cellGrid[x][y] == PASSAGE && !flooded.contains(new Point(x, y))) {
					// find cavern at (x, y) 
					ArrayList<Point> cavern = findCavernAt(new Point(x, y));
					// if there is a cavern at (x, y), then update flooded list and add to caverns list
					if(cavern != null) {
						flooded.addAll(cavern);
						caverns.add(cavern);
					}
				}
			}
		}
		return caverns;
	}
	
	/*
	 * Finds cavern at Point initialPoint and return list of all points within it.
	 * If there is no cave at Point initialPoint, then the value of null is returned.
	 * Point initialPoint must be within the boundaries of the cell grid (i.e. 0 <= x < width, 0 <= y < height)
	 */
	public ArrayList<Point> findCavernAt(Point initialPoint) {
		// Flood Fill Algorithm
		// 1. create 2 empty lists (toBeFlooded: a list of points about to be flooded; flooded: points that have been flooded)
		// 2. add initialPoint to toBeFlooded list
		// 3. get the point at index 0 in toBeFlooded list
		// 4. find the 4 non-flooded points surrounding point (point.y - 1, point.x - 1, point.x + 1, point.y + 1) and add them to toBeFlooded list (if they are non-flooded passageways)
		// 5. add point to flooded list
		// 6. remove point from toBeFlooded list
		// 7. repeat step 3
		ArrayList<Point> toBeFlooded = new ArrayList<Point>();
		ArrayList<Point> flooded = new ArrayList<Point>();
		toBeFlooded.add(initialPoint);
		while(!toBeFlooded.isEmpty()){
			int x = toBeFlooded.get(0).x;
			int y = toBeFlooded.get(0).y;
			if(y - 1 >= 0 && !flooded.contains(new Point(x, y - 1)) && !toBeFlooded.contains(new Point(x, y - 1)) && cellGrid[x][y - 1] == PASSAGE) {
				toBeFlooded.add(new Point(x, y - 1));
			}
			if(x - 1 >= 0 && !flooded.contains(new Point(x - 1, y)) && !toBeFlooded.contains(new Point(x - 1, y))&& cellGrid[x - 1][y] == PASSAGE) {
				toBeFlooded.add(new Point(x - 1, y));
			}
			if(x + 1 < width && !flooded.contains(new Point(x + 1, y)) && !toBeFlooded.contains(new Point(x + 1, y)) && cellGrid[x + 1][y] == PASSAGE) {
				toBeFlooded.add(new Point(x + 1, y));
			}
			if(y + 1 < height && !flooded.contains(new Point(x, y + 1)) && !toBeFlooded.contains(new Point(x, y + 1)) && cellGrid[x][y + 1] == PASSAGE) {
				toBeFlooded.add(new Point(x, y + 1));
			}
			flooded.add(new Point(toBeFlooded.get(0).x, toBeFlooded.get(0).y));
			toBeFlooded.remove(0);
		}
		// if there is no cavern at initialPoint, then set flooded to null, as no cave exists here
		if(flooded.size() == 0) {
			flooded = null;
		}
		return flooded;
	}
	
	/*
	 * Finds the biggest cavern (cavern with biggest area, i.e. the most passageways) and return list of all points within it.
	 * If two caverns are both the biggest and have equal area, then only one of them is returned.
	 * If there are no caverns in the cell grid, then it returns null.
	 */
	public ArrayList<Point> findBiggestCavern(){
		ArrayList<ArrayList<Point>> caverns = findAllCaverns();
		ArrayList<Point> biggestCavern = new ArrayList<Point>();
		// find biggest cavern from list of caverns (if two caverns are both the biggest and have equal area, then only one of them is found)
		for(ArrayList<Point> cavern : caverns) {
			if(cavern.size() > biggestCavern.size()) {
				biggestCavern = cavern;
			}
		}
		// if no cavern found, set biggestCavern to null, as there is no biggest cavern (meaning there are no caverns at all)
		if(biggestCavern.size() == 0) {
			biggestCavern = null;
		}
		return biggestCavern;
	}
	
	/*
	 * Returns width of cell grid in number of cells.
	 */
	public int getWidth() {
		return width;
	}
	
	/*
	 * Returns height of cell grid in number of cells.
	 */
	public int getHeight() {
		return height;
	}
			
}
