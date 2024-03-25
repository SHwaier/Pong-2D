package GameObjects;

import java.awt.*;
import java.util.Random;

public class Ball extends GameObject {

  final double minSpeedY = 0;
  public double maxSpeedY = 4;
  public double speedX = 4;
  public double speedY = 1;
  public int width = 10;
  public int height = 10;

  public Ball(int x, int y) {
    super(x, y);
    Random random = new Random();
    int randomNumber = random.nextInt(2);
    if (randomNumber == 0) {
      speedX *= -1;
    }
    speedY = 0;
  }

  public void Restart() {
    Random random = new Random();
    int randomNumber = random.nextInt(2);
    if (randomNumber == 0) {
      speedX *= -1;
    }
  }

  @Override
  public void tick() {
    this.x += speedX;
    this.y += speedY;
  }

  @Override
  public void render(Graphics2D g) {
    g.setColor(Color.WHITE);
    g.fillRect(x, y, width, height);
  }

  public boolean intersects(Wall topWall) {
    Rectangle ballRect = new Rectangle(x, y, width, height);
    Rectangle wallRect = new Rectangle(
      topWall.getX(),
      topWall.getY(),
      topWall.getWidth(),
      topWall.getHeight()
    );
    return ballRect.intersects(wallRect);
  }

  public int getY() {
    return y;
  }

  public int getHeight() {
    return height;
  }

  public double getMaxSpeedY() {
    return maxSpeedY;
  }

  public void setSpeedY(double d) {
    this.speedY = (int) d;
  }

  public void setSpeedX(double d) {
    this.speedX = d;
  }

  public double getSpeedY() {
    return speedY;
  }

  public double getSpeedX() {
    return speedX;
  }

  public double getMinSpeedY() {
    return minSpeedY;
  }
}
