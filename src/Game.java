import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.image.BufferStrategy;
import java.util.LinkedList;

public class Game implements Runnable, MouseMotionListener, KeyListener {

    private Canvas canvas;
    private int mouseX = 0, mouseY = 0;
    private final Maze maze;
    private final RayCaster rayCaster;
    private final int cellSize;
    private boolean upPressed, downPressed, rightPressed, leftPressed;
    private final Player player = new Player(100,100,0);


    public Game() {
        Maze mazeLevel1 = new Maze(Level.loadLevel(1));
        this.maze = mazeLevel1;

        int gridCols = mazeLevel1.getColCount();
        int gridRows = mazeLevel1.getRowCount();
        cellSize = mazeLevel1.getCellSize();

        rayCaster = new RayCaster(mazeLevel1);

        setupWindow();
        new Thread(this).start();
    }

    private void setupWindow(){
        JFrame frame = new JFrame("Game - Level 1");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(canvas = new Canvas());
        canvas.addMouseMotionListener(this);
        canvas.addKeyListener(this);
        canvas.setFocusable(true);
        canvas.requestFocus();
        frame.setSize(Settings.WINDOW_WIDTH, Settings.WINDOW_HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    @Override
    public void run(){
        while (true){
            updatePlayer();
            render();
            try {
                Thread.sleep(Settings.FRAME_DELAY);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    private  void updatePlayer(){
        if(leftPressed) player.angle -= player.rotSpeed;
        if(rightPressed) player.angle += player.rotSpeed;

        float dx = (float)Math.cos(player.angle) * player.moveSpeed;
        float dy = (float)Math.sin(player.angle) * player.moveSpeed;

        if(upPressed){
            float newX = player.x + dx;
            float newY = player.y + dy;
            if(!maze.isWall(newX, newY)){
                player.x = newX;
                player.y = newY;
            }
        }

        if(downPressed){
            float newX = player.x - dx;
            float newY = player.y - dy;
            if(!maze.isWall(newX, newY)){
                player.x = newX;
                player.y = newY;
            }
        }



    }

    private void render(){
        BufferStrategy bs = canvas.getBufferStrategy();
        if (bs==null){
            canvas.createBufferStrategy(2); //creates off screen buffer and swaps it
            return;
        }

        Graphics g = bs.getDrawGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0,0, Settings.WINDOW_WIDTH, Settings.WINDOW_HEIGHT);

        drawMaze(g);
        drawRays(g);

        g.dispose();
        bs.show();
    }

    private void drawMaze(Graphics g){
        for(int row = 0; row < maze.getRowCount(); row++){
            for(int col = 0; col < maze.getColCount(); col++){
                if(maze.getCell(row,col) == 1){
                    g.setColor(Color.GRAY);
                }
                else {
                    g.setColor(Color.WHITE);
                }
                g.fillRect(col*cellSize, row*cellSize, cellSize, cellSize);
            }
        }
    }

    private void drawRays(Graphics g){
        LinkedList<Line2D.Float> rays = rayCaster.castRays(mouseX, mouseY);
        g.setColor(Color.GREEN);
        for (Line2D.Float ray : rays){
            g.drawLine((int) ray.x1, (int) ray.y1, (int) ray.x2, (int) ray.y2);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e){
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseDragged(MouseEvent e){
        mouseMoved(e);
    }

    @Override
    public void keyPressed(KeyEvent e){
        switch (e.getKeyCode()){
            case KeyEvent.VK_UP -> upPressed = true;
            case KeyEvent.VK_DOWN -> downPressed = true;
            case KeyEvent.VK_LEFT -> leftPressed = true;
            case KeyEvent.VK_RIGHT -> rightPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e){
        switch (e.getKeyCode()){
            case KeyEvent.VK_UP -> upPressed = false;
            case KeyEvent.VK_DOWN -> downPressed = false;
            case KeyEvent.VK_LEFT -> leftPressed = false;
            case KeyEvent.VK_RIGHT -> rightPressed = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e){};



}
