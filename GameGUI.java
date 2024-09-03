package rockpaperscissors;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameGUI {
	private final Game game = new Game();
    private final JFrame frame = new JFrame("Rock-Paper-Scissors");
    private final JLabel resultLabel = new JLabel("Choose your move:", SwingConstants.CENTER);
    private final JLabel botMoveLabel = new JLabel("", SwingConstants.CENTER);
    private int playerWins = 0;
    private int botWins = 0;
    private int rounds = 0;
    private int roundsPlayed = 0;

    public void createAndShowGUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLayout(new BorderLayout());

        // Top panel for round decision and start button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel roundsLabel = new JLabel("Rounds:");
        roundsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        JTextField roundsField = new JTextField(5);
        JButton startButton = new JButton("Start Game");

        topPanel.add(roundsLabel);
        topPanel.add(roundsField);
        topPanel.add(startButton);

        // Center panel for game buttons
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add spacing between buttons

        // Create buttons with images
        JButton rockButton = createButton("Rock", "resources/rock.png");
        JButton paperButton = createButton("Paper", "resources/paper.png");
        JButton scissorsButton = createButton("Scissors", "resources/scissors.png");

        // Add buttons to the grid
        gbc.gridx = 0;
        centerPanel.add(rockButton, gbc);
        gbc.gridx = 1;
        centerPanel.add(paperButton, gbc);
        gbc.gridx = 2;
        centerPanel.add(scissorsButton, gbc);

        // Bottom panel for result label
        JPanel bottomPanel = new JPanel(new GridLayout(2, 1));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Style the result labels
        resultLabel.setFont(new Font("Arial", Font.BOLD, 18));
        botMoveLabel.setFont(new Font("Arial", Font.BOLD, 18));

        // Add result and bot move labels
        bottomPanel.add(resultLabel);
        bottomPanel.add(botMoveLabel);

        // Add panels to frame
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Disable move buttons until the game starts
        rockButton.setEnabled(false);
        paperButton.setEnabled(false);
        scissorsButton.setEnabled(false);

        // Start button action listener
        startButton.addActionListener(e -> {
            try {
                rounds = Integer.parseInt(roundsField.getText());
                if (rounds > 0) {
                    resultLabel.setText("Game Started! Choose your move:");
                    roundsPlayed = 0;
                    playerWins = 0;
                    botWins = 0;
                    // Enable move buttons
                    rockButton.setEnabled(true);
                    paperButton.setEnabled(true);
                    scissorsButton.setEnabled(true);
                    roundsField.setEnabled(false); // Disable input field to prevent changing rounds mid-game
                    startButton.setEnabled(false); // Disable start button after the game starts
                } else {
                    JOptionPane.showMessageDialog(frame, "Please enter a positive number for rounds.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid number of rounds. Please enter a valid number.");
            }
        });

        // Display the frame
        frame.setVisible(true);
    }

    private JButton createButton(String move, String imagePath) {
        // Load and resize image
        ImageIcon icon = new ImageIcon(imagePath);
        Image image = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH); // Resize image to fit button
        icon = new ImageIcon(image);

        JButton button = new JButton(icon);
        button.setActionCommand(move);
        button.setPreferredSize(new Dimension(120, 120)); // Set preferred size for uniformity
        button.setBackground(Color.WHITE); // Set background color for buttons
        button.setFocusPainted(false); // Remove focus paint to make the button look cleaner
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add a border for a defined look
        button.addActionListener(new MoveButtonListener());
        return button;
    }

    private class MoveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (roundsPlayed < rounds) {
                String userMove = e.getActionCommand();
                String botMove = game.getBotMove();
                String result = game.determineWinner(userMove, botMove);

                botMoveLabel.setText("Bot chose: " + botMove);

                // Update scores based on the result
                if (result.equals("You win!")) {
                    playerWins++;
                } else if (result.equals("Bot wins!")) {
                    botWins++;
                }
                
                resultLabel.setText(result);  // Display the result of the current round

                // Increment rounds played
                roundsPlayed++;

                // Check if game is over
                if (roundsPlayed == rounds) {
                    announceWinner();
                }
            }
        }
    }

    private void announceWinner() {
        // Announce the winner based on scores
        if (playerWins > botWins) {
            resultLabel.setText("Game Over! You win the game!");
        } else if (playerWins < botWins) {
            resultLabel.setText("Game Over! Bot wins the game!");
        } else {
            resultLabel.setText("Game Over! It's a tie!");
        }

        // Print the final scores for clarity
        System.out.println("Final Player Wins: " + playerWins);
        System.out.println("Final Bot Wins: " + botWins);

        // Automatically restart the game after announcing the winner
        Timer timer = new Timer(3000, e -> resetGame());
        timer.setRepeats(false); // Only run once
        timer.start();
    }

    private void resetGame() {
        // Reset game state and UI components
        roundsPlayed = 0;
        playerWins = 0;
        botWins = 0;
        resultLabel.setText("Choose your move:");
        botMoveLabel.setText("");
        rounds = 0;

        // Re-enable game start elements
        for (Component comp : frame.getContentPane().getComponents()) {
            if (comp instanceof JPanel) {
                for (Component subComp : ((JPanel) comp).getComponents()) {
                    if (subComp instanceof JButton) {
                        JButton button = (JButton) subComp;
                        if (button.getText().equals("Start Game")) {
                            button.setEnabled(true);
                        } else {
                            button.setEnabled(false); // Disable move buttons
                        }
                    }
                    if (subComp instanceof JTextField) {
                        subComp.setEnabled(true); // Enable rounds input field
                    }
                }
            }
        }
    }

    // Main method to run the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameGUI gui = new GameGUI();
            gui.createAndShowGUI();
        });
    }
}
