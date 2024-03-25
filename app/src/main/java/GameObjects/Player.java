package GameObjects;
import java.awt.*;

public class Player extends GameObject {

  public int speed = 4;
  public int width = 5;
  public int height = 50;

  public Player(int x, int y) {
    super(x, y);
  }

  public void setSize(int width, int height) {
    if (width > 0 && width < 10) {
      this.width = width;
    }
    if (height < 20) height = 20;
    if (height > 100) height = 100;
    this.height = height;
  }

  @Override
  public void tick() {}

  @Override
  public void render(Graphics2D g) {
    g.setColor(Color.WHITE);
    g.fillRect(x, y, width, height);
  }

  public boolean intersects(Ball ball) {
    Rectangle playerRect = new Rectangle(x, y, width, height);
    Rectangle ballRect = new Rectangle(ball.x, ball.y, ball.width, ball.height);
    return playerRect.intersects(ballRect);
  }
  public boolean intersects(SpecialBalls ball) {
    Rectangle playerRect = new Rectangle(x, y, width, height);
    Rectangle ballRect = new Rectangle(ball.x, ball.y, ball.width, ball.height);
    return playerRect.intersects(ballRect);
  }

  public void reset(){
    this.setSize(10, 50);
  }
  public int getY() {
    return y;
  }

  public int getHeight() {
    return height;
  }
}
