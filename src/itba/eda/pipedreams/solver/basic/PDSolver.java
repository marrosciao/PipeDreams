package itba.eda.pipedreams.solver.basic;

import itba.eda.pipedreams.solver.board.BasicBoard;
import itba.eda.pipedreams.solver.board.Board;
import itba.eda.pipedreams.solver.engine.Engine;
import itba.eda.pipedreams.solver.pipe.PipeBox;

public class PDSolver { // Front/Back end mediator
	private final BoardDisplay display;
	private final PipeBox pipes; // TODO: Interfaces?
	private final BasicBoard board; // TODO: Interfaces?
	private final Engine engine;

	public PDSolver(PDSolverArgs args, BoardDisplay display) { // TODO: Create PDSolverArguments here?
		this.pipes = new PipeBox(args.getPipeSizes());
		this.board = new Board(args.getBoardFile());
		this.engine = new Engine(board, args.getMethod(), args.getTime(), args.withProgress(), pipes);
		this.display = display;

		this.display.setBoard(board);
		this.board.addObserver(display);
	}

	public void start() {
		Thread thread = new Thread(engine);
		thread.start();
	}
}
