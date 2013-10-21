
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.io.FileNotFoundException;



import org.junit.Test;


public class GameTests {
	
	@Test
	public void test1 () {
		Game g = new Game();
		GameBoard b = new GameBoard(g);
		assertFalse(b.pieces[5][4].getVisibility());
		assertEquals(b.pieces[6][0].getColNum(),6);
		assertEquals(b.pieces[4][3].getRowNum(),3);
	}
	
	@Test
	public void testColor () {
		Game g = new Game();
		GameBoard b = new GameBoard(g);
		assertEquals(b.pieces[4][3].getColor(),Color.BLACK);
		assertEquals(b.pieces[3][4].getColor(),Color.BLACK);
		assertEquals(b.pieces[4][4].getColor(),Color.WHITE);
		assertEquals(b.pieces[3][3].getColor(),Color.WHITE);
	}
	
	@Test 
	public void test3 () {
		Game g = new Game();
		GameBoard b = new GameBoard(g);
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				assertEquals(b.pieces[i][j].getRowNum(),b.squares[i][j].rownum);
				assertEquals(b.pieces[i][j].getColNum(),b.squares[i][j].colnum);

			}
		}
	}
	
	@Test 
	public void test4 () {
		Game g = new Game();
		GameBoard b = new GameBoard(g);
		assertEquals(b.emptySquares[0].piece.getRowNum(),0);
		assertEquals(b.emptySquares[1].piece.getRowNum(),1);
		assertEquals(b.emptySquares[7].piece.getRowNum(),7);
		assertEquals(b.emptySquares[17].piece.getColNum(),2);
		assertEquals(b.emptySquares[20].piece,b.pieces[2][4]);
	}



}
