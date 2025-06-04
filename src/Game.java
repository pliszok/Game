import java.awt.*;
import java.awt.event.MouseMotionListener;

public class Game implements Runnable, MouseMotionListener {

    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 800;
    private Canvas canvas;
    private int mouseX = 0, mouseY = 0;
    private final Maze maze;
    private final RayCaster rayCaster;
    private final int cellSize;


    public Game() {
        Maze mazeLevel1 = new Maze(Level.loadLevel(1));

        int gridCols = mazeLevel1.getColCount();
        int gridRows = mazeLevel1.getRowCount();
        cellSize = Math.min(WINDOW_WIDTH/gridCols, WINDOW_HEIGHT/gridRows);

        rayCaster = new RayCaster(mazeLevel1, cellSize){

        }

    }

}
