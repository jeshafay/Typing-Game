import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.io.File;

public class GameOver extends JFrame {
    private boolean isRetrySelected;
    private ImageIcon retryIcon;
    private ImageIcon menuIcon;

    public GameOver(String difficulty) {
        setTitle("Game Over");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        isRetrySelected = true; // start with 'Retry' selected

        File retryFile = new File("KeytoHatch\\resources\\selectRetry.png");
        File menuFile = new File("KeytoHatch\\resources\\selectMenu.png");

        retryIcon = new ImageIcon(retryFile.getPath());
        menuIcon = new ImageIcon(menuFile.getPath());

        JPanel gameOverPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                // get the y-coordinate where the image will be drawn
                int x = (getWidth() - retryIcon.getIconWidth()) / 2;
                int y = (getHeight() - retryIcon.getIconHeight()) / 2;

                // draw the selectRetry/selectMenu image based on isRetrySelected's value
                if (isRetrySelected) {
                    g2d.drawImage(retryIcon.getImage(), x, y, null);
                } else {
                    g2d.drawImage(menuIcon.getImage(), x, y, null);
                }
            }
        };
        add(gameOverPanel);


        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();

                // restart typing game if enter is pressed on the Retry option
                if (keyCode == KeyEvent.VK_ENTER && isRetrySelected) {
                    startTypingGame(difficulty);
                }
                // switch between retry and menu images if the arrow keys are pressed
                if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN) {
                    isRetrySelected = !isRetrySelected;

                    gameOverPanel.repaint();
                } 
                // go to menu if enter is pressed on the Menu option
                else if (keyCode == KeyEvent.VK_ENTER && !isRetrySelected) {
                    returnToMenu();
                }
            }
        });

        setLocationRelativeTo(null); // Center the JFrame
    }

    // if enter is pressed while on the Retry option:
    private void startTypingGame(String difficulty) {
        dispose();
        SwingUtilities.invokeLater(() -> {
            switch (difficulty) {
                case "easy":
                    TypingGameEasy typingGameEasy = new TypingGameEasy();
                    typingGameEasy.setLocationRelativeTo(null);
                    typingGameEasy.setVisible(true);
                    break;
                case "medium":
                    TypingGameMedium typingGameMedium = new TypingGameMedium();
                    typingGameMedium.setLocationRelativeTo(null);
                    typingGameMedium.setVisible(true);
                    break;
                case "survival":
                    TypingGameSurvival typingGameSurvival = new TypingGameSurvival();
                    typingGameSurvival.setLocationRelativeTo(null);
                    typingGameSurvival.setVisible(true);
                    break;

            }
        });
    }

    // if enter is pressed while on the Menu option:
    private void returnToMenu() {
        dispose();
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Game Menu");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            GameMenu menu = new GameMenu();

            frame.setResizable(false);
            frame.add(menu);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GameOver("easy").setVisible(true);
        });
    }
}
