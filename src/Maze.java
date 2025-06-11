public class Maze {

    private final int[][] grid;

    public Maze(int [][] grid) {
        this.grid = grid;
    }

     public int getCell(int row, int col){
        return grid[row][col];
     }
     public int getRowCount(){
        return grid.length;
     }
     public int getColCount(){
        return grid[0].length;
     }
     public boolean isWall(float x, float y){
        int col = (int) x/getCellSize();
        int row = (int) y/getCellSize();
        return getCell(row,col) == 1;
     }
     public int getCellSize(){
        return Math.min(Settings.WINDOW_WIDTH/getColCount(), Settings.WINDOW_HEIGHT/getRowCount());
     }
}
