package GameObjects;
import java.awt.Graphics2D;

public class GameObject {
    public int x;
    public int y;

    public GameObject(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void tick() {
    }

    public void render(Graphics2D g) {
    }
}
