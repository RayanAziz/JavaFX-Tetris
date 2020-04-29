package tetris;
import tetris.Square;
import tetris.Shape;
import javafx.scene.paint.Color;

public class Tshape extends Shape {

	private enum Rotation {
		UP, RIGHT, DOWN, LEFT
	}

	private Rotation rotation;

	public Tshape(int x, int y, int squarePixelSize) {
		super(x, y, squarePixelSize, Color.rgb(138, 224, 41));
		rotation = Rotation.UP;
		squares[0] = new Square(-1, 0, squarePixelSize, color);
		squares[1] = new Square(0, 0, squarePixelSize, color);
		squares[2] = new Square(1, 0, squarePixelSize, color);
		squares[3] = new Square(0, -1, squarePixelSize, color);
	}

	@Override
	public void rotate() {
		switch (rotation) {
		case UP:
			rotation = Rotation.RIGHT;
			squares[0].updateLocation(0, -1);
			squares[1].updateLocation(0, 0);
			squares[2].updateLocation(0, 1);
			squares[3].updateLocation(1, 0);
			break;
		case RIGHT:
			rotation = Rotation.DOWN;
			squares[0].updateLocation(-1, 0);
			squares[1].updateLocation(0, 0);
			squares[2].updateLocation(1, 0);
			squares[3].updateLocation(0, 1);
			break;
		case DOWN:
			rotation = Rotation.LEFT;
			squares[0].updateLocation(0, -1);
			squares[1].updateLocation(0, 0);
			squares[2].updateLocation(0, 1);
			squares[3].updateLocation(-1, 0);
			break;
		case LEFT:
			rotation = Rotation.UP;
			squares[0].updateLocation(-1, 0);
			squares[1].updateLocation(0, 0);
			squares[2].updateLocation(1, 0);
			squares[3].updateLocation(0, -1);
			break;
		default:
			break;
		}
	}
	@Override
	public void rotateBack(){
		switch (rotation) {
		case UP:
			rotation = Rotation.LEFT;
			squares[0].updateLocation(0, -1);
			squares[1].updateLocation(0, 0);
			squares[2].updateLocation(0, 1);
			squares[3].updateLocation(-1, 0);
			break;
		case RIGHT:
			rotation = Rotation.UP;
			squares[0].updateLocation(-1, 0);
			squares[1].updateLocation(0, 0);
			squares[2].updateLocation(1, 0);
			squares[3].updateLocation(0, -1);
			break;
		case DOWN:
			rotation = Rotation.RIGHT;
			squares[0].updateLocation(0, -1);
			squares[1].updateLocation(0, 0);
			squares[2].updateLocation(0, 1);
			squares[3].updateLocation(1, 0);
			break;
		case LEFT:
			rotation = Rotation.DOWN;
			squares[0].updateLocation(-1, 0);
			squares[1].updateLocation(0, 0);
			squares[2].updateLocation(1, 0);
			squares[3].updateLocation(0, 1);
			break;
		default:
			break;
		}
	}
}
