package tetris;
import javafx.scene.paint.Color;

// super class for all shapes
public abstract class Shape {

	protected int x;
	protected int y;
	protected int squarePixelSize;
	protected Square[] squares = new Square[4];
	protected Color color;
	
	// constructor: takes x & y coordinates, pixel size of a square and color then sets them all
	public Shape(int x, int y, int squarePixelSize, Color color) {
		this.x = x;
		// to make the shape initially outside the playing area
		this.y = y-1;
		this.squarePixelSize = squarePixelSize;
		this.color = color;
	}
	
	// abstract method with no body to rotate the shape whatever it is
	public abstract void rotate();
	public abstract void rotateBack();
	
	// returns an array of the shape's squares
	public Square[] getSquares() {
		return squares;
	}
	
	// get x location for the shape
	public int getX() {
		return x;
	}

	// get y location for the shape
	public int getY() {
		return y;
	}

	// move the shape down
	public void moveDown() {
		this.y += 1;
	}
	
	// move the shape up
	public void moveUp() {
		this.y -= 1;
	}
	
	// move the shape left
	public void moveLeft() {
		this.x -= 1;
	}
	
	// move the shape r
	public void moveRight() {
		this.x += 1;
	}

}
