import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TypingGameSurvival extends JFrame {
    private JPanel gamePanel;
    private JLabel timerLabel;
    private JLabel healthLabel;
    private JLabel pointsLabel;
    private JTextField typeField;
    private Timer timer;
    private final int WORD_DURATION = 1;
    private final int WORD_SPEED = 50;
    private int health = 5;
    private int elapsedTime = 0;
    private int points = 0;
    private String[] words = { "apple", "banana", "grape", "starfruit", "kiwi", "pineapple", "house", "car", "school",
            "cat", "dog", "father", "mother", "day", "time", "month", "book", "table", "pencil", "laptop", "phone",
            "friend", "smile", "road", "water", "park", "strawberry", "orange", "elephant", "guitar", "sun", "moon",
            "beach", "mountain", "river", "computer", "chair", "television", "flower", "painting", "football", "basketball",
            "soccer", "baseball", "restaurant", "pizza", "sandwich", "library", "ocean", "pithecanthropus", "microphone",
            "robot", "universe", "star", "galaxy", "planet", "giraffe", "elephant", "lion" };

    private List<String> shuffledWords;
    private List<JLabel> wordLabels = new ArrayList<>();
    private List<Point> wordPositions = new ArrayList<>();
    private int wordIndex = 0;
    private ImageIcon backgroundImage;

    public TypingGameSurvival() {
        File bgImageFile = new File("KeytoHatch\\resources\\bgEmpty.png");
        backgroundImage = new ImageIcon(bgImageFile.getPath()); 

        setTitle("Typing Game Survival");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        timerLabel = new JLabel("Time: " + elapsedTime);
        timerLabel.setForeground(Color.WHITE);
        timerLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        topPanel.setBackground(Color.BLACK);
        topPanel.add(timerLabel, BorderLayout.WEST);

        pointsLabel = new JLabel("Points: " + points);
        pointsLabel.setForeground(Color.WHITE);
        pointsLabel.setFont(new Font("Arial", Font.PLAIN, 26));
        pointsLabel.setHorizontalAlignment(SwingConstants.LEFT);
        topPanel.add(pointsLabel, BorderLayout.EAST);

        healthLabel = new JLabel("Health: " + getHealthString());
        healthLabel.setForeground(Color.WHITE);
        healthLabel.setFont(new Font("Arial", Font.PLAIN, 26));
        healthLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(healthLabel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };
        gamePanel.setLayout(null);
        add(gamePanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());

        typeField = new JTextField();
        typeField.setHorizontalAlignment(SwingConstants.CENTER);
        typeField.setFont(new Font("Courier New", Font.PLAIN, 30));
        typeField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                // Not needed for this implementation
            }

            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                // Not needed for this implementation
            }

            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                checkWord();
            }
        });
        bottomPanel.add(typeField, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                elapsedTime++;
                timerLabel.setText("Time: " + elapsedTime);
                if (elapsedTime % WORD_DURATION == 0) {
                    addNextWord();
                }
                moveWords();
            }
        });

        // Shuffling the words
        shuffledWords = new ArrayList<>(List.of(words));
        Collections.shuffle(shuffledWords);

        timer.start();
    }

    private String getHealthString() {
        StringBuilder healthString = new StringBuilder();
        for (int i = 0; i < health; i++) {
            healthString.append("♥"); // Unicode character for heart
        }
        return healthString.toString();
    }

    private void addNextWord() {
        // Reset wordIndex if it exceeds the size of shuffledWords list
        if (wordIndex >= shuffledWords.size()) {
            wordIndex = 0;
        }

        // Get the word from the shuffledWords list
        String word = shuffledWords.get(wordIndex);
    
        JLabel label = new JLabel(word);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Courier New", Font.BOLD, 20));
    
        // Set the size of the label based on its preferred size
        label.setSize(label.getPreferredSize());
    
        // Calculate a random position for the new word label within the game panel
        int x = new Random().nextInt(gamePanel.getWidth() - label.getWidth());
        int y = new Random().nextInt(gamePanel.getHeight() - label.getHeight());
    
        // Set the position of the label
        label.setLocation(x, y);
        
        // Add the label to the game panel
        gamePanel.add(label);

        // Repaint the game panel to update the display
        gamePanel.revalidate();
        gamePanel.repaint();

        // Add the label and position to the lists
        wordLabels.add(label);
        wordPositions.add(new Point(x, y));

        // Increment the word index
        wordIndex++;
    }

    private void teleportWord(JLabel label) {
        int x = new Random().nextInt(gamePanel.getWidth() - label.getWidth());
        int y = new Random().nextInt(gamePanel.getHeight() - label.getHeight());
        label.setLocation(x, y);
    }

    private void moveWords() {
        for (int i = 0; i < wordLabels.size(); i++) {
            JLabel label = wordLabels.get(i);
            Point position = wordPositions.get(i);

            // Choose a random direction
            int direction = new Random().nextInt(4); // 0: downwards, 1: upwards, 2: right to left, 3: teleport

            switch (direction) {
                case 0: // Move downwards
                    position.translate(0, WORD_SPEED);
                    break;
                case 1: // Move upwards
                    position.translate(0, -WORD_SPEED);
                    break;
                case 2: // Move right to left
                    position.translate(-WORD_SPEED, 0);
                    break;
                case 3: // Teleport
                    teleportWord(label);
                    break;
            }

            // Check and correct if the word goes out of bounds
            if (position.x < 0) {
                position.x = 0;
            } else if (position.x > gamePanel.getWidth() - label.getWidth()) {
                position.x = gamePanel.getWidth() - label.getWidth();
            }
            if (position.y < 0) {
                position.y = 0;
            } else if (position.y > gamePanel.getHeight() - label.getHeight()) {
                position.y = gamePanel.getHeight() - label.getHeight();
            }

            label.setLocation(position);
        }
    }

    private void checkWord() {
        String typedText = typeField.getText().toLowerCase().trim(); // Convert typed text to lowercase and trim

        // Flag to keep track of whether the word is found or not
        boolean wordFound = false;

        // Iterate through all displayed words to find the word the user is typing
        for (int i = 0; i < wordLabels.size(); i++) {
            JLabel label = wordLabels.get(i);
            String word = label.getText().toLowerCase(); // Convert displayed word to lowercase

            // Check if the typed text matches the beginning of any displayed word
            if (word.startsWith(typedText)) {
                wordFound = true;

                // Compare typed text with the displayed word
                int j;
                for (j = 0; j < typedText.length() && j < word.length(); j++) {
                    if (typedText.charAt(j) != word.charAt(j)) {
                        // If any letter doesn't match, break the loop
                        break;
                    }
                }

                // If the loop completes without breaking, it means the typed word matches the displayed word
                if (j == typedText.length() && j == word.length()) {
                    // Remove the word label
                    gamePanel.remove(label);

                    JLabel pointsLabel = new JLabel("+" + typedText.length());
                    pointsLabel.setForeground(Color.WHITE);
                    pointsLabel.setFont(new Font("Courier New", Font.BOLD, 20)); // Change font size here
                    pointsLabel.setBackground(Color.BLACK); // Change background color here
                    pointsLabel.setOpaque(false); // Make the background opaque
                    pointsLabel.setBounds(label.getBounds());
                    gamePanel.add(pointsLabel);

                    // Animate the points label briefly
                    Timer pointsTimer = new Timer(100, e -> {
                    pointsLabel.setForeground(new Color(pointsLabel.getForeground().getRed(),
                            pointsLabel.getForeground().getGreen(),
                            pointsLabel.getForeground().getBlue(),
                            Math.max(0, pointsLabel.getForeground().getAlpha() - 15)));
                    if (pointsLabel.getForeground().getAlpha() == 0) {
                        gamePanel.remove(pointsLabel);
                        gamePanel.revalidate();
                        // Repaint immediately after removing label
                        gamePanel.repaint();
                        ((Timer) e.getSource()).stop();
                    }
                });
                pointsTimer.start();

                    points += typedText.length();
                    this.pointsLabel.setText("Points: " + points);
                    typeField.setText("");
                    // wordLabels.remove(i);
                    // wordPositions.remove(i);

                    break; // No need to continue searching for the word
                }
            }
        }

        // If the typed word is not found, reset the text field and decrement health
        if (!wordFound && !typedText.isEmpty()) { // Add a check to ensure typedText is not empty
            health--;
            healthLabel.setText("Health: " + getHealthString());
            typeField.setText("");
            if (health == 0) {
                endGame();
            }
        }
    }

    private void endGame(){
        timer.stop();
        JOptionPane.showMessageDialog(this, "Game Over! You scored " + points + " points in " + elapsedTime + " seconds");
        gameOver();
    }

    private void gameOver(){
        dispose();
        SwingUtilities.invokeLater(() -> {
            GameOver gameOver = new GameOver("survival");
            gameOver.setVisible(true);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TypingGameSurvival typingGame = new TypingGameSurvival();
            typingGame.setLocationRelativeTo(null);
            typingGame.setVisible(true);
        });
    }
}
