
import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.awt.event.*;
import java.util.LinkedList;

import javax.swing.*;

@SuppressWarnings("serial")
public class GameBoard extends JPanel {

	public static Turn turn;
	BoardSquare[][] squares = new BoardSquare[8][8]; 
	GamePiece[][] pieces = new GamePiece[8][8];
	//keeps track of what squares have not had a piece played on them yet
	public BoardSquare[] emptySquares = new BoardSquare[64];
	public int numBlackPlayed = 0;
	public int numWhitePlayed = 0;
	public Game thisGame;
	public boolean blackUsedBestMove = false;
	public boolean whiteUsedBestMove = false;
	public GamePiece previewPiece;
	public static Icon blackWins = new ImageIcon("black_smile.png");
	public static Icon whiteWins = new ImageIcon("white_smile.png");
	public static Icon tiePic = new ImageIcon("tie_smile.png");


	public GameBoard (Game g) {
		setFocusable(true);
		thisGame = g;
		setLayout(new GridLayout(8,8,0,0)); //SPACE SET TO 0 NOT WORKING
		setBackground(Color.BLACK);
		turn = Turn.Black; //game starts with black's turn
		thisGame.turnChanges.setText("BLACK's");
		
		//LOOP for adding all board squares 
		for (int i = 0; i < squares.length; i++) {
			for (int j = 0; j < squares[0].length; j++) {
				GamePiece piece = new GamePiece(j,i,true); //sets all pieces to black initially
				final BoardSquare b = new BoardSquare(piece,j,i,this);
				piece.setBoardLocation(b);
				squares[i][j] = b;
				pieces[i][j] = piece;
				//System.out.println("the piece at pieces[" + i + "][" + j + "] " +
						//"has rownum " + piece.getRowNum() + " and colnum " + piece.getColNum());
				add(b); //adds board square to GameBoard panel
			}
		}
		
		//emptySquares starts out containing all 64 squares
		for (int i = 0; i < squares.length; i++) {
			for (int j = 0; j < squares[0].length; j++) {
				emptySquares[i*8+j] = squares[i][j];
			}
		}
		//set the 4 starting center pieces
		pieces[3][3].setColor(false); //top left and bottom right are white
		pieces[4][4].setColor(false);
		pieces[3][4].setColor(true); //top right and bottom left are black
		pieces[4][3].setColor(true);
		pieces[3][3].setVisible();
		pieces[3][4].setVisible();
		pieces[4][3].setVisible();
		pieces[4][4].setVisible();
		emptySquares[27] = null;
		emptySquares[28] = null;
		emptySquares[35] = null;
		emptySquares[36] = null;
		numBlackPlayed+=2;
		numWhitePlayed+=2;
		
		repaint();
	}
	
	
	public void updateTurn() {
		boolean whiteHasMove = checkValidTurn(Turn.White);
		boolean blackHasMove = checkValidTurn(Turn.Black);
		if (numBlackPlayed + numWhitePlayed == 64)
			gameOverAction();
		else if (!whiteHasMove && !blackHasMove)
			gameOverAction();
		else {
			if (turn == Turn.White) {
				if (blackHasMove) {
					turn = Turn.Black;					
					thisGame.turnChanges.setForeground(Color.BLACK);
					thisGame.turnChanges.setText("BLACK's");
				}
				else 
					noMovesAvailableAction();
			}
			else {
				if (whiteHasMove) {
					turn = Turn.White;
					thisGame.turnChanges.setForeground(Color.WHITE);
					thisGame.turnChanges.setText("WHITE's");
				}
				else
					noMovesAvailableAction();
			}
		}
	}
	
	
	public boolean checkValidTurn(Turn t) {
		//checks all squares that are still empty to see if they are a valid click for this player
		int numTestedinCheckValidTurn = 0;
		for (int i = 0; i < emptySquares.length; i++) {
			if (emptySquares[i] != null) {
				numTestedinCheckValidTurn++;
				int testRow = emptySquares[i].piece.getRowNum();
				int testCol = emptySquares[i].piece.getColNum();
				
				GamePiece testp;
				if (t == Turn.Black)
					 testp = new GamePiece(testRow, testCol, true);
				else 
					testp = new GamePiece(testRow, testCol, false);
				BoardSquare testb = new BoardSquare(testp,testRow,testCol,this);
				//System.out.println("The color of the test piece is " + testp.getColor());

				LinkedList<GamePiece> ll = testb.checkValidClick(testp, testRow, testCol);
				if (ll.size() != 0) {
					for (int k = 0; k < ll.size(); k++) {
						System.out.println("The piece making this a valid turn is at row " +
					ll.get(k).getRowNum() + " and col " + ll.get(k).getColNum());
					}
					System.out.println(numTestedinCheckValidTurn);
					return true;
				}
			}
		}
		//if it gets to this point, none of the empty spaces were valid
		System.out.println(numTestedinCheckValidTurn);
		return false;
	}
	
	public boolean booleanCheckValidClick(BoardSquare b, GamePiece p, int r, int c) {
		for (int i = c-1; i <= c+1; i++) {
			for (int j = r-1; j <= r+1; j++) {
				//in case some surrounding squares don't exist (i.e. b/c it is an edge piece)
				if (!BoardSquare.indexInBounds(i,j))
					continue;
				if (i == c && j ==r)
					continue;
				//if a surrounding piece is visible and is of the opponent's color, check further
				if ((pieces[i][j].getColor() != p.getColor()) 
						&& pieces[i][j].getVisibility()) {
					if (b.checkFurtherPieces(r,c,j,i,p)) {
						return true;
					}
				}
			}
		}
		return false;
	}


	public void updatePiecesLabel() {
		thisGame.blackChanges.setText(Integer.toString(numBlackPlayed));
		thisGame.whiteChanges.setText(Integer.toString(numWhitePlayed));	
	}
	

	public void gameOverAction() {
		
		if (numBlackPlayed > numWhitePlayed) {
			Object blackWinsMessage = "GAME OVER! BLACK WINS!";
			JOptionPane.showMessageDialog(thisGame.frame, blackWinsMessage,"GAME OVER",1,blackWins);
		} 
		
		else if (numWhitePlayed > numBlackPlayed) {
			Object whiteWinsMessage = "GAME OVER! WHITE WINS!";
			JOptionPane.showMessageDialog(thisGame.frame, whiteWinsMessage,"GAME OVER",1,whiteWins);
		}
		
		else {
			Object tieMessage = "GAME OVER! THE GAME IS A TIE!";
			JOptionPane.showMessageDialog(thisGame.frame, tieMessage,"GAME OVER",1,tiePic);
		}
		
	}
	
	
	public void noMovesAvailableAction() {
		if (turn == Turn.Black) {
			thisGame.warningsDisplay.setText("There are currently no moves available " +
				"for White. It is Black's turn again.");
			turn = Turn.Black;
		}
		else {
			thisGame.warningsDisplay.setText("There are currently no moves available " +
				"for Black. It is White's turn again.");
			turn = Turn.White;
		}
	}
	
	public void invalidClickAction() {
		//update the warnings label in Game class
		thisGame.warningsDisplay.setText("WARNING: The selected space is not a valid move. " +
				"Please try again!");
	}
	
	public GamePiece getBestMove() {
		//System.out.println("GET BEST MOVE ENTERED");
		GamePiece bestMove = null;
		int maxValue = 0;
		for (int i = 0; i < emptySquares.length; i++) {
			//skips any squares that have already been played 
			if (emptySquares[i] == null)
				continue;
			
			int r = emptySquares[i].rownum;
			int c = emptySquares[i].colnum;
			//System.out.println("TEST PIECE ROW NUM IS: " + r);
			//System.out.println("TEST PIECE COL NUM IS: " + c);
			GamePiece testp = new GamePiece(r, c, true);
			testp.setBoardLocation(squares[c][r]);
			if (GameBoard.turn == Turn.White)
				testp.setColor(false);
			
			LinkedList<GamePiece> valDir = testp.location.checkValidClick(testp, r, c);
			if (valDir.size() == 0)
				continue;
			/*
			System.out.println("size of validDir is " + valDir.size());
			for (int k = 0; k < valDir.size(); k ++) {
				System.out.println("Piece " + k + " in valDir has row " + valDir.get(k).getRowNum() +
						" and col " + valDir.get(k).getColNum());
				System.out.println("Piece " + k + " in valDir has color " + valDir.get(k).getColor());
				if (valDir.get(k).getVisibility())
					System.out.println("Piece " + k + " in valDir is visible");
			}
			*/
			
			//only evaluates moves that have a non-empty valid directions linked list 
			//System.out.println("getBestMove checks value of emptySquares[" + i + "]");
			int value = evalueThisMove(testp,GameBoard.turn,valDir);
			if (value > maxValue) {
				maxValue = value;
				bestMove = testp;
			}
		}
		return bestMove;
	}
	
	public int evalueThisMove(GamePiece p, Turn t, LinkedList<GamePiece> ll) {
		//p is the origin piece we're testing, ll is a list of pieces surrounding it that could make it a valid click
		
		//WHAT THIS DOES: evaluates what would happen if curr player were to put piece p
		//on its square b
		int originRow = p.getRowNum();
		int originCol = p.getColNum();
		int value = 0;
		for (int i = 0; i < ll.size(); i++) {
			GamePiece surrounding = ll.get(i);
			int nextRow = surrounding.getRowNum();
			int nextCol = surrounding.getColNum();
			value += getValue(originRow,originCol,nextRow,nextCol,p);
		}
		/*
		if (value >= 1)
			System.out.println("The value of a move at square with row num " + originRow +
					" and col num" + originCol + " is " + value);
					*/
		return value;
	}
	
	public int getValue(int squareTriedRow, int squareTriedCol, 
			int nextSquareRow, int nextSquareCol, GamePiece originPiece) {

		int colDiff = nextSquareCol-squareTriedCol;
		int rowDiff = nextSquareRow-squareTriedRow;
		int nexti = nextSquareCol + colDiff;
		int nextj = nextSquareRow + rowDiff;
		GamePiece next = pieces[nexti][nextj];

		int value = 1;
		while(next.getColor() != originPiece.getColor()) {
			value++;
			nexti += colDiff;
			nextj += rowDiff;
			next = pieces[nexti][nextj];
		}
		
		return value;
	}

	
	public static String turnToString(Turn t) {
		if (t == Turn.Black)
			return "black";
		else
			return "white";
	}

	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int i = 0; i < squares.length; i++) {
			for (int j = 0; j < squares[0].length; j++) {
				g.setColor(Color.GREEN);
				squares[i][j].paint(g);
			}
		}
	}

}
