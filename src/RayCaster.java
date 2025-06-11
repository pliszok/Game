public class RayCaster {

    private final Maze maze;

    public RayCaster(Maze maze){
        this.maze = maze;
    }

    public float [] castRayDistances (float x,float y, float angle){
        float [] distances = new float[Settings.NUM_RAYS];
        float fov = (float) Math.toRadians(Settings.FOV);

        for ( int i = 0; i < Settings.NUM_RAYS; i++){
            float rayAngle = angle - fov/2 + fov * i/Settings.NUM_RAYS;
            float dist = castSingleRay(x, y, rayAngle);
            distances[i] = dist;
        }
        return distances;
    }

    public float castSingleRay(float x, float y, float angle){
        float dx = (float) Math.cos(angle);
        float dy = (float) Math.sin(angle);
        float stepSize = 1f;
        float distance = 0;

        while (distance < Settings.VISIBILITY){
            float checkX = x + dx * distance;
            float checkY = y + dy * distance;
            if(maze.isWall(checkX, checkY)) break;
            distance += stepSize;
        }
        return distance;
    }
}
