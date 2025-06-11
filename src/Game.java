import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

public class Game implements Runnable, KeyListener {

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
        cellSize = mazeLevel1.getCellSize();
        rayCaster = new RayCaster(mazeLevel1);
        setupWindow();
        canvas.createBufferStrategy(2);
    }

    public void start(){
        new Thread(this).start();
    }

    private void setupWindow(){
        JFrame frame = new JFrame("Game - Level 1");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(canvas = new Canvas());
        canvas.addKeyListener(this);
        canvas.setFocusable(true);
        frame.setSize(Settings.WINDOW_WIDTH, Settings.WINDOW_HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        canvas.requestFocusInWindow();
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
                Thread.currentThread().interrupt();
            }
        }
    }

    private  void updatePlayer(){
        if(leftPressed) player.angle -= Settings.ROT_SPEED;
        if(rightPressed) player.angle += Settings.ROT_SPEED;

        float dx = (float)Math.cos(player.angle) * Settings.MOVE_SPEED;
        float dy = (float)Math.sin(player.angle) * Settings.MOVE_SPEED;

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
        g.setColor(Color.CYAN);
        g.fillRect(0,0, Settings.WINDOW_WIDTH, Settings.WINDOW_HEIGHT/2);
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, Settings.WINDOW_HEIGHT/2, Settings.WINDOW_WIDTH, Settings.WINDOW_HEIGHT/2);

        draw3dView(g);

        g.dispose();
        bs.show();
    }

    public void draw3dView(Graphics g){
        float[] distances = rayCaster.castRayDistances(player.x, player.y, player.angle);
        float fov = (float) Math.toRadians(Settings.FOV);
        int width = Settings.WINDOW_WIDTH;
        int height = Settings.WINDOW_HEIGHT;

        for (int i = 0; i < distances.length; i++){
            float dist = distances[i];

            // Fix fish-eye effect
            float angleOffset = -fov / 2 + (fov * i / distances.length);
            dist *= Math.cos(angleOffset);

            // Prevent zero distance
            dist = Math.max(dist, 1f);

            // Calculate wall slice height
            int lineHeight = (int) (cellSize * Settings.WALL_SCALING / dist); // scaling factor
            int startY = height / 2 - lineHeight / 2;

            // Shade based on distance
            int brightness = Math.max(0, 255 - (int)(dist * 0.1));
            g.setColor(new Color(brightness, brightness, brightness));

            // Draw vertical slice
            int sliceWidth = width / distances.length;
            int x = i * sliceWidth;
            if (i == distances.length - 1) {
                sliceWidth = width - x;
            }
            g.fillRect(i * sliceWidth, startY, sliceWidth, lineHeight);
        }
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
