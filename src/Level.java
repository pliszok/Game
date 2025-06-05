public class Level {

    public static int[][] loadLevel(int level) {
        switch (level) {
            case 1:
                return new int[][]{
                        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                        {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                        {1, 0, 1, 1, 1, 1, 1, 1, 0, 1},
                        {1, 0, 1, 0, 0, 0, 0, 1, 0, 1},
                        {1, 0, 1, 0, 0, 0, 0, 1, 0, 1},
                        {1, 0, 1, 0, 1, 1, 0, 1, 0, 1},
                        {1, 0, 0, 0, 1, 0, 0, 1, 0, 1},
                        {1, 0, 1, 1, 1, 1, 0, 1, 0, 1},
                        {1, 0, 0, 1, 0, 0, 0, 1, 0, 1},
                        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                };
            default:
                throw new IllegalArgumentException("Level " + level + " does not exist.");
        }
    }
}
