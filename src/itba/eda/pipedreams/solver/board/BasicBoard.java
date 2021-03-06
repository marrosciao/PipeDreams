package itba.eda.pipedreams.solver.board;

import itba.eda.pipedreams.solver.basic.GameBoard;
import itba.eda.pipedreams.solver.basic.Point;
import itba.eda.pipedreams.solver.pipe.Pipe;

import java.util.Iterator;
import java.util.Observable;

/**
 * The basic structure of a game board. It has the main methods that every implementation should have.
 */

public abstract class BasicBoard extends Observable implements GameBoard {
	protected String[] file;

	protected Point startPoint;
	protected Dir startFlow;

	public abstract Pipe getPipe(Point point);
	public abstract boolean putPipe(Pipe pipe, Point point);
	public abstract boolean removePipe(Point point);
	protected abstract boolean setPiece(char c, int row, int column);

	public abstract boolean isEmpty(Point point);
	public abstract boolean hasPipe(Point point);
	public abstract boolean isBlocked(Point point, Dir from);

	public abstract boolean withinLimits(Point point);

	public Point getStartPoint() {
		return startPoint.clone();
	}

	public Dir getStartFlow() {
		return startFlow;
	}

	public abstract boolean draw(Iterator<Pipe> it);

	public void clear() {
		for(int i = 0; i < file.length; i++) {
			for(int j = 0; j < file[0].length(); j++) {
				setPiece(file[i].charAt(j), i, j);
			}
		}
	}

	protected interface BasicTile {
		Pipe getPipe();
	}
}
