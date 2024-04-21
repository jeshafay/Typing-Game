import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

// Define the TypingGameEasy class which extends JFrame
public class TypingGameMedium extends JFrame {
    // Declare variable
    private JPanel topPanel;
    private JPanel gamePanel;
    private JPanel bottomPanel;
    private JLabel timerLabel;
    private JLabel pointsLabel;
    private JLabel difficultyLabel;
    private JTextField typeField;
    private Timer timer;
    private final int GAME_DURATION = 60;
    private final int WORD_DURATION = 1;
    private final int WORD_SPEED = 200; // Pixels to move per update
    private int points = 0;
    private String[] words = {"apple", "banana", "grape", "starfruit", "kiwi", "pineapple", "house", "car", "school",
            "cat", "dog", "father", "mother", "day", "time", "month", "book", "table", "pencil", "laptop", "phone",
            "friend", "smile", "road", "water", "park", "strawberry", "orange", "elephant", "guitar", "sun", "moon",
            "beach", "mountain", "river", "computer", "chair", "television", "flower", "painting", "football", "basketball",
            "soccer", "baseball", "restaurant", "pizza", "sandwich", "library", "ocean", "pithecanthropus", "microphone",
            "robot", "universe", "star", "galaxy", "planet", "giraffe", "elephant", "lion"};

    private List<String> shuffledWords;
    private List<JLabel> wordLabels = new ArrayList<>();
    private List<Point> wordPositions = new ArrayList<>();
    private int wordIndex = 0;
    private ImageIcon backgroundImage;

    // Constructor
    public TypingGameMedium() {
        File backgroundFile = new File("KeytoHatch\\resources\\bg1.png");
        backgroundImage = new ImageIcon(backgroundFile.getPath());

        // Set JFrame properties
        setTitle("Typing Game Medium");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
         //create a panel to hold the timer, points, and difficulty labels
        topPanel = new JPanel(new BorderLayout());

        //create label for the timer
        timerLabel = new JLabel("Time: " + GAME_DURATION);
        timerLabel.setForeground(Color.WHITE);
        timerLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        topPanel.setBackground(Color.BLACK);
        topPanel.add(timerLabel, BorderLayout.WEST);

        //create label for the points
        pointsLabel = new JLabel("Points: " + points);
        pointsLabel.setForeground(Color.WHITE);
        pointsLabel.setFont(new Font("Arial", Font.PLAIN, 26));
        pointsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(pointsLabel, BorderLayout.CENTER);

        //create label for the diffculty chosen
        difficultyLabel = new JLabel("Difficulty : Medium ");
        difficultyLabel.setForeground(Color.WHITE);
        difficultyLabel.setFont(new Font("Arial", Font.PLAIN, 26));
        topPanel.add(difficultyLabel, BorderLayout.EAST);

        //add the top panel to the main panel
        add(topPanel, BorderLayout.NORTH);

        // add game panel for the game
        gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Load the background image
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), null);
                
            }
        };

        gamePanel.setLayout(null);
        add(gamePanel, BorderLayout.CENTER);

        // add bottom panel for typing the text for the game
        // add typing field for our typed text
        bottomPanel = new JPanel(new BorderLayout());
        typeField = new JTextField();
        typeField.setHorizontalAlignment(SwingConstants.CENTER);
        typeField.setFont(new Font("Courier New", Font.PLAIN, 30));
        typeField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // Not needed for this implementation
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // Not needed for this implementation
            }

            @Override
            public void keyReleased(KeyEvent e) {
                checkWord();
            }
        });

        //adding the typefield to the bottom panel
        bottomPanel.add(typeField, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

         //create timer for our top panel with game_duration as the time
        timer = new Timer(1000, new ActionListener() {
            int timeLeft = GAME_DURATION;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (timeLeft <= 0) {
                    endGame();
                } else {
                    timeLeft--;
                    timerLabel.setText("Time: " + timeLeft);
                    if (timeLeft % WORD_DURATION == 0) {
                        addNextWord();
                    }
                    moveWords();
                }
            }
        });

        //shuffle the array that includes the words
        shuffledWords = new ArrayList<>(List.of(words));
        Collections.shuffle(shuffledWords);


        timer.start();
    }

    //method to add our next word
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

    // Teleport the word within the game panel boundaries
    private void teleportWord(JLabel label) {
    // Get a random x-coordinate within the game panel's width minus the label's width
    int x = new Random().nextInt(gamePanel.getWidth() - label.getWidth());
    // Get a random y-coordinate within the game panel's height minus the label's height
    int y = new Random().nextInt(gamePanel.getHeight() - label.getHeight());
    // Set the location of the label to the calculated x and y coordinates
    label.setLocation(x, y);
    }


    // Method for moving the words
    private void moveWords() {
    for (int i = 0; i < wordLabels.size(); i++) {
        JLabel label = wordLabels.get(i);
        Point position = wordPositions.get(i);

        // Choose a random direction with 4 as a bound
        int direction = new Random().nextInt(4); // 0: downwards, 1: upwards, 2: right to left, 3: teleport

        switch (direction) {
            case 0: // Move downwards
                // Ensure the word keeps moving down even when it reaches the top boundary
                position.translate(0, WORD_SPEED);
                break;
            case 1: // Move upwards
                // Ensure the word keeps moving up even when it reaches the bottom boundary
                position.translate(0, -WORD_SPEED);
                break;
            case 2: // Move right to left
                // Ensure the word keeps moving left even when it reaches the right boundary
                position.translate(-WORD_SPEED, 0);
                break;
            case 3: // Teleport by calling the teleportWord method
                teleportWord(label);
                break;
        }

        // Check and correct if the word goes out of bounds
        if (position.x < 0) {
            position.x = gamePanel.getWidth() - label.getWidth(); // Move the word to the right boundary
        } else if (position.x > gamePanel.getWidth() - label.getWidth()) {
            position.x = 0; // Move the word to the left boundary
        }
        if (position.y < 0) {
            position.y = gamePanel.getHeight() - label.getHeight(); // Move the word to the bottom boundary
        } else if (position.y > gamePanel.getHeight() - label.getHeight()) {
            position.y = 0; // Move the word to the top boundary
        }

        // Set the new location of the word label
        label.setLocation(position);
        }

    // Repaint the game panel to update the display
    gamePanel.repaint();
    }

    //method to check the typed text(our current typing) if it is equal to the floating word
    private void checkWord() {
        String typedText = typeField.getText();
        for (int i = 0; i < wordLabels.size(); i++) {
            JLabel label = wordLabels.get(i);
            String word = label.getText();
            if (word.equals(typedText)) {
                // Remove the word label
                gamePanel.remove(label);

                // Create a points label at the same position and size as the word label
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

                // Update scores and remove the word label (after adding points label)
                points += typedText.length();
                this.pointsLabel.setText("Points: " + points);
                typeField.setText("");
                wordLabels.remove(i);
                wordPositions.remove(i);

               // Check if the player reached certain points and change the image accordingly
                if (points >= 60) {
                    File backgroundFile = new File("KeytoHatch\\resources\\bg4.png");
                    backgroundImage = new ImageIcon(backgroundFile.getPath());
                    repaint();
                    winGame();
                } else if (points >= 50) {
                    File backgroundFile = new File("KeytoHatch\\resources\\bg3.png");
                    backgroundImage = new ImageIcon(backgroundFile.getPath());
                    repaint();
                } else if (points >= 40) {
                    File backgroundFile = new File("KeytoHatch\\resources\\bg2.png");
                    backgroundImage = new ImageIcon(backgroundFile.getPath());
                    repaint();
                }
                

                break; // ini utk apa
            }
        }
    }

      //end game method to stop our game if we didnt meet the condition
      private void endGame() {
        timer.stop();
        JOptionPane.showMessageDialog(this, "Game Over! Your final score is: " + points);
        dispose();
        SwingUtilities.invokeLater(() -> {
            GameOver gameOver = new GameOver("easy");
            gameOver.setVisible(true);
        });
    }

    //win game method to stop our game if we met the points set
    private void winGame() {
        timer.stop();
        gamePanel.removeAll();
        JOptionPane.showMessageDialog(this, "You Won! Your chick turned into a COCK");
        topPanel.removeAll();
        bottomPanel.removeAll();
        File backgroundFile = new File("KeytoHatch\\resources\\win.jpg");
        backgroundImage = new ImageIcon(backgroundFile.getPath());

         // Add a key listener to detect when the Enter key is pressed
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    dispose();// Dispose of the current frame and open the main menu
                    // open the main menu
                    SwingUtilities.invokeLater(() -> {
                        JFrame menuFrame = new JFrame("Game Menu");
                        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        GameMenu menu = new GameMenu();
                        menuFrame.add(menu);
                        menuFrame.pack();
                        menuFrame.setLocationRelativeTo(null);
                        menuFrame.setVisible(true);
                    });
                }
            }
        });
        this.requestFocusInWindow();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TypingGameMedium typingGame = new TypingGameMedium();
            typingGame.setLocationRelativeTo(null); // spawn di tengah
            typingGame.setVisible(true);
        });
    }
}