import java.awt.geom.Line2D;
import java.util.LinkedList;

public class RayCaster {

    private final Maze maze;
    private final int cellSize;
    private final int maxDist = Settings.VISIBILITY;
    private final int numRays = Settings.NUM_RAYS;

    public RayCaster(Maze maze){
        this.maze = maze;
        this.cellSize = maze.getCellSize();
    }

    //List of rays from [x,y] to maxDist
    public LinkedList<Line2D.Float> castRays (int x, int y){
        LinkedList<Line2D.Float> rays = new LinkedList<>();
        int rowCount = maze.getRowCount();
        int colCount = maze.getColCount();

            for (int i = 0; i < numRays; i++){
                double angle = (Math.PI * 2) * ((double) i / numRays);  //Angle in radians (full circle 2PI)
                float minDist = maxDist;

                for (int wallRow = 0; wallRow < rowCount; wallRow++){

                    for (int wallCol = 0; wallCol < colCount; wallCol++){

                        if(maze.getCell(wallRow,wallCol) == 1){  //Checks cells for 1 = wall or 0 = space
                            float dist = checkWallCollision(x, y, angle, wallRow, wallCol);
                            if(dist < minDist && dist >0){
                                minDist = dist; //if the new collision is in shorter distance than previous - it is new distance
                            }
                        }
                    }
                }
                rays.add(new Line2D.Float(x, y, x + (float) Math.cos(angle) * minDist, y + (float) Math.sin(angle) * minDist));
            }
            return rays;
        }

    public float checkWallCollision(float x, float y, double angle, int wallRow, int wallCol){
        int wallX = wallCol * cellSize;
        int wallY = wallRow * cellSize;

        int wallRight = wallX + cellSize;
        int wallBottom = wallY + cellSize; //coordinates for cell

        float dx = (float)Math.cos(angle);
        float dy = (float)Math.sin(angle);

        float stepSize = 1.0f;
        float distance = 0;

        while(distance < maxDist){
            if(x >= wallX && x < wallRight && y >= wallY && y < wallBottom){ //checks if you are inside the wall
                return distance;
            }

            x += dx * stepSize;
            y += dy * stepSize;
            distance += stepSize;
        }
        return -1;
    }

    //should replace distance variable above??
    private float dist(float x1, float y1, float x2, float y2){
        return (float) Math.sqrt((x2 - x1)*(x2 - x1) + (y2 - y1)*(y2 - y1)); //Pythagoras theorem
    }
}
