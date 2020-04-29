package tetris;
import javafx.scene.paint.Color;

public class Square {

	private int x, y;
	public int squareSize;
	public Color color;

	public Square(int x, int y, int squareSize, Color color) {
		this.x = x;
		this.y = y;
		this.squareSize = squareSize;
		this.color = color;
	}

	public void updateLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
