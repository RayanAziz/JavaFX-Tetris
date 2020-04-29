package tetris;
import javafx.scene.paint.Color;

public class Oshape extends Shape {

	public Oshape(int x, int y, int squarePixelSize) {
		super(x+1, y, squarePixelSize, Color.rgb(251, 234, 40));
		squares[0] = new Square(0, 0, squarePixelSize, color);
		squares[1] = new Square(1, 0, squarePixelSize, color);
		squares[2] = new Square(0, 1, squarePixelSize, color);
		squares[3] = new Square(1, 1, squarePixelSize, color);
	}
	
	// no rotations for the o shape
	@Override
	public void rotate() {
	}
	@Override
	public void rotateBack(){
	}
}
