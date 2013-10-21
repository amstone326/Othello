import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JButton;
import javax.swing.JComponent;

@SuppressWarnings("serial")
public class GamePiece extends JComponent {
	
	private boolean isPreview;
	private boolean isVisible;
	private boolean isBlack;
	private int rownum;
	private int colnum;
	public BoardSquare location; //the square where this piece is located

	
	public GamePiece(int y, int x, boolean b) {
		isVisible = false;
		isBlack = b;
		rownum = y;
		colnum = x;
	}
	
	/*
	//overloads Piece construction for purposes of preview feature
	public GamePiece(int y, int x, boolean b, boolean isprev) {
		isVisible = true;
		isBlack = b;
		rownum = y;
		colnum = x;
		isPreview = isprev;
	}
	*/
	
	public boolean getVisibility() {
		return isVisible;
	}
	
	public void setVisibility(boolean b) {
		isVisible = b;
	}
	
	public void setVisible() {
		isVisible = true;
	}
	
	public void setBoardLocation(BoardSquare b) { 
		location = b;
	}
			
	public boolean getColorBoolean() {
		return isBlack;
	}
	
	public Color getColor() {
		if (isBlack) 
			return Color.BLACK;
		else 
			return Color.WHITE;
	}
	
	public void setColor(boolean b) {
		isBlack = b;
	}
	
	public int getRowNum() {
		return rownum;
	}
	
	public int getColNum() {
		return colnum;
	}
	
	public void draw(Graphics gc) {
		if (!isPreview) {
			gc.setColor(getColor());
			gc.fillOval(rownum, colnum, BoardSquare.SQUARE_DIM, BoardSquare.SQUARE_DIM);
		} else {
			System.out.println("drawPreview is entered");
			gc.setColor(Color.RED);
			gc.drawOval(rownum, colnum, BoardSquare.SQUARE_DIM, BoardSquare.SQUARE_DIM);
		}
	}
	
}
