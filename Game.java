package rockpaperscissors;

import java.util.Random;
public class Game {
	 private final String[] options = {"Rock", "Paper", "Scissors"};

	    public String getBotMove() {
	        Random rand = new Random();
	        return options[rand.nextInt(options.length)];
	    }

	    public String determineWinner(String userMove, String botMove) {
	        if (userMove.equals(botMove)) {
	            return "It's a draw!";
	        } else if ((userMove.equals("Rock") && botMove.equals("Scissors")) ||
	                   (userMove.equals("Paper") && botMove.equals("Rock")) ||
	                   (userMove.equals("Scissors") && botMove.equals("Paper"))) {
	            return ("You win!");
	        } else {
	            return ("Bot wins!");
	        }
	    }
}
