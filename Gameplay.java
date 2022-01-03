import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Random;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Timer;
import javax.swing.JPanel;

public class Gameplay extends JPanel implements KeyListener, ActionListener {
    private boolean play = false;
    private int score = 0;
    
    private int totalBricks = 21;

    private Timer timer;
    private int delay = 8;

    private int playerX = 290;

    private int ballposX = 120;
    private int ballposY = 350;
    private int ballXdir = getRandomNumberForX();
    private int ballYdir = getRandomNumberForY();

    private MapGenerator map;

    public Gameplay() {
        map = new MapGenerator(3, 7);

        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paint(Graphics g) {
        //background
        g.setColor(Color.BLACK);
        g.fillRect(1, 1, 692, 592);

        //drawing map
        map.draw((Graphics2D)g, Color.WHITE);

        //borders
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, 3, 592);       
        g.fillRect(0, 0, 692, 3);       
        g.fillRect(691, 0, 3, 592);     

        //scores
        g.setColor(Color.WHITE);
        g.setFont(new Font("serif", Font.BOLD, 22));
        g.drawString("Score: " + score + "/105", 535, 30);

        //paddle
        g.setColor(Color.GREEN);
        g.fillRect(playerX, 550, 100, 8);

        if (play == false) {
            //game start message
            g.setColor(Color.YELLOW);                      
            g.setFont(new Font("serif", Font.PLAIN, 25));
            g.drawString("Press Enter/Left/Right Arrow to start the game!", 105, 350);
        } else {
            //the ball
            g.setColor(Color.YELLOW);                   
            g.fillOval(ballposX, ballposY, 20, 20);
        }

        if(totalBricks <= 0) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;

            g.setColor(Color.RED);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("You Won:)    Score: " + score, 260, 250);

            g.setColor(Color.YELLOW);
            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart!", 240, 295);

            //above score hides after winning
            g.setColor(Color.BLACK);
            g.setFont(new Font("serif", Font.BOLD, 22));
            g.drawString("Score: " + score + "/105", 535, 30);

            //brick remains hide after winning
            map.draw((Graphics2D)g, Color.BLACK);

            //paddle
            g.setColor(Color.BLACK);
            g.fillRect(playerX, 550, 100, 8);

            //game start message
            g.setColor(Color.BLACK);
            g.setFont(new Font("serif", Font.PLAIN, 25));
            g.drawString("Press Enter/Left/Right Arrow to start the game!", 105, 350);
        }

        if(ballposY > 570) {        
            play = false;
            ballXdir = 0;
            ballYdir = 0;

            g.setColor(Color.RED);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("GAME OVER!    Score: " + score, 160, 250);

            g.setColor(Color.YELLOW);
            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press(Enter) to Restart..", 240, 295);

            //above score hides when player loses a game
            g.setColor(Color.BLACK);
            g.setFont(new Font("serif", Font.BOLD, 22));
            g.drawString("Score: " + score + "/105", 535, 30);

            //brick remains hide when player loses a game
            map.draw((Graphics2D)g, Color.BLACK);

            //paddle
            g.setColor(Color.BLACK);
            g.fillRect(playerX, 550, 100, 8);

            //game start message
            g.setColor(Color.BLACK);
            g.setFont(new Font("serif", Font.PLAIN, 25));
            g.drawString("Press Enter/Left/Right Arrow to start the game!", 105, 350);
        }
        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();

        if(play) {
            if(new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) {
                ballYdir = -ballYdir;
            }
            A: for(int i=0; i<map.map.length; i++) {
                for(int j=0; j<map.map.length; j++) {
                    if(map.map[i][j] > 0) {
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
                        Rectangle brickRect = rect;

                        if(ballRect.intersects(brickRect)) {
                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;     

                            if(ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width) {
                                ballXdir = -ballXdir;
                            } else {
                                ballYdir = -ballYdir;
                            }
                            break A;
                        }
                    }
                }
            }
            ballposX += ballXdir;
            ballposY += ballYdir;

            if(ballposX < 0) {          
                ballXdir = -ballXdir;
            }
            if(ballposY < 0) {          
                ballYdir = -ballYdir;
            }
            if(ballposX > 670) {        
                ballXdir = -ballXdir;
            }
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {    
    }

    @Override
    public void keyReleased(KeyEvent e) {    
    }
    public int getRandomNumberForY() {
        Random random = new Random();
        int max = -1;
        int min = -5;
        int randomNumber = min + random.nextInt(max - min + 1);
        return randomNumber;
    }

    public int getRandomNumberForX() {
        Random random = new Random();
        int max = -1;
        int min = -3;
        int randomNumber = min + random.nextInt(max - min + 1);
        return randomNumber;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if(playerX >= 600) {
                playerX = 600;
            } else {
                moveRight();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT) {
            if(playerX < 10) {
                playerX = 10;
            } else {
                moveLeft();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_ENTER) {
            if(!play) {
                play = true;
                ballposX = 120; 
                ballposY = 350;
                ballXdir = getRandomNumberForX();
                ballYdir = getRandomNumberForY();
                playerX = 290;
                score = 0;
                totalBricks = 21;
                map = new MapGenerator(3, 7);

                repaint();
            }
        }
    }
    public void moveRight() {
        play = true;
        playerX += 20;
    }
    public void moveLeft() {
        play = true;
        playerX -= 20;
    }
}