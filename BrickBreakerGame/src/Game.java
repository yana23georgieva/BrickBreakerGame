import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Game extends JPanel implements KeyListener, ActionListener {
    private boolean play = false;
    private int score = 0;

    private int totalBricks = 85;

    private Timer timer;
    private int delay = 8;

    private int playerX = 310;

    private int ballPositionX = 120;
    private int ballPositionY = 350;
    private int ballXDir = -1;
    private int ballYDir = -2;

    private int mapRows = 4;
    private int mapCols = 12;

    private Map map;

    public Game() {
        map = new Map(mapRows, mapCols);
        totalBricks = mapRows * mapCols;
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paint(Graphics graphics) {
        //background
        graphics.setColor(Color.black);
        graphics.fillRect(1, 1, 692, 592);

        //borders
        graphics.setColor(Color.green);
        graphics.fillRect(1, 1, 3, 592);
        graphics.fillRect(1, 1, 692, 3);
        graphics.fillRect(683, 0, 3, 592);

        //the paddle
        graphics.setColor(Color.green);
        graphics.fillRect(playerX, 550, 100, 8);

        //the ball
        graphics.setColor(Color.yellow);
        graphics.fillOval(ballPositionX, ballPositionY, 20, 20);

        //map
        map.draw((Graphics2D) graphics);

        // when you won the game
        if(totalBricks <= 0) {
            play = false;
            ballXDir = 0;
            ballYDir = 0;
            graphics.setColor(Color.GREEN);
            graphics.setFont(new Font("serif",Font.BOLD, 30));
            graphics.drawString("You Won", 260,300);

            graphics.setColor(Color.GREEN);
            graphics.setFont(new Font("serif",Font.BOLD, 20));
            graphics.drawString("Press (Enter) to Restart", 230,350);
            mapRows = 5;
            mapCols = 17;
            totalBricks = mapRows * mapCols;
        }

        // when you lose the game
        if(ballPositionY > 570) {
            play = false;
            ballXDir = 0;
            ballYDir = 0;
            graphics.setColor(Color.RED);
            graphics.setFont(new Font("serif",Font.BOLD, 30));
            graphics.drawString("Game Over, Scores: " + score, 190,300);

            graphics.setColor(Color.RED);
            graphics.setFont(new Font("serif",Font.BOLD, 20));
            graphics.drawString("Press (Enter) to Restart", 230,350);
            mapRows = 3;
            mapCols = 7;
            totalBricks = mapRows * mapCols;
        }

        graphics.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if (play) {
            if (new Rectangle(ballPositionX, ballPositionY, 20, 20).
                    intersects(new Rectangle(playerX, 550, 30, 8))) {
                ballYDir = -ballYDir;
                ballXDir = -2;
            } else if(new Rectangle(ballPositionX, ballPositionY, 20, 20).
                    intersects(new Rectangle(playerX + 70, 550, 30, 8))) {
                ballYDir = -ballYDir;
                ballXDir = ballXDir + 1;
            } else if(new Rectangle(ballPositionX, ballPositionY, 20, 20).
                    intersects(new Rectangle(playerX + 30, 550, 40, 8))) {
                ballYDir = -ballYDir;
            }

            // check map collision with the ball
            A: for(int i = 0; i < map.map.length; i++) {
                for(int j =0; j < map.map[i].length; j++) {
                    if(map.map[i][j] > 0) {
                        //scores++;
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballPositionX, ballPositionY, 20, 20);
                        Rectangle brickRect = rect;

                        if(ballRect.intersects(brickRect)) {
                            map.setBrickValue(0, i, j);
                            score += 5;
                            totalBricks--;

                            // when ball hit right or left of brick
                            if(ballPositionX + 19 <= brickRect.x || ballPositionX + 1 >= brickRect.x + brickRect.width) {
                                ballXDir = -ballXDir;
                            }
                            // when ball hits top or bottom of brick
                            else {
                                ballYDir = -ballYDir;
                            }

                            break A;
                        }
                    }
                }
            }

            ballPositionX += ballXDir;
            ballPositionY += ballYDir;

            if(ballPositionX < 0) {
                ballXDir = -ballXDir;
            }
            if(ballPositionY < 0) {
                ballYDir = -ballYDir;
            }
            if(ballPositionX > 670) {
                ballXDir = -ballXDir;
            }
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (playerX >= 582) {
                playerX = 580;
            } else {
                moveRight();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (playerX < 10) {
                playerX = 3;
            } else {
                moveLeft();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if(!play) {
                play = true;
                ballPositionX = 120;
                ballPositionY = 350;
                ballYDir = -1;
                ballYDir = -2;
                playerX = 310;
                score = 0;
                totalBricks = 21;
                map = new Map(mapRows, mapCols);

                repaint();
            }
        }
    }

    public void moveRight() {
        play = true;
        playerX += 40;
    }

    public void moveLeft() {
        play = true;
        playerX -= 40;
    }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyReleased(KeyEvent e) { }
}
