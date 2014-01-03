import java.awt.Point;
import java.util.ArrayList;




public class Snake {
	
	private String currentDirection; //step button
	private ArrayList<Point> positions = new ArrayList<Point>(); // ???
	private Point nextPositionOfHead = new Point();
	
	
	private int length;

	public Snake(){
		positions.add(new Point(2, 3));
		positions.add(new Point(3, 3));
		positions.add(new Point(4, 3));
		positions.add(new Point(5, 3));
		positions.add(new Point(6, 3));
		
		currentDirection = Constants.UP;
		
		this.setLength(5); 
	}
		public boolean Move(String direction, Apple currentApple) {

			if (direction.equals(Constants.UP)) {

				Point newPoint = new Point(positions.get(0).x - 1,
						positions.get(0).y);
				if (!eatingMyself(newPoint)) {
					if (headIsEatingApple(currentApple)) {
						positions.add(currentApple);
						length++;
					} // si motivul..stai
					
					recalculatePositions();
					currentDirection = Constants.UP;
					return true;
				} else
					return false;

			}
			if (direction.equals(Constants.RIGHT)) {

				Point newPoint = new Point(positions.get(0).x ,
						positions.get(0).y+1);
				if (!eatingMyself(newPoint)) {
					if (headIsEatingApple(currentApple)) {
						positions.add(currentApple);
						length++;
					}
					
					recalculatePositions();
					currentDirection = Constants.RIGHT;
					return true;
				} else
					return false;

			}
			if (direction.equals(Constants.LEFT)) {

				Point newPoint = new Point(positions.get(0).x ,
						positions.get(0).y-1);
				if (!eatingMyself(newPoint)) {
					if (headIsEatingApple(currentApple)) {
						positions.add(currentApple);
						length++;
					}
					
					recalculatePositions();
					currentDirection = Constants.LEFT;
					return true;
				} else
					return false;

			}
			if (direction.equals(Constants.DOWN)) {

				Point newPoint = new Point(positions.get(0).x+1,
						positions.get(0).y);
				if (!eatingMyself(newPoint)) {
					if (headIsEatingApple(currentApple)) {
						positions.add(currentApple);
						length++;
					}
					
					recalculatePositions();
					currentDirection = Constants.DOWN;
					return true;
				} else
					return false;

			}
		/*	if (direction.equals(Constants.STEP)) {

				Point newPoint = new Point(positions.get(0).x - 1,
						positions.get(0).y);
				if (!eatingMyself(newPoint)) {
					if (headIsEatingApple(currentApple)) {
						positions.add(currentApple);
						length++;
					}
					
					recalculatePositions();
					currentDirection = Constants.STEP;
					return true;
				} else
					return false;
				}
					*/
			if (direction.equals(Constants.STEP)) {
				return Move(currentDirection, currentApple);
			} else
				return false;
			}
			
			private boolean headIsEatingApple(Apple currentApple) {
				return (nextPositionOfHead.x == currentApple.x && nextPositionOfHead.y == currentApple.y);
			}
			
			//Recalculates the positions of the snake after a move. 
			private void recalculatePositions() {
				for (int i = length - 1; i > 0; i--) {
					positions.set(i,new Point(positions.get(i - 1).x, positions.get(i - 1).y)); //!???
				}
				normalizePoint(nextPositionOfHead);
				positions.set(0, nextPositionOfHead);
			}

			private void normalizePoint(Point newHead) {
				
					if(newHead.x <0){
						newHead.x = Constants.BOARD_SIZE-1;
					}
					if(newHead.x<0){
						newHead.y = Constants.BOARD_SIZE-1;
					}
					if(newHead.x > Constants.BOARD_SIZE-1){
						newHead.x=0;
					}
					if(newHead.y> Constants.BOARD_SIZE-1){
						newHead.y=0;
					}
			}

			private boolean eatingMyself(Point newPoint) {
				boolean eating = false;

				for (int i = 1; i < length; i++)
					if (newPoint.x == positions.get(i).x && newPoint.y == positions.get(i).y)
						eating = true;
				return eating;
			}

			public ArrayList<Point> returnPositions() {
				return positions;
			}

			public int getLength() {
				return length;
			}

			public void setLength(int length) {
				this.length = length;
			}
		}

	