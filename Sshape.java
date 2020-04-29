package tetris;
import tetris.Square;
import tetris.Shape;
import javafx.scene.paint.Color;

public class Sshape extends Shape {

	private enum Rotation {
		UP, RIGHT, DOWN, LEFT
	}

	private Rotation rotation;

	public Sshape(int x, int y, int squarePixelSize) {
		super(x, y, squarePixelSize, Color.rgb(249, 40, 124));
		rotation = Rotation.UP;
		squares[0] = new Square(0, 1, squarePixelSize, color);
		squares[1] = new Square(1, 0, squarePixelSize, color);
		squares[2] = new Square(1, 1, squarePixelSize, color);
		squares[3] = new Square(2, 0, squarePixelSize, color);
	}

	@Override
	public void rotate() {
		switch (rotation) {
		case UP:
			rotation = Rotation.RIGHT;
			squares[0].updateLocation(0, -1);
			squares[1].updateLocation(0, 0);
			squares[2].updateLocation(1, 0);
			squares[3].updateLocation(1, 1);
			break;
		case RIGHT:
			rotation = Rotation.DOWN;
			squares[0].updateLocation(0, 0);
			squares[1].updateLocation(1, -1);
			squares[2].updateLocation(1, 0);
			squares[3].updateLocation(2, -1);
			break;
		case DOWN:
			rotation = Rotation.LEFT;
			squares[0].updateLocation(1, -1);
			squares[1].updateLocation(1, 0);
			squares[2].updateLocation(2, 0);
			squares[3].updateLocation(2, 1);
			break;
		case LEFT:
			rotation = Rotation.UP;
			squares[0].updateLocation(0, 1);
			squares[1].updateLocation(1, 0);
			squares[2].updateLocation(1, 1);
			squares[3].updateLocation(2, 0);
			break;
		default:
			break;
		}
	}
	@Override
	public void rotateBack(){
		rotate();
	}
}
