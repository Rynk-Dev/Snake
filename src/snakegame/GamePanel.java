/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package snakegame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import javax.swing.JPanel;

/**
 *
 * @author USer
 */
public class GamePanel extends JPanel implements ActionListener{
    
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 90;
    int x[] = new int[GAME_UNITS];
    int y[] = new int[GAME_UNITS];
    int bodyParts = 6;
    int applesEaten;
    int topScore = 0;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean restart = false;
    boolean running = false;
    Timer timer;
    Random random;
    
    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }
    public void startGame(){
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        if (running){
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++){
                if (i == 0){
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                else {
                    g.setColor(new Color(45,180,0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
        }
        else {
            gameOver(g);
            if (restart == true){
                reset();
            }
        }
        score(g);
        topScore(g);
    }
    public void newApple(){
        appleX = random.nextInt((int)(SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }
    public void move(){
        for (int i = bodyParts; i>0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        
        switch(direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }
    public void checkApple(){
        if (x[0] == appleX && y[0] == appleY){
            bodyParts++;
            applesEaten++;
            if (applesEaten > topScore){
                topScore = applesEaten;
            }
            newApple();
        }
    }
    public void checkCollisions(){
        //check for head - body collision
        for (int i = bodyParts; i > 0; i--){
            if ((x[0] == x[i]) && (y[0] == y[i])){
                running = false;
            }
        }
        //check for head - border collision
        boolean hitLeft = x[0] < 0;
        boolean hitRight = x[0] > SCREEN_WIDTH - 1;
        boolean hitTop = y[0] < 0;
        boolean hitBottom = y[0] > SCREEN_HEIGHT - 1;
        
        if ( hitLeft || hitRight || hitTop || hitBottom ){
            running = false;
        }
    }
    
    public void score(Graphics g){
        //score screen
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
    }
    
    public void topScore(Graphics g){
        //top score screen
        g.setColor(Color.blue);
        g.setFont(new Font("Ink Free", Font.BOLD, 20));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Top Score: " + topScore, (SCREEN_WIDTH - metrics.stringWidth("Top Score: " + topScore)) / 2, g.getFont().getSize() * 4);
    }
    
    public void gameOver(Graphics g){
        
        //game over screen
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
        g.drawString("Enter to Restart", (SCREEN_WIDTH - metrics.stringWidth("Enter to Restart")) / 2, SCREEN_HEIGHT * 3 / 4);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }
    
    private void reset(){
        applesEaten = 0;
        restart = false;
        running = true;
        newApple();
        bodyParts = 6;
        direction = 'R';
        x = new int[GAME_UNITS];
        y = new int[GAME_UNITS];

    }
    
    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
                case KeyEvent.VK_ENTER:
                    restart = true;
                    break;
            }// exit switch
        }
    }
}
