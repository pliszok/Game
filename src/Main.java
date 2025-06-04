public class Main {

    public static void main(String[] args) {
        Maze maze = new Maze(Level.loadLevel(1));

        System.out.println(maze);
    }
}
