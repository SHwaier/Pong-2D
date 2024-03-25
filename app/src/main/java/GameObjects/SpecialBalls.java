package GameObjects;
import java.awt.Color;
import java.awt.Graphics2D;

public class SpecialBalls extends Ball {

  public enum SpecialBallType {
    RED,
    BLUE,
  }

  public SpecialBallType type;

  public SpecialBalls(int x, int y, SpecialBallType type) {
    super(x, y);
    this.type = type;
  }

  @Override
  public void tick() {
    super.tick();
    
  }

  @Override
  public void render(Graphics2D g) {
    g.setColor(type == SpecialBallType.RED ? Color.RED : Color.BLUE);
    g.fillRect(x, y, width, height);
  }
  
}
