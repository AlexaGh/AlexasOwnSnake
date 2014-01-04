import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class SnakeBoard extends JFrame implements MouseListener, KeyListener {
	private JPanel base = new JPanel();
	private JPanel leftGameBoard = new JPanel();
	private JPanel rightBoard = new JPanel();
	private JPanel innerRightControls = new JPanel();
	private JLabel[][] squares = new JLabel[Constants.BOARD_SIZE][Constants.BOARD_SIZE];
	private JButton[][] controls = new JButton[3][3];

	Apple currentApple = new Apple();
	private int score=0;
	private Snake snake;
	
	SnakeBoard() {
		super("Snake!");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(700, 500)); // preferredSize
		setContentPane(base);
		base.setLayout(new BorderLayout());

		LineBorder lb = new LineBorder(Color.BLACK);

		leftGameBoard.setLayout(new GridLayout(Constants.BOARD_SIZE,
				Constants.BOARD_SIZE));

		rightBoard.setBorder(lb);
		leftGameBoard.setPreferredSize(new Dimension(250, 500));

		rightBoard.setLayout(new GridLayout(3, 3));
		innerRightControls.setLayout(new GridLayout(3, 3));

		// 1
		for (int i = 0; i < Constants.BOARD_SIZE; i++)
			for (int j = 0; j < Constants.BOARD_SIZE; j++) {
				squares[i][j] = new JLabel();
				squares[i][j].setBackground(Color.LIGHT_GRAY);
				squares[i][j].setOpaque(true);
				leftGameBoard.add(squares[i][j]);
				squares[i][j].setBorder(lb);
			}
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++) {
				controls[i][j] = new JButton();
				rightBoard.add(controls[i][j]);
				innerRightControls.add(controls[i][j]);
				controls[i][j].setBorder(lb);
				controls[i][j].addMouseListener(this);
			}
		rightBoard.add(innerRightControls);
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++) {
				if (i == 1 && j == 1) // uhm..
					rightBoard.add(innerRightControls);
				else
					rightBoard.add(new JLabel());
			}

		{
			controls[0][0].setEnabled(false);
			controls[0][0].setBackground(Color.WHITE);
			controls[0][2].setEnabled(false);
			controls[0][2].setBackground(Color.WHITE);
			controls[2][0].setEnabled(false);
			controls[2][0].setBackground(Color.WHITE);
			controls[2][2].setEnabled(false);
			controls[2][2].setBackground(Color.WHITE);
		}
		{
			controls[0][1].setBackground(Color.LIGHT_GRAY);
			controls[0][1].setText(Constants.UP);
			controls[1][0].setBackground(Color.LIGHT_GRAY);
			controls[1][0].setText(Constants.LEFT);
			controls[1][1].setBackground(Color.LIGHT_GRAY);
			controls[1][1].setText(Constants.STEP);
			controls[1][2].setBackground(Color.LIGHT_GRAY);
			controls[1][2].setText(Constants.RIGHT);
			controls[2][1].setBackground(Color.LIGHT_GRAY);
			controls[2][1].setText(Constants.DOWN);
		}
		snake = new Snake();
		generateApple();
		updateBoard(snake.returnPositions());
		base.addKeyListener(this);
		base.setFocusable(true);

		base.add(leftGameBoard, BorderLayout.WEST);
		base.add(rightBoard, BorderLayout.CENTER);

		controls[0][0].setText(String.valueOf(score));
		pack();
	}

	public void moveSnake(String direction) {
		// if eating himself
		if (!snake.Move(direction, currentApple)) {
			JOptionPane.showMessageDialog(null, "You lost");
			System.exit(0);
		} else {
			updateBoard(snake.returnPositions());
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == controls[0][1]) {
			moveSnake(Constants.UP);
		}
		if (e.getSource() == controls[1][1]) {
			moveSnake(Constants.STEP);
		}
		if (e.getSource() == controls[1][2]) {
			moveSnake(Constants.RIGHT);
		}
		if (e.getSource() == controls[1][0]) {
			moveSnake(Constants.LEFT);
		}
		if (e.getSource() == controls[2][1]) {
			moveSnake(Constants.DOWN);
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		switch (keyCode) {
		case KeyEvent.VK_UP:
			moveSnake(Constants.UP);
			break;
		case KeyEvent.VK_DOWN:
			moveSnake(Constants.DOWN);
			break;
		case KeyEvent.VK_LEFT:
			moveSnake(Constants.LEFT);
			break;
		case KeyEvent.VK_RIGHT:
			moveSnake(Constants.RIGHT);
			break;
		}
	}

	private void generateApple() {

		Random r = new Random();
		int x = r.nextInt(Constants.BOARD_SIZE);
		int y = r.nextInt(Constants.BOARD_SIZE);

		currentApple = new Apple();
		currentApple.setLocation(x, y);

		int nrOfTries = 0;

		while (appleIntersectsWithSnake()) {

			Random r2 = new Random();
			int x2 = r2.nextInt(Constants.BOARD_SIZE);
			int y2 = r2.nextInt(Constants.BOARD_SIZE);
			currentApple.setLocation(x2, y2);
			nrOfTries++;
			if (nrOfTries > 100000) {
				JOptionPane.showMessageDialog(null, "You won!");
				System.exit(0);
			}
		}
	}

	private boolean appleIntersectsWithSnake() {
		boolean intersects = false;

		for (int i = 0; i < snake.getLength(); i++) {
			if (snake.returnPositions().get(i).x == currentApple.x
					&& snake.returnPositions().get(i).y == currentApple.y){
				return true;
			}
		}
		return intersects;
	}
	//
	//partea aia nu am facut-o inca
	/// |
	// nu mai trebuie facut nimic jos
 
	/**
	 * Updates the GUI with the new coordinates of the snake
	 * 
	 * @param points
	 *            the points coming from the snake's body coordinates
	 */
	private void updateBoard(ArrayList<Point> points) {
		resetBoard();
		for (int i = 0; i < snake.getLength(); i++)
			if (i == 0) {
				// special case for head
				squares[points.get(i).x][points.get(i).y]
						.setBackground(Color.yellow);
			} else
				squares[points.get(i).x][points.get(i).y]
						.setBackground(Color.GREEN);

		/**
		 * extra
		 */
		// place apple
		// checking if the head is on it
		if (snake.returnPositions().get(0).x == currentApple.x
				&& snake.returnPositions().get(0).y == currentApple.y) {
			// regenerate the apple if the snake just ate it
			score++;
			controls[0][0].setText(String.valueOf(score));
			generateApple();
		}
		squares[currentApple.x][currentApple.y].setBackground(Color.RED);

	}

	// Method which resets the board.
	private void resetBoard() {
		for (int i = 0; i < Constants.BOARD_SIZE; i++)
			for (int j = 0; j < Constants.BOARD_SIZE; j++) {
				squares[i][j].setBackground(Color.LIGHT_GRAY);
			}
	}

	public static void main(String args[]) {
		SnakeBoard sb = new SnakeBoard();
		sb.setVisible(true);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
