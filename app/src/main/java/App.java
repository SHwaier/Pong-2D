import javax.swing.*;

import Utilities.GameCanvas;

public class App {

  public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
          public void run() {
              JFrame frame = new JFrame("Game");
              frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
              frame.setSize(960, 540);
              frame.setLocationRelativeTo(null);
              frame.setResizable(false);
              
              GameCanvas gameCanvas = new GameCanvas();
              frame.add(gameCanvas);
              frame.setVisible(true);

              // This ensures setupGame() is called after the JFrame is visible and GameCanvas is added.
              gameCanvas.setupGame();
          }
      });
  }
}
