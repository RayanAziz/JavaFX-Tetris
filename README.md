This JavaFX program is a Tetris game.
It utilizes panes to construct the GUI and a canvas to draw and handle the shapes. It has a total of 11 classes: Tetris.java, the main class that does most of the work, 7 shape classes for each shape that have drawing and rotating instructions in methods, all of which inherit an abstract class that has general and abstract methods for all of them. Then there's a class for handling individual squares (pieces) of each shape.

Finally, the Grid class is responsible for the grid and its related operations like registering shapes and clearing completed lines.

Some features are added such as pause/resume buttons and score/high score counter. The game was tested many times to ensure it's bug-free.
Programmed as a final project for a college course by:
Rayan Aziz
May, 2018
