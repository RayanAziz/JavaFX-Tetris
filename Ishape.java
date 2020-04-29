package tetris;
import tetris.Square;
import tetris.Shape;

import javafx.scene.paint.Color;

public class Ishape extends Shape {

	private enum Rotation {
		HORIZONTAL, VERTICAL
	}

	private Rotation rotation;

	public Ishape(int x, int y, int squarePixelSize) {
		super(x, y, squarePixelSize, Color.rgb(26, 225, 234));
		rotation = Rotation.HORIZONTAL;

		squares[0] = new Square(-1, 0, squarePixelSize, color);
		squares[1] = new Square(0, 0, squarePixelSize, color);
		squares[2] = new Square(1, 0, squarePixelSize, color);
		squares[3] = new Square(2, 0, squarePixelSize, color);
	}

	@Override
	public void rotate() {
		if (rotation.equals(Rotation.HORIZONTAL)) {
			rotation = Rotation.VERTICAL;
			squares[0].updateLocation(0, -1);
			squares[1].updateLocation(0, 0);
			squares[2].updateLocation(0, 1);
			squares[3].updateLocation(0, 2);
		}
		else {
			rotation = Rotation.HORIZONTAL;
			squares[0].updateLocation(-1, 0);
			squares[1].updateLocation(0, 0);
			squares[2].updateLocation(1, 0);
			squares[3].updateLocation(2, 0);
		}
	}
	
	@Override
	public void rotateBack(){
		rotate();
	}
}