package tetris;
import tetris.Square;
import tetris.Shape;

public class Grid {
	private Square[][] gridBlocks;
	private int width, height;
	private int score = 0;

	public Grid(int width, int height, int gridBlockSize) {
		this.width = width;
		this.height = height;

		gridBlocks = new Square[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				gridBlocks[x][y] = null;
			}
		}
	}
	public int getWidth(){
		return width;
	}

	public Square[][] getGridBlocks() {
		return gridBlocks;
	}

	public boolean isAtEdge(Shape shape) {
		boolean isAtEdge = false;
		for (Square square : shape.getSquares()) {
			int x = (shape.getX() + square.getX());
			if (x < 0 || x >= width) {
				isAtEdge = true;
			}
		}
		return isAtEdge;
	}

	public boolean isBottom(Shape shape) {
		boolean isBottom = false;
		for (Square square : shape.getSquares()) {
			int y = (shape.getY() + square.getY());
			if (y >= height) {
				isBottom = true;
			}
		}
		return isBottom;
	}

	public boolean isShapeCollision(Shape shape) {
		boolean isCollision = false;
		for (Square square : shape.getSquares()) {
			int x = (shape.getX() + square.getX());
			int y = (shape.getY() + square.getY());

			if (isSquareCollision(x, y)) {
				isCollision = true;
				break;
			}
		}

		return isCollision;
	}

	public boolean isSquareCollision(int x, int y) {
		boolean isContainsSquare = false;
		try {
			// checks if the square on the grid is filled or not
			if (x >= 0 && x < width) {
				if (gridBlocks[x][y] != null) {
					isContainsSquare = true;
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Shape is partially out.");
		}
		return isContainsSquare;
	}
	
	// registers a shape on the grid by passing the shape's squares 
	// parameters like coordinates and color to addSquare method
	public void addShapeToGrid(Shape shape) {
		for (Square square : shape.getSquares()) {
			int x = (shape.getX() + square.getX());
			int y = (shape.getY() + square.getY());
			addSquare(x, y, square);
		}
	}
	
	// registers a square on the grid by x,y coordinates
	public void addSquare(int x, int y, Square square) {
		gridBlocks[x][y] = square;
	}
	
	// check for completed lines and clears them
	public void checkCompleteLine() {
		for (int y = 0; y < height; y++) {
			boolean isCompletedLine = true;
			for (int x = 0; x < width; x++) {
				if (gridBlocks[x][y] == null) {
					isCompletedLine = false;
					break;
				}
			}

			if (isCompletedLine) {
				score += 100;
				for (int x = 0; x < width; x++) {
					gridBlocks[x][y] = null;
				}
				moveDown(y);
			}
		}
	}
	
	// moves all lines above the cleared line 1 step down
	private void moveDown(int deletedLine) {
		for (int y = deletedLine; y > 0; y--) {
			for (int x = 0; x < width; x++) {
				gridBlocks[x][y] = gridBlocks[x][y - 1];
			}
		}
	}
	
	// for restarting the game
	public void reset(){
		score = 0;
		gridBlocks = new Square[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				gridBlocks[x][y] = null;
			}
		}
		
	}
	
	public void addBonusScore(int bonus){
		score += bonus;
	}
	
	// no comments needed
	public int getScore(){
		return score;
	}
}
