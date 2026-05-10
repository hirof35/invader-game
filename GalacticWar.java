package galacticWar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GalacticWar extends JPanel implements ActionListener, KeyListener {
    // 定数管理
    private static final int WIDTH = 500;
    private static final int HEIGHT = 550;
    private static final int PLAYER_SPEED = 6;
    private static final int BULLET_SPEED = 8;
    private static final int ENEMY_BULLET_SPEED = 5;

    // ゲーム状態
    private boolean isGameOver = false;
    private boolean isClear = false;
    private int score = 0;
    private int lives = 3;

    // 入力管理（操作感を滑らかにするため）
    private boolean leftPressed = false;
    private boolean rightPressed = false;

    // プレイヤー
    private int playerX = 225;
    private final int playerY = 450;
    private final int playerWidth = 50;

    // オブジェクトリスト
    private ArrayList<Point> bullets = new ArrayList<>();
    private ArrayList<Point> enemyBullets = new ArrayList<>();
    private ArrayList<Rectangle> aliens = new ArrayList<>();
    private int alienSpeed = 2;

    public GalacticWar() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        setDoubleBuffered(true); // 描画の安定化

        initAliens();

        Timer timer = new Timer(16, this); // 約60FPS
        timer.start();
    }

    private void initAliens() {
        aliens.clear();
        bullets.clear();
        enemyBullets.clear();
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 8; c++) {
                aliens.add(new Rectangle(50 + c * 50, 60 + r * 40, 30, 20));
            }
        }
    }

    private void resetGame() {
        score = 0;
        lives = 3;
        isGameOver = false;
        isClear = false;
        alienSpeed = 2;
        playerX = 225;
        initAliens();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isGameOver && !isClear) {
            updatePlayer();
            updateBullets();
            updateAliens();
            updateEnemyBullets();
        }
        repaint();
    }

    private void updatePlayer() {
        if (leftPressed) playerX -= PLAYER_SPEED;
        if (rightPressed) playerX += PLAYER_SPEED;
        playerX = Math.max(0, Math.min(playerX, getWidth() - playerWidth));
    }

    private void updateBullets() {
        Iterator<Point> it = bullets.iterator();
        while (it.hasNext()) {
            Point b = it.next();
            b.y -= BULLET_SPEED;
            if (b.y < 0) {
                it.remove();
                continue;
            }

            // 当たり判定
            Rectangle bRect = new Rectangle(b.x, b.y, 5, 10);
            for (int j = 0; j < aliens.size(); j++) {
                if (bRect.intersects(aliens.get(j))) {
                    aliens.remove(j);
                    it.remove();
                    score += 100;
                    if (aliens.isEmpty()) isClear = true;
                    return; // 1つの弾で複数を壊さない
                }
            }
        }
    }

    private void updateAliens() {
        boolean hitWall = false;
        for (Rectangle a : aliens) {
            a.x += alienSpeed;
            if (a.x <= 0 || a.x >= getWidth() - a.width) hitWall = true;
            if (a.y + a.height >= playerY) isGameOver = true;
        }

        if (hitWall) {
            alienSpeed *= -1;
            for (Rectangle a : aliens) a.y += 15;
        }

        if (Math.random() < 0.02 && !aliens.isEmpty()) {
            Rectangle s = aliens.get((int) (Math.random() * aliens.size()));
            enemyBullets.add(new Point(s.x + s.width / 2, s.y + s.height));
        }
    }

    private void updateEnemyBullets() {
        Rectangle pRect = new Rectangle(playerX, playerY, playerWidth, 20);
        Iterator<Point> it = enemyBullets.iterator();
        while (it.hasNext()) {
            Point eb = it.next();
            eb.y += ENEMY_BULLET_SPEED;
            if (pRect.contains(eb)) {
                lives--;
                it.remove();
                if (lives <= 0) isGameOver = true;
                continue;
            }
            if (eb.y > getHeight()) it.remove();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 自機
        g2d.setColor(Color.GREEN);
        g2d.fillRect(playerX, playerY, playerWidth, 20);

        // 弾
        g2d.setColor(Color.YELLOW);
        for (Point b : bullets) g2d.fillRect(b.x, b.y, 5, 10);

        // 敵
        g2d.setColor(Color.RED);
        for (Rectangle a : aliens) g2d.fillRect(a.x, a.y, a.width, a.height);
        
        g2d.setColor(Color.ORANGE);
        for (Point eb : enemyBullets) g2d.fillRect(eb.x, eb.y, 5, 10);

        // UI
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Monospaced", Font.BOLD, 18));
        g2d.drawString("SCORE: " + score, 20, 30);
        g2d.drawString("LIVES: " + lives, getWidth() - 120, 30);

        if (isGameOver) drawCenterText("GAME OVER - Press R to Restart", g2d);
        if (isClear) drawCenterText("MISSION CLEAR! - Press R to Restart", g2d);
    }

    private void drawCenterText(String s, Graphics2D g2d) {
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        FontMetrics fm = g2d.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(s)) / 2;
        int y = getHeight() / 2;
        g2d.setColor(Color.BLACK); // 文字に影をつけて見やすく
        g2d.drawString(s, x + 2, y + 2);
        g2d.setColor(Color.WHITE);
        g2d.drawString(s, x, y);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) leftPressed = true;
        if (key == KeyEvent.VK_RIGHT) rightPressed = true;
        if (key == KeyEvent.VK_SPACE && !isGameOver && !isClear) {
            // 同時に出せる弾数を制限する場合などはここで判定
            bullets.add(new Point(playerX + playerWidth / 2 - 2, playerY));
        }
        if (key == KeyEvent.VK_R) resetGame();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) leftPressed = false;
        if (key == KeyEvent.VK_RIGHT) rightPressed = false;
    }

    @Override public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Java Galactic War");
        GalacticWar game = new GalacticWar();
        frame.add(game);
        frame.pack(); // JPanelのサイズに合わせる
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
