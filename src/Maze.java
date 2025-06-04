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
     public int [][] getGrid(){
        return grid;
     }
}
