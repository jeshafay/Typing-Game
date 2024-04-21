import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class GameMenu extends JPanel {
    private ImageIcon startIcon;
    private ImageIcon exitIcon;
    private ImageIcon selectEasyIcon;
    private ImageIcon selectMediumIcon;
    private ImageIcon selectSurvivalIcon;
    private int menuState; // 0 for start, 1 for difficulty selection
    private int difficultySelection; // 0 for easy, 1 for medium, 2 for survival
    private boolean isStartSelected;

    public GameMenu() {
        File startFile = new File("KeytoHatch\\resources\\start1.png");
        File exitFile = new File("KeytoHatch\\resources\\exit1.png");
        File selectEasyFile = new File("KeytoHatch\\resources\\selecteasy.png");
        File selectMediumFile = new File("KeytoHatch\\resources\\selectmedium.png");
        File selectSurvivalFile = new File("KeytoHatch\\resources\\selectsurvival.png");

        startIcon = new ImageIcon(startFile.getPath());
        exitIcon = new ImageIcon(exitFile.getPath());
        selectEasyIcon = new ImageIcon(selectEasyFile.getPath());
        selectMediumIcon = new ImageIcon(selectMediumFile.getPath());
        selectSurvivalIcon = new ImageIcon(selectSurvivalFile.getPath());

        isStartSelected = true;
        menuState = 0;
        difficultySelection = 0; // Default to easy difficulty

        setPreferredSize(new Dimension(800, 600));

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (menuState == 0) { // Start screen
                    if (keyCode == KeyEvent.VK_ENTER && isStartSelected) {
                        menuState = 1; // Change to difficulty selection screen
                    } else if (keyCode == KeyEvent.VK_ENTER && !isStartSelected) {
                        System.exit(0); // Exit the application
                    } else if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN) {
                        isStartSelected = !isStartSelected; // Toggle selection between start and exit
                    }
                } else if (menuState == 1) { // Difficulty selection screen
                    if (keyCode == KeyEvent.VK_ENTER) {
                        // Handle difficulty selection based on the current selection
                        switch (difficultySelection) {
                            case 0:
                                startTypingGameEasy();
                                break;
                            case 1:
                                startTypingGameMedium();
                                break;
                            case 2:
                                startTypingGameSurvival();
                        }
                    } else if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_LEFT) {
                        difficultySelection = (difficultySelection - 1 + 3) % 3; // Cycle through difficulties
                    } else if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_RIGHT) {
                        difficultySelection = (difficultySelection + 1) % 3; // Cycle through difficulties
                    } else if (keyCode == KeyEvent.VK_ESCAPE) {
                        menuState = 0; // Go back to start screen
                    }
                }
                repaint(); // Repaint the panel to reflect changes
            }
        });

        setFocusable(true);
    }

    private void startTypingGameEasy() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this); // Get the parent JFrame
        frame.dispose(); // Close the menu window
        SwingUtilities.invokeLater(() -> {
            TypingGameEasy typingGame = new TypingGameEasy(); 
            typingGame.setLocationRelativeTo(null); // make window appear in the middle of screen
            typingGame.setVisible(true);
        });
    }

    private void startTypingGameMedium() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.dispose();
        SwingUtilities.invokeLater(() -> {
            TypingGameMedium typingGame = new TypingGameMedium();
            typingGame.setLocationRelativeTo(null);
            typingGame.setVisible(true);
        });
    }

    private void startTypingGameSurvival() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.dispose();
        SwingUtilities.invokeLater(() -> {
            TypingGameSurvival typingGame = new TypingGameSurvival();
            typingGame.setLocationRelativeTo(null);
            typingGame.setVisible(true);
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (menuState == 0) { // Start screen
            int x = (getWidth() - startIcon.getIconWidth()) / 2;
            int y = (getHeight() - startIcon.getIconHeight()) / 2;
            if (isStartSelected) {
                g2d.drawImage(startIcon.getImage(), x, y, null);
            } else {
                g2d.drawImage(exitIcon.getImage(), x, y, null);
            }
        } else if (menuState == 1) { // Difficulty selection screen
            int x = (getWidth() - selectEasyIcon.getIconWidth()) / 2;
            int y = (getHeight() - selectEasyIcon.getIconHeight()) / 2;
            switch (difficultySelection) {
                case 0:
                    g2d.drawImage(selectEasyIcon.getImage(), x, y, null);
                    break;
                case 1:
                    g2d.drawImage(selectMediumIcon.getImage(), x, y, null);
                    break;
                case 2:
                    g2d.drawImage(selectSurvivalIcon.getImage(), x, y, null);
                    break;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Game Menu");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            GameMenu menu = new GameMenu();

            frame.add(menu);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
