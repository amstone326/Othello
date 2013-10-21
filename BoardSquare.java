import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.AbstractQueue;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.JButton;


public class BoardSquare extends Canvas {
	
	GamePiece piece; //the piece on this square
	public int rownum;
	public int colnum;
	public static final int SQUARE_DIM = 47;
	GameBoard board; //the board that this square is a part of
	
	public BoardSquare(GamePiece p, int j, int i, GameBoard g) {
		piece = p;
		rownum = i;
		colnum = j;
		board = g;
		Dimension d = new Dimension(SQUARE_DIM,SQUARE_DIM);
		setPreferredSize(d);
		
		//ACTION LISTENER
		addMouseListener(new MouseListener() {				
			@Override
			public void mouseClicked(MouseEvent arg0) {
				board.thisGame.warningsDisplay.setText("");
				board.previewPiece = null;
				
				if (piece.getVisibility()) {
					board.invalidClickAction();
				}
				else {	
					//depending on whose turn it is, set color of piece to be placed
					if (GameBoard.turn == Turn.Black)
						piece.setColor(true);
					else
						piece.setColor(false);
					
					int rowPlacement = piece.getRowNum();
					int colPlacement = piece.getColNum();
					
					LinkedList<GamePiece> validDirections = checkValidClick(piece,rowPlacement,colPlacement);
					int numDirections = validDirections.size();
					if (numDirections != 0) { //if the list of valid directions is not empty

						//1) make the piece played visible
						piece.setVisible();
						
						//2) flip all pieces that the played piece affects
						for (int i = 0; i < numDirections; i++) {
							flipAllPieces(validDirections.get(i),rowPlacement,colPlacement);
						}
						
						//3) remove the square it is on from the emptySquares array
						board.emptySquares[rowPlacement*8 + colPlacement] = null;
						
						//4) change number of pieces played, and then update turn

						if (GameBoard.turn == Turn.White) {
							board.numWhitePlayed++;
						}			
						else if (GameBoard.turn == Turn.Black) {
							board.numBlackPlayed++;
						}
						board.updateTurn();

						
						//5) update labels for pieces played
						board.updatePiecesLabel();

						//this calls the GameBoard's paintComponent method, which in turn
						//redraws all of the pieces
						board.repaint();
						
						for (int i = 0; i < 64; i++) {
							if (board.emptySquares[i] != null)
								System.out.println("emptySquares[" + i + "] is empty");
						}
						
						for (int l = 0; l < 8; l++) {
							for (int m = 0; m < 8; m++) {
								if (!board.pieces[l][m].getColorBoolean())
									System.out.println("The color of piece at col " + l + " and row " + m +
										"is " + board.pieces[l][m].getColor());
							}
						}
						System.out.println("***********");

					}
					
					else { 
						board.invalidClickAction();
					}
				}
			}
				
			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
        });
	}
	

	//checks if that space is a valid click, and then proceeds accordingly
	public LinkedList<GamePiece> checkValidClick(GamePiece p, int r, int c) {

		//array for storing any surrounding pieces that are found to make this a valid click
		LinkedList<GamePiece> validDirections = new LinkedList<GamePiece>();
		
		//check all squares surrounding the selected square
		for (int i = c-1; i <= c+1; i++) {
			for (int j = r-1; j <= r+1; j++) {
				//in case some surrounding squares don't exist (i.e. b/c it is an edge piece)
				if (!indexInBounds(i,j))
					continue;
				if (i == c && r == j)
					continue;
				//if a surrounding piece is visible and is of the opponent's color, check further
				if (board.pieces[i][j].getColor() != p.getColor() && board.pieces[i][j].getVisibility()) {
					if (checkFurtherPieces(r,c,j,i,p)) {
						validDirections.add(board.pieces[i][j]);
					}
				}
			}
		}
		return validDirections;

	}
	
	public boolean checkFurtherPieces(int squareTriedRow, int squareTriedCol, 
			int nextSquareRow, int nextSquareCol, GamePiece originPiece) {
		//calculate locational relationship between the square tried and the next square
		//being tested
		int colDiff = nextSquareCol-squareTriedCol;
		int rowDiff = nextSquareRow-squareTriedRow;
		int nexti = nextSquareCol + colDiff;
		int nextj = nextSquareRow + rowDiff;
		//System.out.println("row Diff is " + rowDiff);
		//System.out.println("col Diff is " + colDiff);
		
		while(indexInBounds(nexti,nextj)) {
			//System.out.println("in further pieces while loop");
			GamePiece next = board.pieces[nexti][nextj];
			//if arrive at an empty square before reaching an opposite color square, it is invalid
			if (!next.getVisibility()) {
				return false;
			}
			//if reach an opposite color square before reaching an empty square, it is valid
			if (next.getColor() == originPiece.getColor()) {
				//System.out.println("REACHED PIECE OF SAME COLOR TO CREATE SANDWICH");
				//System.out.println("sandwiching piece is at row " + nextj + " and col " + nexti);
				//System.out.println("color of sandwiching piece is " + next.getColor());
				return true;
			}
			nexti += colDiff;
			nextj += rowDiff;
		}
		return false;
		
	}
	
	public void flipAllPieces(GamePiece p, int r, int c) {//p is one piece away from the center
		Color startingColor = p.getColor();
		
		boolean startingBoolean = p.getColorBoolean();
		System.out.println("starting color boolean in flipAllPieces is " + startingBoolean);
		int squareTriedRow = r;
		int squareTriedCol = c;

		int nextSquareRow = p.getRowNum();
		int nextSquareCol = p.getColNum(); 

		
		int rowDiff = nextSquareRow-squareTriedRow;
		int colDiff = nextSquareCol-squareTriedCol;
		int nexti = nextSquareCol;
		int nextj = nextSquareRow;		
		
		while (true) {
			board.pieces[nexti][nextj].setColor(!startingBoolean);
			if (GameBoard.turn == Turn.Black) {
				board.numBlackPlayed++;
				board.numWhitePlayed--;
			}
			else {
				board.numBlackPlayed--;
				board.numWhitePlayed++;
			}

			nexti += colDiff;
			nextj += rowDiff;
			if (!indexInBounds(nexti,nextj))
				break;

			if (board.pieces[nexti][nextj].getColor() != startingColor)
				break;
		}
		board.updatePiecesLabel();
	}	

	public static boolean indexInBounds(int x, int y) {
		return (x >= 0 && x <= 7 && y >= 0 && y <= 7);
	}
	
	
	public void paint(Graphics gc) {
		gc.setColor(Color.GREEN.darker());
		gc.fillRect(colnum, rownum, SQUARE_DIM, SQUARE_DIM);
		if (piece.getVisibility())
			piece.draw(gc);
		repaint();
	}
		

}
