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
public class TypingGameEasy extends JFrame {
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
    private final int WORD_DURATION = 3;
    private int points = 0;
    private String[] words = { "apple", "banana", "grape", "starfruit", "kiwi", "pineapple", "house", "Car", "school",
            "cat", "dog", "father", "mother", "day", "time", "month", "book", "table", "pencil", "laptop", "phone",
            "friend", "smile", "road", "water", "park", "strawberry" };
    private List<String> shuffledWords;
    private List<JLabel> wordLabels = Collections.synchronizedList(new ArrayList<>());
    private List<Point> wordPositions = new ArrayList<>();
    private int wordIndex = 0;
    private ImageIcon backgroundImage;


    // Constructor
    public TypingGameEasy() {
        //load background image
        File backgroundFile = new File("KeytoHatch\\resources\\bg1.png");
        backgroundImage = new ImageIcon(backgroundFile.getPath());

        // Set JFrame properties
        setTitle("Typing Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        //create a panel to hold the timer, points, and difficulty labels
        topPanel = new JPanel(new BorderLayout());

        //create label for the timer
        timerLabel = new JLabel("Time: " + GAME_DURATION);
        timerLabel.setForeground(Color.BLACK);
        timerLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        topPanel.add(timerLabel, BorderLayout.WEST);

        //create label for the points
        pointsLabel = new JLabel("Points: " + points);
        pointsLabel.setForeground(Color.BLACK);
        pointsLabel.setFont(new Font("Arial", Font.PLAIN, 26));
        pointsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(pointsLabel, BorderLayout.CENTER);

        //create label for the diffculty chosen
        difficultyLabel = new JLabel ("Difficulty : Easy ");
        difficultyLabel.setForeground(Color.BLACK);
        difficultyLabel.setFont(new Font("Arial", Font.PLAIN, 26));
        topPanel.add(difficultyLabel, BorderLayout.EAST);

        //add the top panel to the main panel
        add(topPanel, BorderLayout.NORTH);

        // add game panel for the game
        gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Load the background image for the game
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), null);
                
            }
        };
        gamePanel.setLayout(null);
        add(gamePanel, BorderLayout.CENTER);

        // add bottom panel for typing the text for the game
        bottomPanel = new JPanel(new BorderLayout());
        typeField = new JTextField();
        typeField.setHorizontalAlignment(SwingConstants.CENTER);
        typeField.setFont(new Font("Courier New", Font.PLAIN, 30));
        typeField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
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
        label.setFont(new Font("Courier New", Font.BOLD, 40));

        // Calculate a random position for the new word label within the screen size

        int x = new Random().nextInt(gamePanel.getWidth() - label.getPreferredSize().width);
        // the 'x' position is calculated as a random number from 0 to (the game panel width - the word label width). 
        // (subtract it to ensure the entire word label is visible within the game panel)
    
        int y = new Random().nextInt(gamePanel.getHeight() - label.getPreferredSize().height);
        // The 'y' position is calculated similarly to the 'x' position,

        // Set the bounds of the label using calculated x and y coordinates  and add the word to the game panel
        label.setBounds(x, y, label.getPreferredSize().width, label.getPreferredSize().height);
        gamePanel.add(label);
        wordLabels.add(label);
        gamePanel.revalidate();
        gamePanel.repaint();
        
        wordLabels.add(label);
        wordPositions.add(new Point(x, y));

        int finalX = x; // Create a final variable to store the initial x-coordinate for the label
        // Start a new thread to animate the movement of the word label across the screen
        Thread t = new Thread(() -> {
            try {
                int localX = finalX; // variable for tracking the current x-position of the label
                while (true) {
                    Thread.sleep(47); // Pause for 47 milliseconds between each update to control the speed of the animation
                    localX += 5; // amount of pixels to move the word
                    if (localX > gamePanel.getWidth()) {
                        break;
                    }
                    // Update the label's position based on the new x-coordinate
                    label.setBounds(localX, y, label.getWidth(), label.getHeight());
                    gamePanel.repaint();
                }
            } catch (InterruptedException ex) { // Handle interruptions (if any)
                ex.printStackTrace();
            }
        });
        t.start(); // start the thread

        // Create a timer to control how long the word label is displayed on the screen
        Timer wordTimer = new Timer(WORD_DURATION * 1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // This code runs when the timer expires:
                gamePanel.remove(label);
                gamePanel.revalidate();
                gamePanel.repaint();
                wordLabels.remove(label);
                t.interrupt(); // Stop the thread when the word is removed
            }
        });
        wordTimer.setRepeats(false);
        wordTimer.start();

        wordIndex++;
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
                break;
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
                    dispose(); // Dispose of the current frame and open the main menu
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
        this.requestFocusInWindow(); // Request focus for key events
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TypingGameEasy typingGame = new TypingGameEasy(); 
            typingGame.setLocationRelativeTo(null);
            typingGame.setVisible(true);
        });
    }
}