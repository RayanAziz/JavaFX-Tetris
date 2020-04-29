/*
 * This JavaFX program is a Tetris game.
 * It utilizes panes to construct the GUI and a canvas to draw and handle
 * the shapes. It has a total of 11 classes: this main class that does most
 * of the work, 7 shape classes for each shape that have drawing and
 * rotating instructions in methods, all of which inherit an abstract class
 * that has general and abstract methods for all of them. Then there's a
 * class for handling individual squares (pieces) of each shape. Finally,
 * the Grid class is responsible for the grid and its related operations like
 * registering shapes and clearing completed lines.
 * Some features are added such as pause/resume buttons and score/high score
 * counter. The game was tested many times to ensure it's bug-free.
 * Programmed as a final project for a college course by:
 * Rayan Aziz
 * May, 2018
 */
 
package tetris;
import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
 
public class Tetris extends Application {
 
    private static final int GRID_PIXEL_SIZE = 32;
    private static final int GAME_AREA_WIDTH = GRID_PIXEL_SIZE * 10; //px
    private static final int GAME_AREA_HEIGHT = GRID_PIXEL_SIZE * 20; //px
    private static final int WINDOW_WIDTH = (int) (GAME_AREA_WIDTH * 1.5); //px
    private static final int WINDOW_HEIGHT = (int) (GAME_AREA_HEIGHT * 1.1); //px
    private static final int GAME_AREA_X = 10; //px
    private static final int GAME_AREA_Y = 10; //px
    private static final int TIMER = 500; //ms
    private int highScore = 0;
    private int bonus = 0;
    private boolean isPaused = false;
    private Canvas canvas;
    private GraphicsContext graphicsContext;
    private Timeline animation;
    private Grid grid;
    private Shape activeShape;
	Random random = new Random();
	private int ran; // random variable
	private int NXT; // just a variable where can avoid ran to become zero
	private boolean ranEN = true; //enable random to happen .. enable it from the start to include the first shape
	//private boolean R = false;	// control the random
	Pane nextSh = new Pane(); // pane where all the next shape squares will be at
	
    @Override
    public void start(Stage primaryStage){
        Pane mainPane = new Pane();
        mainPane.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        mainPane.setStyle("-fx-background-color: #141d26;");
        Pane gamePane = new Pane();
        gamePane.setPrefSize(GAME_AREA_WIDTH+20, GAME_AREA_HEIGHT+20);
        gamePane.setLayoutX(GAME_AREA_X);
        gamePane.setLayoutY(GAME_AREA_X);
        gamePane.setStyle("-fx-border-color: #39536c; -fx-border-width: 10px; -fx-background-color: #ffffff;");
        canvas = new Canvas(GAME_AREA_WIDTH+10, GAME_AREA_HEIGHT+10);
        graphicsContext = canvas.getGraphicsContext2D();
        gamePane.getChildren().add(canvas);
        // next shape pane and area for shape
        VBox nextShapePane = new VBox(20);
        nextShapePane.setPrefSize(WINDOW_WIDTH/3.75, WINDOW_HEIGHT*13/44);
        nextShapePane.setAlignment(Pos.CENTER);
        nextShapePane.setLayoutX(360.55);////(WINDOW_WIDTH/1.35)
        nextShapePane.setLayoutY(WINDOW_HEIGHT/14.6);
        nextShapePane.setStyle("-fx-border-color: white; -fx-border-width: 2px;");
        Text nextShapeText = new Text("Next");
        nextShapeText.setFill(Color.WHITE);
        nextShapeText.setFont(Font.font("Courier New", FontWeight.BOLD, FontPosture.REGULAR, 16));
        //nextShapePane.getChildren().add(nextShapeText);
        nextShapePane.getChildren().add(nextSh);
        
        // score pane and textfield
        VBox scorePane = new VBox(20);
        scorePane.setPrefSize(WINDOW_WIDTH/3.75, WINDOW_HEIGHT/8.8);
        scorePane.setSpacing(5);
        scorePane.setAlignment(Pos.CENTER);
        scorePane.setLayoutX(WINDOW_WIDTH/1.35);
        scorePane.setLayoutY(WINDOW_HEIGHT/2.2);
        Text scoreText = new Text("Score");
        scoreText.setFont(Font.font("Courier New", FontWeight.BOLD, FontPosture.REGULAR, 26));
        scoreText.setFill(Color.WHITE);
        TextField score = new TextField();
        score.setFont(new Font("Courier New", 20));
        score.setStyle("-fx-border-color: #39536c; -fx-border-width: 5px; -fx-font-weight: bold");
        score.setText("0");
        score.setDisable(true);
        score.setMaxWidth(100);
        score.setAlignment(Pos.CENTER);
        scorePane.getChildren().addAll(scoreText, score);
       
        // buttons panes and buttons
        StackPane pausePane = new StackPane();
        pausePane.setPrefSize(WINDOW_WIDTH/3.75,WINDOW_HEIGHT/8.8);
        pausePane.setLayoutX(360.55);
        pausePane.setLayoutY(WINDOW_HEIGHT-260);
        Button pause = new Button ("Pause");
        pause.setFont(Font.font("Courier New",FontWeight.NORMAL,18));
        pause.setPrefSize(140, 36);
        pause.setStyle("-fx-padding: 8 15 15 15; -fx-background-insets: 0,0 0 5 0, 0 0 6 0, 0 0 7 0; -fx-background-radius: 8; -fx-background-color:  "
                + "linear-gradient(from 0% 93% to 0% 100%, #a34313 0%, #903b12 100%), #9d4024, #d86e3a, radial-gradient(center 50% 50%, radius 100%,"
                + "#d86e3a, #c54e2c); -fx-effect: dropshadow(gaussian , rgba(0,0,0,0.75) , 4,0,0,1 ); -fx-font-weight: bold; -fx-font-size: 1.2em;"
                + "-fx-text-fill:white;");
        pause.setOnMousePressed(e -> pause.setStyle("-fx-background-color: linear-gradient(from 0% 93% to 0% 100%, #a34313 0%, #903b12 100%),#9d4024,"
                + "#d86e3a,radial-gradient(center 50% 50%, radius 100%, #ea7f4b, #c54e2c); -fx-text-fill:white;-fx-font-size: 1.3em;"));
        pause.setOnMouseReleased(e -> pause.setStyle("-fx-padding: 8 15 15 15; -fx-background-insets: 0,0 0 5 0, 0 0 6 0, 0 0 7 0; -fx-background-radius: 8; -fx-background-color:  "
                + "linear-gradient(from 0% 93% to 0% 100%, #a34313 0%, #903b12 100%), #9d4024, #d86e3a, radial-gradient(center 50% 50%, radius 100%,"
                + "#d86e3a, #c54e2c); -fx-effect: dropshadow(gaussian , rgba(0,0,0,0.75) , 4,0,0,1 ); -fx-font-weight: bold; -fx-font-size: 1.2em;"
                + "-fx-text-fill:white; -fx-padding: 10 15 13 15; -fx-background-insets: 2 0 0 0,2 0 3 0, 2 0 4 0, 2 0 5 0;"));
        pause.setOnAction(e ->{
            isPaused = true;
            animation.stop();}
                );
        pausePane.getChildren().add(pause);
       
        StackPane resumePane = new StackPane();
        resumePane.setPrefSize(WINDOW_WIDTH/3.75,WINDOW_HEIGHT/8.8);
        resumePane.setLayoutX(360.55);
        resumePane.setLayoutY(WINDOW_HEIGHT-200);
        Button resume = new Button ("Resume");
        resume.setFont(Font.font("Courier New",FontWeight.NORMAL,18));
        resume.setPrefSize(140, 36);
        resume.setStyle("-fx-padding: 8 15 15 15; -fx-background-insets: 0,0 0 5 0, 0 0 6 0, 0 0 7 0; -fx-background-radius: 8; -fx-background-color:  "
                + "linear-gradient(from 0% 93% to 0% 100%, #a34313 0%, #903b12 100%), #9d4024, #d86e3a, radial-gradient(center 50% 50%, radius 100%,"
                + "#d86e3a, #c54e2c); -fx-effect: dropshadow(gaussian , rgba(0,0,0,0.75) , 4,0,0,1 ); -fx-font-weight: bold; -fx-font-size: 1.2em;"
                + "-fx-text-fill:white;");
        resume.setOnMousePressed(e -> resume.setStyle("-fx-background-color: linear-gradient(from 0% 93% to 0% 100%, #a34313 0%, #903b12 100%),#9d4024,"
                + "#d86e3a,radial-gradient(center 50% 50%, radius 100%, #ea7f4b, #c54e2c); -fx-text-fill:white;-fx-font-size: 1.3em;"));
        resume.setOnMouseReleased(e -> resume.setStyle("-fx-padding: 8 15 15 15; -fx-background-insets: 0,0 0 5 0, 0 0 6 0, 0 0 7 0; -fx-background-radius: 8; -fx-background-color:  "
                + "linear-gradient(from 0% 93% to 0% 100%, #a34313 0%, #903b12 100%), #9d4024, #d86e3a, radial-gradient(center 50% 50%, radius 100%,"
                + "#d86e3a, #c54e2c); -fx-effect: dropshadow(gaussian , rgba(0,0,0,0.75) , 4,0,0,1 ); -fx-font-weight: bold; -fx-font-size: 1.2em;"
                + "-fx-text-fill:white; -fx-padding: 10 15 13 15; -fx-background-insets: 2 0 0 0,2 0 3 0, 2 0 4 0, 2 0 5 0;"));
        resume.setOnAction(e ->{
            isPaused = false;
            animation.play();}
                );
        resumePane.getChildren().add(resume);
       
        StackPane restartPane = new StackPane();
        restartPane.setPrefSize(WINDOW_WIDTH/3.75,WINDOW_HEIGHT/8.8);
        restartPane.setLayoutX(360.55);
        restartPane.setLayoutY(WINDOW_HEIGHT-140);
        Button restart = new Button ("Restart Game");
        restart.setFont(Font.font("Courier New",FontWeight.NORMAL,18));
        restart.setPrefSize(140, 36);
        restart.setStyle("-fx-padding: 8 15 15 15; -fx-background-insets: 0,0 0 5 0, 0 0 6 0, 0 0 7 0; -fx-background-radius: 8; -fx-background-color:  "
                + "linear-gradient(from 0% 93% to 0% 100%, #a34313 0%, #903b12 100%), #9d4024, #d86e3a, radial-gradient(center 50% 50%, radius 100%,"
                + "#d86e3a, #c54e2c); -fx-effect: dropshadow(gaussian , rgba(0,0,0,0.75) , 4,0,0,1 ); -fx-font-weight: bold; -fx-font-size: 1em;"
                + "-fx-text-fill:white;");
        restart.setOnMousePressed(e -> restart.setStyle("-fx-background-color: linear-gradient(from 0% 93% to 0% 100%, #a34313 0%, #903b12 100%),#9d4024,"
                + "#d86e3a,radial-gradient(center 50% 50%, radius 100%, #ea7f4b, #c54e2c); -fx-text-fill:white;-fx-font-size: 1em;"));
        restart.setOnMouseReleased(e -> restart.setStyle("-fx-padding: 8 15 15 15; -fx-background-insets: 0,0 0 5 0, 0 0 6 0, 0 0 7 0; -fx-background-radius: 8; -fx-background-color:  "
                + "linear-gradient(from 0% 93% to 0% 100%, #a34313 0%, #903b12 100%), #9d4024, #d86e3a, radial-gradient(center 50% 50%, radius 100%,"
                + "#d86e3a, #c54e2c); -fx-effect: dropshadow(gaussian , rgba(0,0,0,0.75) , 4,0,0,1 ); -fx-font-weight: bold; -fx-font-size: 1em;"
                + "-fx-text-fill:white; -fx-padding: 10 15 13 15; -fx-background-insets: 2 0 0 0,2 0 3 0, 2 0 4 0, 2 0 5 0;"));
        restart.setOnAction(e ->{
            isPaused = false;
            restartGame();}
                );
        restartPane.getChildren().add(restart);
       
        // instructions pane and text
        StackPane iPane = new StackPane();
        iPane.setPrefSize(WINDOW_WIDTH*61/60, WINDOW_HEIGHT*3/44);
        iPane.setLayoutY(WINDOW_HEIGHT*0.95);
        FlowPane iPane2 = new FlowPane();
        iPane2.setAlignment(Pos.CENTER);
        Text moveLeftText = new Text("Move Left <- ");    
        Text moveRightText = new Text("-> Move Right");
        Text divider = new Text(" | ");
        Text rotateText = new Text("Rotate ^ ");
        Text dropText = new Text("v Drop");
 
        divider.setFont(Font.font("Courier New", FontWeight.BOLD, FontPosture.REGULAR, 17));
        divider.setFill(Color.WHITE);
        moveLeftText.setFont(Font.font("Courier New", FontWeight.BOLD, FontPosture.REGULAR, 17));
        moveLeftText.setFill(Color.WHITE);
        moveRightText.setFont(Font.font("Courier New", FontWeight.BOLD, FontPosture.REGULAR, 17));
        moveRightText.setFill(Color.WHITE);
        rotateText.setFont(Font.font("Courier New", FontWeight.BOLD, FontPosture.REGULAR, 17));
        rotateText.setFill(Color.WHITE);
        dropText.setFont(Font.font("Courier New", FontWeight.BOLD, FontPosture.REGULAR, 17));
        dropText.setFill(Color.WHITE);
        iPane2.getChildren().addAll(moveLeftText,moveRightText,divider,
                rotateText,dropText);
        iPane.getChildren().add(iPane2);
        mainPane.getChildren().addAll(gamePane, nextShapePane, scorePane, restartPane, pausePane, resumePane, iPane);
 
        Scene scene = new Scene(mainPane, 488.55, WINDOW_HEIGHT); //WINDOW_WIDTH
        primaryStage.setTitle("JavaFX Tetris");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        scene.setOnKeyPressed((e) -> {
            // if statement to disable key inputs when paused
            if (!isPaused){
                if (e.getCode() == KeyCode.UP){
                    // color text red when pressed
                    rotateText.setFill(Color.RED);
                    activeShape.rotate();
                    // to avoid a glitch specific to I shape where it goes out of bounds
                    for (int i=0; i<activeShape.getSquares().length; i++){
                        if (activeShape.getSquares()[i].getX()<0  && activeShape.getX() == 9){
                            activeShape.moveLeft();
                        }
                    }
                    // to avoid a glitch that makes the shape go out of bounds or collide when rotating
                    if((grid.isBottom(activeShape))||(grid.isShapeCollision(activeShape))){
                        activeShape.rotateBack();
                    }
 
                    // to avoid a glitch that makes the shape move out of bounds
                    // when both up and right/left are pressed together
                    if (grid.isAtEdge(activeShape)){
                        activeShape.moveLeft();
                        if (grid.isAtEdge(activeShape)){
                            activeShape.moveRight();
                            activeShape.moveRight();
                        }
                    }
                    drawGrid();
                    drawGridSquares();
                    drawActiveShape();
                }
                else if (e.getCode() == KeyCode.DOWN){
                    // give bonus score for playing risky
                    bonus += 1;
                    // color text red when pressed
                    dropText.setFill(Color.RED);
                    activeShape.moveDown();
                    if ((grid.isAtEdge(activeShape))||(grid.isBottom(activeShape))||
                            (grid.isShapeCollision(activeShape))){
                        activeShape.moveUp();
                    }
                    drawGrid();
                    drawGridSquares();
                    drawActiveShape();
                }
                else if (e.getCode() == KeyCode.LEFT){
                    // color text red when pressed
                    moveLeftText.setFill(Color.RED);
                    // shape can move out of bounds or collide with other shapes
                    activeShape.moveLeft();
                    // this will make the shape move 1 step in the opposite direction if
                    // it moved out of bounds or collided with other shapes
                    if (grid.isAtEdge(activeShape)){
                        activeShape.moveRight();
                    }
                    if (grid.isShapeCollision(activeShape)){
                        activeShape.moveRight();
                    }
                    drawGrid();
                    drawGridSquares();
                    drawActiveShape();
                }
                else if (e.getCode() == KeyCode.RIGHT){
                    // color text red when pressed
                    moveRightText.setFill(Color.RED);
                    // shape can move out of bounds or collide with other shapes
                    activeShape.moveRight();
                    // this will make the shape move 1 step in the opposite direction if
                    // it moved out of bounds or collided with other shapes
                    if (grid.isAtEdge(activeShape)){
                        activeShape.moveLeft();
                    }
                    if (grid.isShapeCollision(activeShape)){
                        activeShape.moveLeft();
                    }
                    drawGrid();
                    drawGridSquares();
                    drawActiveShape();;
                }
            }
        });
        // to recolor the instructions white after the key is released
        scene.setOnKeyReleased((e) -> {
            if (e.getCode() == KeyCode.UP){
                rotateText.setFill(Color.WHITE);
            }
            else if (e.getCode() == KeyCode.DOWN){
                grid.addBonusScore(bonus);
                bonus = 0;
                dropText.setFill(Color.WHITE);
            }
            else if (e.getCode() == KeyCode.LEFT){
                moveLeftText.setFill(Color.WHITE); 
            }
            else if (e.getCode() == KeyCode.RIGHT){
                moveRightText.setFill(Color.WHITE);
            }
        });
 
        grid = new Grid(GAME_AREA_WIDTH / GRID_PIXEL_SIZE, GAME_AREA_HEIGHT / GRID_PIXEL_SIZE, GRID_PIXEL_SIZE);
        
        ran = random.nextInt(7); // do the random one time from the start .. to show the first shape by random
        generateShape();
        drawGrid();
        drawActiveShape();
        animation = new Timeline(
                new KeyFrame(Duration.millis(TIMER), e ->{
                    animate();
                    score.setText(String.valueOf(grid.getScore()));
                }
                        ));
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
    }
    public void animate(){
        // routine process: moves the shape down, checks for collision under and if so
        // it registers the shape then generates a new one, and calls the complete line checking method
        try{
        	if(ranEN == true) // when random enabled
    			ran = random.nextInt(7);  // do the random one time
        	
            updateShape();
        }
        catch(ArrayIndexOutOfBoundsException e){
            System.out.println("Game over.");
            setGameOver();
        }
        // draws an empty grid everywhere (except where the shape is at & where shapes are registered)
        drawGrid();
        // draws the grid that contains the registered shapes
        drawGridSquares();
        // draws the active shape in the new location
        drawActiveShape();
    }
    // draws the current shape
    private void drawActiveShape(){
        for (Square square : activeShape.getSquares()){
            int x = (activeShape.getX() + square.getX()) * GRID_PIXEL_SIZE + GAME_AREA_X;
            int y = (activeShape.getY() + square.getY()) * GRID_PIXEL_SIZE + GAME_AREA_Y;
            drawSquare(x, y, square.squareSize, square.squareSize, square.color);
        }
    }
   
    // draws an empty grid
    private void drawGrid(){
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillRect(GAME_AREA_X, GAME_AREA_Y, GAME_AREA_WIDTH, GAME_AREA_HEIGHT);
        graphicsContext.setStroke(Color.GREY);
        graphicsContext.setLineWidth(0.5);
       
        // draws cell borders
        for (int x = GAME_AREA_X; x < GAME_AREA_X + GAME_AREA_WIDTH; x += GRID_PIXEL_SIZE){
            graphicsContext.strokeLine(x, GAME_AREA_Y, x, GAME_AREA_Y + GAME_AREA_HEIGHT);
        }
        for (int y = GAME_AREA_Y; y < GAME_AREA_Y + GAME_AREA_HEIGHT; y += GRID_PIXEL_SIZE){
            graphicsContext.strokeLine(GAME_AREA_X, y, GAME_AREA_X + GAME_AREA_WIDTH, y);
        }
    }
   
    // draws the grid that contains the registered shapes
    private void drawGridSquares(){
        Square[][] squares = grid.getGridBlocks();
 
        for (int x = 0; x < squares.length; x++){
            for (int y = 0; y < squares[x].length; y++){
                Square square = squares[x][y];
                if (square != null){
                    int squareX = x * GRID_PIXEL_SIZE + GAME_AREA_X;
                    int squareY = y * GRID_PIXEL_SIZE + GAME_AREA_Y;
                    drawSquare(squareX, squareY, square.squareSize, square.squareSize, square.color);
                }
            }
        }
    }
   
    // draws the squares of the shape
    private void drawSquare(int x, int y, int height, int width, Color color){
        // if statement to avoid a glitch in which a square is drawn outside the playing area
        if (y>=10){
            graphicsContext.setFill(color);
            graphicsContext.fillRect(x, y, width, height);
            graphicsContext.setStroke(Color.BLACK);
            graphicsContext.strokeRect(x, y, width, height);
        }
    }
   
    // routine process: moves the shape down, checks for collision under to register the shape
    // then generate a new one, and calls the complete line checking method
    private void updateShape(){
    	
    	////////// here creating the squares of the nextShape //////////// 
    	Rectangle r= new Rectangle(40,0,30,30);
        r.setFill(Color.YELLOW);
        r.setStroke(Color.BLACK);
        Rectangle r1= new Rectangle(30+40,0,30,30);
        r1.setFill(Color.YELLOW);
        r1.setStroke(Color.BLACK);
        Rectangle r2= new Rectangle(0+40,30,30,30);
        r2.setFill(Color.YELLOW);
        r2.setStroke(Color.BLACK);
        Rectangle r3= new Rectangle(30+40,30,30,30);
        r3.setFill(Color.YELLOW);
        r3.setStroke(Color.BLACK);
        ///////////////////////////////////
        // Ishape
        Rectangle I= new Rectangle(40,0,30,30);
        I.setFill(Color.rgb(26, 225, 234));
        I.setStroke(Color.BLACK);
        Rectangle I1= new Rectangle(0+40,30,30,30);
        I1.setFill(Color.rgb(26, 225, 234));
        I1.setStroke(Color.BLACK);
        Rectangle I2= new Rectangle(0+40,60,30,30);
        I2.setFill(Color.rgb(26, 225, 234));
        I2.setStroke(Color.BLACK);
        Rectangle I3= new Rectangle(0+40,90,30,30);
        I3.setFill(Color.rgb(26, 225, 234));
        I3.setStroke(Color.BLACK);
        ////////////////////////////////////
        // Tshape
        Rectangle T= new Rectangle(40,0,30,30);
        T.setFill(Color.rgb(138, 224, 41));
        T.setStroke(Color.BLACK);
        Rectangle T1= new Rectangle(0+40,30,30,30);
        T1.setFill(Color.rgb(138, 224, 41));
        T1.setStroke(Color.BLACK);
        Rectangle T2= new Rectangle(0+40,60,30,30);
        T2.setFill(Color.rgb(138, 224, 41));
        T2.setStroke(Color.BLACK);
        Rectangle T3= new Rectangle(30+40,30,30,30);
        T3.setFill(Color.rgb(138, 224, 41));
        T3.setStroke(Color.BLACK);
        ///////////////////////////////////////
        // Lshape
        Rectangle L= new Rectangle(40,0,30,30);
        L.setFill(Color.rgb(246, 110, 45));
        L.setStroke(Color.BLACK);
        Rectangle L1= new Rectangle(0+40,30,30,30);
        L1.setFill(Color.rgb(246, 110, 45));
        L1.setStroke(Color.BLACK);
        Rectangle L2= new Rectangle(0+40,60,40,30);
        L2.setFill(Color.rgb(246, 110, 45));
        L2.setStroke(Color.BLACK);
        Rectangle L3= new Rectangle(30+40,60,30,30);
        L3.setFill(Color.rgb(246, 110, 45));
        L3.setStroke(Color.BLACK);
        //////////////////////////////////////////
        // Jshape
        Rectangle J= new Rectangle(30+40,0,30,30);
        J.setFill(Color.rgb(221, 63, 240));
        J.setStroke(Color.BLACK);
        Rectangle J1= new Rectangle(30+40,30,30,30);
        J1.setFill(Color.rgb(221, 63, 240));
        J1.setStroke(Color.BLACK);
        Rectangle J2= new Rectangle(30+40,60,30,30);
        J2.setFill(Color.rgb(221, 63, 240));
        J2.setStroke(Color.BLACK);
        Rectangle J3= new Rectangle(0+40,60,30,30);
        J3.setFill(Color.rgb(221, 63, 240));
        J3.setStroke(Color.BLACK);
        ////////////////////////////////////////////
        // Sshape
        Rectangle S= new Rectangle(30+20,0,30,30);
        S.setFill(Color.rgb(249, 40, 124));
        S.setStroke(Color.BLACK);
        Rectangle S1= new Rectangle(60+20,0,30,30);
        S1.setFill(Color.rgb(249, 40, 124));
        S1.setStroke(Color.BLACK);
        Rectangle S2= new Rectangle(0+20,30,30,30);
        S2.setFill(Color.rgb(249, 40, 124));
        S2.setStroke(Color.BLACK);
        Rectangle S3= new Rectangle(30+20,30,30,30);
        S3.setFill(Color.rgb(249, 40, 124));
        S3.setStroke(Color.BLACK);
        /////////////////////////////////////////////
        // Zshape
        Rectangle Z= new Rectangle(0+20,0,30,30);
        Z.setFill(Color.rgb(52, 143, 240));
        Z.setStroke(Color.BLACK);
        Rectangle Z1= new Rectangle(30+20,0,30,30);
        Z1.setFill(Color.rgb(52, 143, 240));
        Z1.setStroke(Color.BLACK);
        Rectangle Z2= new Rectangle(30+20,30,30,30);
        Z2.setFill(Color.rgb(52, 143, 240));
        Z2.setStroke(Color.BLACK);
        Rectangle Z3= new Rectangle(60+20,30,30,30);
        Z3.setFill(Color.rgb(52, 143, 240));
        Z3.setStroke(Color.BLACK);
        
        NXT=ran+1; // put the ran + 1 to avoid ran to become zero
        if(ranEN == true){
		if (NXT == 1){
	        // adds all squares into the pane
			nextSh.getChildren().clear();
			nextSh.getChildren().addAll(r,r1,r2,r3);
			ranEN=false; // disable random
	        
	        }
		else if (NXT == 2){
	        // adds all squares into the pane
			nextSh.getChildren().clear();
			nextSh.getChildren().addAll(I,I1,I2,I3);
	        ranEN=false;
	        }
		else if (NXT == 3){
	        // adds all squares into the pane
			nextSh.getChildren().clear();
			nextSh.getChildren().addAll(T,T1,T2,T3);
			ranEN=false; // disable random
	        }
		else if (NXT == 4){
	        // adds all squares into the pane
			nextSh.getChildren().clear();
			nextSh.getChildren().addAll(L,L1,L2,L3);
			ranEN=false; // disable random
	        }
		else if (NXT == 5){
	        // adds all squares into the pane
			nextSh.getChildren().clear();
			nextSh.getChildren().addAll(J,J1,J2,J3);
			ranEN=false; // disable random
	        }
		else if (NXT == 6){
	        // adds all squares into the pane
			nextSh.getChildren().clear();
			nextSh.getChildren().addAll(S,S1,S2,S3);
			ranEN=false; // disable random
	        }
		else if (NXT == 7){
	        // adds all squares into the pane
			nextSh.getChildren().clear();
			nextSh.getChildren().addAll(Z,Z1,Z2,Z3);
			ranEN=false; // disable random
	        }
		}
        activeShape.moveDown();
        if (grid.isBottom(activeShape) || grid.isShapeCollision(activeShape)){
            activeShape.moveUp();
            registerShape();
            grid.checkCompleteLine();
            checkGameOver();
            generateShape();
            ranEN=true; // enable random
        }
    }
   
    // after a shape has stopped this method draws it on the grid
    private void registerShape(){
        grid.addShapeToGrid(activeShape);
    }
   
    // genertaes the next shape based on random number
    private void generateShape(){
    	
    	int randomShape =ran+1; // put the ran + 1 to avoid ran to become zero
    	
        switch (randomShape){
		case 1:
			activeShape = new Oshape(3, 0, GRID_PIXEL_SIZE);
			break;
		case 2:
			activeShape = new Ishape(4, 0, GRID_PIXEL_SIZE);
			break;
		case 3:
			activeShape = new Tshape(4, 0, GRID_PIXEL_SIZE);
			break;
		case 4:
			activeShape = new Lshape(4, 0, GRID_PIXEL_SIZE);
			break;
		case 5:
			activeShape = new Jshape(4, 0, GRID_PIXEL_SIZE);
			break;
		case 6:
			activeShape = new Sshape(4, 0, GRID_PIXEL_SIZE);
			break;
		case 7:
			activeShape = new Zshape(4, 0, GRID_PIXEL_SIZE);
			break;
		default:
			break;
		}
	}
   
    // checks if the game is over
    private void checkGameOver(){
        if (grid.isShapeCollision(activeShape)==true&&activeShape.getY()<1){
            setGameOver();
        }
    }
   
    private void setGameOver(){
        animation.stop();
        if (grid.getScore()>highScore)
            highScore = grid.getScore();
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game over!");
        alert.setHeaderText("Your score is: " + grid.getScore());
        alert.setContentText("High score is: " + highScore);
        alert.show();
    }
    // resets everything to default
    private void restartGame(){
        grid.reset();
        activeShape = new Zshape(4, -1, GRID_PIXEL_SIZE);
        drawGrid();
        drawGridSquares();
        animation.play();
        ranEN = true;
    }
   
    public static void main(String[] args){
        launch(args);
    }
}
