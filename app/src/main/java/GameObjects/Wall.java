package GameObjects;
import java.awt.*;
public class Wall extends GameObject {
    public int width = 10;
    public int height = 10;
    public Wall(int x, int y) {
        super(x, y);
    }   
    public void setSize(int width, int height) {
        if (width > 0) {
            this.width = width;
        }
        if (height > 0) {
            this.height = height;
        }
    }
    @Override
    public void tick() {
        
    }
    @Override
    public void render(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, width, height);
    }
    public boolean intersects(Ball ball) {
        Rectangle wallRect = new Rectangle(x, y, width, height);
        Rectangle ballRect = new Rectangle(ball.x, ball.y, ball.width, ball.height);
        return wallRect.intersects(ballRect);
    }
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    
}
