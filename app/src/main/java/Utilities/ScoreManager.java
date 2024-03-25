package Utilities;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import GameObjects.GameObject;

public class ScoreManager extends GameObject{
    public int score1 = 0;
    public int score2 = 0;

    public ScoreManager(int x, int y) {
        super(x, y);
    }

    @Override
    public void tick() {
        
    }

    @Override
    public void render(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        g.setFont(new Font("Arial", Font.BOLD, 42));
        
        String score1Text = "" + score1;
        String score2Text = "" + score2;
        int score1Width = g.getFontMetrics().stringWidth(score1Text);        
        g.drawString(score1Text, x - score1Width - 30, y); 
        g.drawString(score2Text, x + 30, y); 
    }
}
