package Utilities;

import GameObjects.Ball;
import GameObjects.GameObject;
import GameObjects.Player;
import GameObjects.SpecialBalls;
import GameObjects.Wall;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

public class GameCanvas extends Canvas implements Runnable {

  private Thread thread;
  BufferStrategy bufferStrategy;
  private GameManager gameManager = new GameManager();
  public static Keyboard keyboard = new Keyboard();
  boolean paused = true;
  // two main players
  private Player player1;
  private Player player2;
  Ball ball;
  Wall topWall;
  Wall bottomWall;
  // the spawn interval for the special balls
  private int spawnInterval = 5000; // milliseconds
  private long lastSpawnTime = System.currentTimeMillis();
  SpecialBalls specialBall1 = new SpecialBalls(
    0,
    0,
    SpecialBalls.SpecialBallType.BLUE
  );
  SpecialBalls specialBall2 = new SpecialBalls(
    0,
    0,
    SpecialBalls.SpecialBallType.RED
  );
  ScoreManager scoreManager = new ScoreManager(480, 270);

  public GameCanvas() {
    this.addKeyListener(keyboard);

    player1 = new Player(10, 10);
    player2 = new Player(930, 100);
    ball = new Ball(480, 270);
    topWall = new Wall(0, 0);
    topWall.setSize(1000, 10);
    bottomWall = new Wall(0, 590);
    bottomWall.setSize(1000, 10);
    gameManager.addGameObject(topWall);
    gameManager.addGameObject(bottomWall);
    gameManager.addGameObject(ball);
    gameManager.addGameObject(player1);
    gameManager.addGameObject(player2);
    gameManager.addGameObject(scoreManager);
  }

  public void setupGame() {
    start();
    setupMenu();
  }

  JMenuBar menuBar = new JMenuBar();

  public void setupMenu() {
    JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
    if (parentFrame == null) {
      System.err.println("Error: GameCanvas is not attached to a JFrame yet.");
      return;
    }

    JMenu gameMenu = new JMenu("Game");
    JMenu settingsMenu = new JMenu("Settings");

    JMenuItem startMenuItem = new JMenuItem("Start");
    startMenuItem.addActionListener(e -> startGame());

    JMenuItem restartMenuItem = new JMenuItem("Restart");
    restartMenuItem.addActionListener(e -> Restart());

    JMenuItem quitMenuItem = new JMenuItem("Quit");
    quitMenuItem.addActionListener(e -> System.exit(0));

    JMenu speedMenuItem = new JMenu("Speed");
    JMenuItem slowMenuItem = new JMenuItem("Slow");
    slowMenuItem.addActionListener(e -> {
      ball.maxSpeedY = 2;
      ball.speedX = 2;
    });
    JMenuItem normalMenuItem = new JMenuItem("Normal");
    normalMenuItem.addActionListener(e -> {
      ball.maxSpeedY = 4;
      ball.speedX = 4;
    });
    JMenuItem fastMenuItem = new JMenuItem("Fast");
    fastMenuItem.addActionListener(e -> {
      ball.maxSpeedY = 8;
      ball.speedX = 8;
    });

    speedMenuItem.add(slowMenuItem);
    speedMenuItem.add(normalMenuItem);
    speedMenuItem.add(fastMenuItem);
    settingsMenu.add(speedMenuItem);
    gameMenu.add(startMenuItem);
    gameMenu.add(restartMenuItem);
    gameMenu.add(quitMenuItem);
    menuBar.add(gameMenu);
    menuBar.add(settingsMenu);
    parentFrame.setJMenuBar(menuBar);
  }

  private void startGame() {
    paused = false;
  }

  public void start() {
    this.requestFocus();
    this.createBufferStrategy(2);
    bufferStrategy = this.getBufferStrategy();

    thread = new Thread(this, "Game Thread");
    thread.start();

    player1.y = (getHeight() / 2) - player1.height / 2;
    player2.y = (getHeight() / 2) - player2.height / 2;
    ball.x = (getWidth() / 2) - ball.width / 2;
    ball.y = (getHeight() / 2) - ball.height / 2;
    scoreManager.x = getWidth() / 2;
    scoreManager.y = getHeight() / 4;
    topWall.setSize(getWidth(), 10);
    bottomWall.setSize(getWidth(), 10);
    topWall.y = 0;
    bottomWall.y =
      getHeight() -
      bottomWall.height -
      menuBar.getHeight() -
      (topWall.height * 2);
  }

  @Override
  public void run() {
    long lastTime = System.nanoTime();
    double delta = 0;
    final int UPS_CAP = 60;
    while (true) {
      long now = System.nanoTime();
      delta += (now - lastTime) / (double) (1000000000 / UPS_CAP);
      lastTime = now;
      while (delta >= 1) {
        tick();
        delta--;
      }
      render();
    }
  }

  private void render() {
    Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
    g.setRenderingHint(
      RenderingHints.KEY_ANTIALIASING,
      RenderingHints.VALUE_ANTIALIAS_ON
    );
    g.setColor(Color.BLACK);
    g.setFont(new Font("Arial", Font.BOLD, 42));
    g.fillRect(0, 0, getWidth(), getHeight());

    gameManager.render(g);
    drawNet(g);
    String winner = "WINNER";
    int winnerWidth = g.getFontMetrics().stringWidth(winner);
    if (scoreManager.score1 == 10) {
      paused = true;
      g.drawString(
        winner,
        (getWidth() / 2) - winnerWidth - 100,
        getHeight() / 3
      );
    }
    if (scoreManager.score2 == 10) {
      paused = true;
      g.drawString(
        winner,
        (getWidth() / 2) + winnerWidth + 60,
        getHeight() / 3
      );
    }
    g.dispose();
    bufferStrategy.show();
  }

  private void tick() {
    if (keyboard.space) {
      paused = !paused;
      keyboard.space = false;
    }
    if (!paused) {
      movePlayers();
      moveBall();

      // Check for score
      if (ball.x < 0) {
        scoreManager.score2++;
        restartGame();
      } else if (ball.x > getWidth() - ball.width) {
        scoreManager.score1++;
        restartGame();
      }
      if (scoreManager.score1 == 10 || scoreManager.score2 == 10) {
        scoreManager.score1 = 0;
        scoreManager.score2 = 0;
      }
      long currentTime = System.currentTimeMillis();
      if (currentTime - lastSpawnTime >= spawnInterval) {
        spawnSpecialBall();
        lastSpawnTime = currentTime;
      }
      checkSpecialBallsCollision();

      gameManager.tick();
    }
  }

  private void checkSpecialBallsCollision() {
    for (GameObject gameObject : gameManager.getGameObjects()) {
      if (gameObject instanceof SpecialBalls) {
        SpecialBalls specialBall = (SpecialBalls) gameObject;
        if (player1.intersects(specialBall)) {
          player1.setSize(
            player1.width,
            specialBall.type == SpecialBalls.SpecialBallType.RED
              ? player1.height - 10
              : player1.height + 10
          );
          gameManager.removeGameObject(specialBall);
        }
        if (player2.intersects(specialBall)) {
          player2.setSize(
            player2.width,
            specialBall.type == SpecialBalls.SpecialBallType.RED
              ? player2.height - 10
              : player2.height + 10
          );
          gameManager.removeGameObject(specialBall);
        }
      }
    }
  }

  public void spawnSpecialBall() {
    Random random = new Random();

    SpecialBalls.SpecialBallType initialType = random.nextBoolean()
      ? SpecialBalls.SpecialBallType.RED
      : SpecialBalls.SpecialBallType.BLUE;
    SpecialBalls specialBall1 = new SpecialBalls(
      getWidth() / 2,
      random.nextInt(getHeight() - 100) + 50,
      initialType
    );
    specialBall1.setSpeedX(-5);

    SpecialBalls specialBall2 = new SpecialBalls(
      getWidth() / 2,
      random.nextInt(getHeight() - 100) + 50,
      initialType == SpecialBalls.SpecialBallType.RED
        ? SpecialBalls.SpecialBallType.BLUE
        : SpecialBalls.SpecialBallType.RED
    );
    specialBall2.setSpeedX(5);

    gameManager.addGameObject(specialBall1);
    gameManager.addGameObject(specialBall2);
  }

  //this movess the ball depending on the collision with the players and walls,
  private void moveBall() {
    if (player1.intersects(ball)) {
      Toolkit.getDefaultToolkit().beep();
      double relativeY =
        (ball.getY() + ball.getHeight() / 2) -
        (player1.getY() + player1.getHeight() / 2);
      double normalizedY = relativeY / (player1.getHeight() / 2);
      double baseSpeedY = ball.getMaxSpeedY();
      ball.setSpeedY(normalizedY * baseSpeedY);
      ball.setSpeedX(-ball.getSpeedX()); // Reverse X direction
    } else if (player2.intersects(ball)) {
      Toolkit.getDefaultToolkit().beep();
      double relativeY =
        (ball.getY() + ball.getHeight() / 2) -
        (player2.getY() + player2.getHeight() / 2);
      double normalizedY = relativeY / (player2.getHeight() / 2);
      double baseSpeedY = ball.getMaxSpeedY();
      ball.setSpeedY(normalizedY * baseSpeedY);
      ball.setSpeedX(-ball.getSpeedX()); // Reverse X direction
    } else if (ball.intersects(topWall) || ball.intersects(bottomWall)) {
      ball.setSpeedY(-ball.getSpeedY()); // Reverse Y direction
    }
  }

  private void movePlayers() {
    if (keyboard.w) {
      if ((player1.y - player1.speed) > topWall.getHeight()) {
        player1.y -= player1.speed;
      }
    }
    if (keyboard.s) {
      if (
        (player1.y + player1.speed) <
        getHeight() -
        player1.height -
        bottomWall.getHeight()
      ) {
        player1.y += player1.speed;
      }
    }
    if (keyboard.up) {
      if ((player2.y - player2.speed) > topWall.getHeight()) {
        player2.y -= player2.speed;
      }
    }
    if (keyboard.down) {
      if (
        (player2.y + player2.speed) <
        getHeight() -
        player2.height -
        bottomWall.getHeight()
      ) {
        player2.y += player2.speed;
      }
    }
  }

  private void drawNet(Graphics2D g) {
    g.setColor(Color.WHITE);
    for (int i = 0; i < getHeight(); i += 20) {
      g.fillRect(getWidth() / 2 - 5, i, 10, 10);
    }
  }

  private void Restart() {
    restartGame();
    paused = false;
    scoreManager.score1 = 0;
    scoreManager.score2 = 0;
  }

  private void restartGame() {
    player1.y = (getHeight() / 2) - player1.height / 2;
    player2.y = (getHeight() / 2) - player2.height / 2;
    player1.reset();
    player2.reset();
    ball.x = (getWidth() / 2) - ball.width / 2;
    ball.y = (getHeight() / 2) - ball.height / 2;
    ball.speedY = 0;
    ball.Restart();
    for (GameObject gameObject : gameManager.getGameObjects()) {
      if (gameObject instanceof SpecialBalls) {
        gameManager.removeGameObject(gameObject);
      }
    }
  }
}
