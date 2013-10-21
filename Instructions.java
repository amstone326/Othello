
public class Instructions {

	String instructions;
	
	public Instructions() {
		instructions = 
				"WELCOME TO OTHELLO! Here are the rules of play:" + "\n" + "\n" +
				"1. The object of the game is to end up with the most pieces " + "\n" +
				"of your own color on the board when the game ends." + "\n" +
				"2. The game board starts out with 4 pieces played in the" + "\n" +
				"center, 2 of each color. Black always goes first. " + "\n" +
				"3. A player's move consists of placing one of his/her pieces " + "\n" + 
				"on the board. A move must always sandwich one or more of the " + "\n" +
				"opponent's piece's between 2 of its own. When this happens, " + "\n" +
				"all sandwiched pieces are flipped to the other player's color. " + "\n" +
				"Any moves that do NOT sandwich the opponent's pieces are not " + "\n" +
				"valid, and will not be allowed." + "\n" +
				"4. If at any point, one player has no valid moves when it is " + "\n" +
				"his/her turn, the opponent goes again. " + "\n" +
				"5. The game ends when either: a) All squares on the booard " + "\n" +
				"are filled, or b) Neither player has any valid moves left. " + "\n" +
				"At this point, the player with the most pieces on the board " + "\n" +
				"is the winner." + "\n" + 
				"6. Each player is allowed to use the 'Show My Best Move' button " + "\n" +
				"once per game. This button reveals the board square that will " + "\n" +
				"yield the most flipped pieces if played." + "\n" + "\n" +
				"Helpful Hint: Winning the corner pieces is key!!";
	}
	
	public String getMessage() {
		return instructions;
	}

}
