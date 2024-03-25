package Utilities;
import java.awt.event.*;

public class Keyboard extends KeyAdapter {
    public boolean up = false;
    public boolean down = false;
    public boolean w = false;
    public boolean s = false;
    public boolean space = false;
    @Override
    public void keyPressed(KeyEvent e) {
        int keycode = e.getKeyCode();
        if (keycode == KeyEvent.VK_W) {
            w = true;
        }
        if (keycode == KeyEvent.VK_S) {
            s = true;
        }
        if (keycode == KeyEvent.VK_UP) {
            up = true;
        }
        if (keycode == KeyEvent.VK_DOWN) {
            down = true;
        }
        if (keycode == KeyEvent.VK_SPACE) {
            space = true;
        }
        
    }
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {
            w = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            s = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            up = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            down = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            space = false;
        }
    }

}
