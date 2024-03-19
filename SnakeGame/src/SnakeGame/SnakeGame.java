package SnakeGame;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class SnakeGame extends JFrame {
    private final int BOARD_WIDTH = 20;
    private final int BOARD_HEIGHT = 20;
    private final int TILE_SIZE = 20;
    private final int INITIAL_DELAY = 100;
    private final Color SNAKE_COLOR = Color.GREEN;
    private final Color FOOD_COLOR = Color.RED;

    private ArrayList<Point> snake;
    private Point food;
    private char direction;
    private boolean running;
    private Timer timer;

    public SnakeGame() {
        setTitle("Snake Game");
        setSize(BOARD_WIDTH * TILE_SIZE, BOARD_HEIGHT * TILE_SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        Board board = new Board();
        add(board);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        if (direction != 'S') direction = 'W';
                        break;
                    case KeyEvent.VK_DOWN:
                        if (direction != 'W') direction = 'S';
                        break;
                    case KeyEvent.VK_LEFT:
                        if (direction != 'D') direction = 'A';
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (direction != 'A') direction = 'D';
                        break;
                }
            }
        });

        snake = new ArrayList<>();
        snake.add(new Point(BOARD_WIDTH / 2, BOARD_HEIGHT / 2));
        direction = 'W'; // Initial direction: Up
        placeFood();

        running = true;
        timer = new Timer(INITIAL_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (running) {
                    move();
                    checkCollision();
                    repaint();
                }
            }
        });
        timer.start();
    }

    private void placeFood() {
        Random rand = new Random();
        int x, y;
        do {
            x = rand.nextInt(BOARD_WIDTH);
            y = rand.nextInt(BOARD_HEIGHT);
        } while (snake.contains(new Point(x, y)));
        food = new Point(x, y);
    }

    private void move() {
        Point head = snake.get(0);
        Point newHead = new Point(head.x, head.y);
        switch (direction) {
            case 'W':
                newHead.y--;
                break;
            case 'S':
                newHead.y++;
                break;
            case 'A':
                newHead.x--;
                break;
            case 'D':
                newHead.x++;
                break;
        }
        snake.add(0, newHead);
        if (newHead.equals(food)) {
            placeFood();
        } else {
            snake.remove(snake.size() - 1);
        }
    }

    private void checkCollision() {
        Point head = snake.get(0);
        if (head.x < 0 || head.x >= BOARD_WIDTH || head.y < 0 || head.y >= BOARD_HEIGHT || snake.subList(1, snake.size()).contains(head)) {
            running = false;
            timer.stop();
            JOptionPane.showMessageDialog(this, "Game Over!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private class Board extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(Color.BLACK);

            g.setColor(FOOD_COLOR);
            g.fillRect(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);

            g.setColor(SNAKE_COLOR);
            for (Point point : snake) {
                g.fillRect(point.x * TILE_SIZE, point.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SnakeGame().setVisible(true);
            }
        });
    }
}