package itba.eda.pipedreams.solver.engine;

import itba.eda.pipedreams.solver.Method;
import itba.eda.pipedreams.solver.board.BasicBoard;
import itba.eda.pipedreams.solver.pipe.Pipe;
import itba.eda.pipedreams.solver.pipe.PipeBox;
import itba.eda.pipedreams.solver.board.Board;
import itba.eda.pipedreams.solver.board.Dir;
import itba.eda.pipedreams.solver.board.Point;

import java.util.*;

public class Engine {

	private BasicBoard board;
	private Method method;
	private long time;
	private PipeBox pipeBox;

	private boolean iterative;

	public Engine(BasicBoard board, Method method, int time, PipeBox pipeBox) {
		this.board = board;
		this.method = method;
		this.time = Timer.convertToMiliseconds(time);
		this.pipeBox = pipeBox;
	}

	public void start() {
		switch(method) {
			case EXACT:
				backtracking();
				break;
			case APROX:
				hillClimbing();
				break;
		}
	}

	private Deque<Pipe> hillClimbing() {
		Deque<Pipe> currSolution = new LinkedList<Pipe>();
		Deque<Pipe> bestSolution = new LinkedList<Pipe>();
		Timer timer = new Timer();

		timer.startClock();

		if(!findFirstSolution(Board.getNext(board.getStartPoint().clone(), board.getStartFlow()), board.getStartFlow(), currSolution)) {
			return null;
		}

		copyQueue(currSolution, bestSolution);

		while(timer.getRunningTime() < time) {
			currSolution = findBestNeighbor(currSolution);
			if(currSolution.size() > bestSolution.size()) {
				copyQueue(currSolution, bestSolution);
			} else {
				return currSolution;
			}
		}
		timer.stopClock();

		return bestSolution;
	}

	private Deque<Pipe> findBestNeighbor(Deque<Pipe> currSolution) {
//		Deque<Point> bestNeighbor = new LinkedList<Point>();
//
//		for(Point currPos : currSolution) {
//			if(Solution.isApplicable(board, currPos)) {
//				setPossibleSolution(board, currPos);
//			}
//		}

		return null;
	}

	private void backtracking() {
		Timer timer = new Timer();
		timer.startClock();
		if(iterative) {

		} else {
			Deque<Pipe> longestPath = new LinkedList<Pipe>();
			Deque<Pipe> currPath = new LinkedList<Pipe>();
			backtrackingRec(Board.getNext(board.getStartPoint().clone(), board.getStartFlow()), board.getStartFlow(), currPath, longestPath);
			System.out.println(board);
		}
		timer.stopClock(); //TODO Preguntar si deberia ir en start()
	}

	private void backtrackingRec(Point point, Dir to, Deque<Pipe> currentPath, Deque<Pipe> longestPath) {
		Dir from = to.opposite();

		System.out.println(board);

		if(!board.withinLimits(point)) {
			if(currentPath.size() > longestPath.size()) {
				copyQueue(currentPath, longestPath);
			}
			return;
		}

		if(!board.isEmpty(point)) {
			if(!board.isBlocked(point, from)) {
				Pipe pipe = board.getPipe(point);
				currentPath.push(pipe);

				backtrackingRec(Board.getNext(point, pipe.flow(from)), pipe.flow(from), currentPath, longestPath);
				Board.getPrevious(point, pipe.flow(from));

				currentPath.pop();
			}
			return;
		}

		for(int i = 0; i < pipeBox.length(); i++) {
			Pipe pipe = pipeBox.getPipe(i);
			int size = pipeBox.getSize(i);
			Dir flow = pipe.flow(from);

			if(flow != null && size > 0) {
				pipeBox.removeOnePipe(i);
				board.putPipe(pipe, point);
				currentPath.push(pipe);

				backtrackingRec(Board.getNext(point, pipe.flow(from)), pipe.flow(from), currentPath, longestPath);
				Board.getPrevious(point, pipe.flow(from));

				if(bestSolution(longestPath)) {
					return;
				}

				currentPath.pop();
				board.removePipe(point);
				pipeBox.addOnePipe(i);
			}
		}

	}

	private boolean bestSolution(Deque<Pipe> longestPath) {
		return longestPath.size() == pipeBox.getLongestPossible();
	}

	private boolean findFirstSolution(Point point, Dir to, Deque<Pipe> currentPath) {
		Dir from = to.opposite();

		System.out.println(board);

		if(!board.withinLimits(point)) {
			return true;
		}

		if(!board.isEmpty(point)) {
			if(!board.isBlocked(point, from)) {
				Pipe pipe = board.getPipe(point);
				currentPath.push(pipe);

				if(findFirstSolution(Board.getNext(point, pipe.flow(from)), pipe.flow(from), currentPath)) {
					return true;
				}
				Board.getPrevious(point, pipe.flow(from));

				currentPath.pop();
			}
			return false;
		}

		for(int i = 0; i < pipeBox.length(); i++) {
			Pipe pipe = pipeBox.getPipe(i);
			int size = pipeBox.getSize(i);
			Dir flow = pipe.flow(from);

			if(flow != null && size > 0) {
				pipeBox.removeOnePipe(i);
				board.putPipe(pipe, point);
				currentPath.push(pipe);

				if(findFirstSolution(Board.getNext(point, pipe.flow(from)), pipe.flow(from), currentPath)) {
					return true;
				}
				Board.getPrevious(point, pipe.flow(from));

				currentPath.pop();
				board.removePipe(point);
				pipeBox.addOnePipe(i);
			}
		}
		return false;
	}

	private <T> void copyQueue(Queue<T> from, Queue<T> to) { // TODO: Better way?
		to.clear();

		for(T aux : from) {
			to.add(aux);
		}
	}
}