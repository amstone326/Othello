
import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class Game implements Runnable {
	
	public JLabel blackChanges = new JLabel("2");
	public JLabel whiteChanges = new JLabel("2");
	public JLabel warningsDisplay = new JLabel("");
	public JLabel turnChanges = new JLabel("");
	public JLabel moveLabel = new JLabel("");
	public GameBoard currBoard;
	public static Border basicBorder = new LineBorder(Color.BLACK,7,true);
	public JPanel bPanel = new JPanel();
	public JPanel center = new JPanel();
	public final JFrame frame = new JFrame("Othello");
	public static Icon picture = new ImageIcon("game_image.png");

	public void run() {
		
		//TOP LEVEL FRAME
		frame.setResizable(true);
		frame.setLocation(300,200);
		frame.setPreferredSize(new Dimension(650,500));
		
		//TOP LEVEL MENU BAR
		JMenuBar mb = new JMenuBar();
		frame.setJMenuBar(mb);
		JMenu main = new JMenu("File");
		mb.add(main);
		
		//ADD ACTION LISTENERS here
		JMenuItem newGame = new JMenuItem("New Game");
		JMenuItem instructions = new JMenuItem("Instructions");
		main.add(newGame);
		main.add(instructions);
		instructions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Instructions i = new Instructions();
				JOptionPane.showMessageDialog(frame, i.getMessage(),"INSTRUCTIONS",1,picture);
			}
		});
		newGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reset();
			}
		});
		
		//BORDER LAYOUT PANEL
		frame.add(bPanel);
		bPanel.setLayout(new BorderLayout());		
		center.setLayout(new BorderLayout());
		bPanel.add(center,BorderLayout.CENTER);
		
		GameBoard board = new GameBoard(this);
		currBoard = board;
		center.add(board,BorderLayout.CENTER);
				
		//RIGHT PANEL for keeping track of turn, and number of each pieces that have been played
		JPanel right = new JPanel();
		right.setLayout(new GridLayout(3,1));
		bPanel.add(right,BorderLayout.LINE_END);
		
		//3 panels in right
		JPanel rightTop = new JPanel();
		JPanel rightMid = new JPanel();
		JPanel rightBottom = new JPanel();
		rightTop.setBackground(Color.GRAY);
		rightMid.setBackground(Color.GRAY);
		rightBottom.setBackground(Color.GRAY);
		//top
		right.add(rightTop);
		rightTop.setBorder(basicBorder);
		rightTop.setLayout(new GridLayout(3,1));
		//mid
		right.add(rightMid);
		rightMid.setLayout(new GridLayout(4,1));
		rightMid.setBorder(basicBorder);
		//bottom
		right.add(rightBottom);
		rightBottom.setLayout(new GridLayout(2,1));
		rightBottom.setBorder(basicBorder);
		
		addLabels(rightTop,rightMid,rightBottom);
		
		
		//to add to rightBottom
		JButton bestMove = new JButton ("Show My Best Move");
		rightBottom.add(bestMove);
		rightBottom.add(moveLabel);
		moveLabel.setHorizontalAlignment(SwingConstants.CENTER);
		moveLabel.setVerticalAlignment(SwingConstants.CENTER);
		bestMove.setVerticalAlignment(SwingConstants.CENTER);
		

		bestMove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Turn currTurn = GameBoard.turn;
				if (currTurn == Turn.Black && currBoard.blackUsedBestMove)
					warningsDisplay.setText("The current player has already used their best move hint!");
				else if (currTurn == Turn.White && currBoard.whiteUsedBestMove)
					warningsDisplay.setText("The current player has already used their best move hint!");
				else {
					GamePiece goHere = currBoard.getBestMove();
					if (goHere == null)
						warningsDisplay.setText("The current player has no valid moves!");
					else {
						int row = goHere.getRowNum();
						int col = goHere.getColNum();
						//System.out.println("bestMove row is " + row);
						//System.out.println("bestMove col is " + col);
						String s;
						if (col == 0)
							s = "A";
						else if (col == 1)
							s = "B";
						else if (col == 2)
							s = "C";
						else if (col == 3)
							s = "D";
						else if (col == 4)
							s = "E";
						else if (col == 5)
							s = "F";
						else if (col == 6)
							s = "G";
						else
							s = "H";
						moveLabel.setText(s + Integer.toString(row));
					}
				}
				currBoard.repaint();
					
			}
		});
		
		
		//BOTTOM PANEL for board status
		JPanel bottom = new JPanel();
		bPanel.add(bottom,BorderLayout.PAGE_END);
		bottom.add(warningsDisplay);
		
		//LEFT PANEL FOR NUMBER LABELINGS
		JPanel left = new JPanel();
		left.setLayout(new GridLayout(8,1));
		center.add(left, BorderLayout.LINE_START);
		
		JLabel left0 = new JLabel("0");
		left.add(left0);
		JLabel left1 = new JLabel("1");
		left.add(left1);
		JLabel left2 = new JLabel("2");
		left.add(left2);
		JLabel left3 = new JLabel("3");
		left.add(left3);
		JLabel left4 = new JLabel("4");
		left.add(left4);
		JLabel left5 = new JLabel("5");
		left.add(left5);
		JLabel left6 = new JLabel("6");
		left.add(left6);
		JLabel left7 = new JLabel("7");
		left.add(left7);
		
		JPanel top = new JPanel();
		top.setLayout(new GridLayout(1,8));
		center.add(top, BorderLayout.PAGE_START);
		
		JLabel alabel = new JLabel("A");
		alabel.setHorizontalAlignment(SwingConstants.CENTER);
		top.add(alabel);
		JLabel blabel = new JLabel("B");
		blabel.setHorizontalAlignment(SwingConstants.CENTER);
		top.add(blabel);
		JLabel clabel = new JLabel("C");
		clabel.setHorizontalAlignment(SwingConstants.CENTER);
		top.add(clabel);
		JLabel dlabel = new JLabel("D");
		dlabel.setHorizontalAlignment(SwingConstants.CENTER);
		top.add(dlabel);
		JLabel elabel = new JLabel("E");
		elabel.setHorizontalAlignment(SwingConstants.CENTER);
		top.add(elabel);
		JLabel flabel = new JLabel("F");
		flabel.setHorizontalAlignment(SwingConstants.CENTER);
		top.add(flabel);
		JLabel glabel = new JLabel("G");
		glabel.setHorizontalAlignment(SwingConstants.CENTER);
		top.add(glabel);
		JLabel hlabel = new JLabel("H");		
		hlabel.setHorizontalAlignment(SwingConstants.CENTER);
		top.add(hlabel);

		Instructions i = new Instructions();
		JOptionPane.showMessageDialog(frame, i.getMessage(),"INSTRUCTIONS",1,picture);
		
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public void addLabels(JPanel rightTop, JPanel rightMid, JPanel rightBottom) {
		Font forLabels = new Font("sansserif",Font.ITALIC,17);
		Font bigger = new Font ("sansserif",Font.BOLD,27);
		JLabel turnLabel1 = new JLabel("It is Currently");
		JLabel turnLabel2 = new JLabel("Turn");
		turnLabel1.setFont(forLabels);
		turnLabel2.setFont(forLabels);
		rightTop.add(turnLabel1);
		rightTop.add(turnChanges);
		rightTop.add(turnLabel2);
		turnLabel1.setHorizontalAlignment(SwingConstants.CENTER);
		turnLabel2.setHorizontalAlignment(SwingConstants.CENTER);
		turnChanges.setHorizontalAlignment(SwingConstants.CENTER);


		JLabel blackPerm = new JLabel("Black Pieces Played: ");
		JLabel whitePerm = new JLabel("White Pieces Played: ");
		blackPerm.setFont(forLabels);
		whitePerm.setFont(forLabels);
		blackPerm.setHorizontalAlignment(SwingConstants.CENTER);
		whitePerm.setHorizontalAlignment(SwingConstants.CENTER);
		blackChanges.setHorizontalAlignment(SwingConstants.CENTER);
		whiteChanges.setHorizontalAlignment(SwingConstants.CENTER);

		rightMid.add(blackPerm);
		rightMid.add(blackChanges);
		rightMid.add(whitePerm);
		rightMid.add(whiteChanges);
		
		turnChanges.setFont(bigger);
		blackChanges.setFont(bigger);
		whiteChanges.setFont(bigger);
		moveLabel.setFont(bigger);
		
		Font forWarnings = new Font("sansserif",Font.ITALIC,15);
		warningsDisplay.setFont(forWarnings);
		warningsDisplay.setForeground(Color.RED);
		whitePerm.setForeground(Color.WHITE);
		whiteChanges.setForeground(Color.WHITE);
	}
	
	public void reset() {
		center.remove(currBoard);
		currBoard = new GameBoard(this);
		center.add(currBoard);
		blackChanges.setText("2");
		whiteChanges.setText("2");
		turnChanges.setText("BLACK's");
		turnChanges.setForeground(Color.BLACK);
		warningsDisplay.setText("");
		frame.pack();
		frame.repaint();
		currBoard.repaint();
	}
	
    public static void main(String[] args){
        SwingUtilities.invokeLater(new Game());
    }

}
