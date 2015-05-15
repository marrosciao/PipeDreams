package itba.eda.pipedreams.enginelogic;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

import itba.eda.pipedreams.pipelogic.Pipe;
import itba.eda.pipedreams.pipelogic.PipeBox;
import itba.eda.pipedreams.pipelogic.PipeFactory;
import itba.eda.pipedreams.tablelogic.Board;
import itba.eda.pipedreams.tablelogic.Dir;
import itba.eda.pipedreams.tablelogic.Tile;

public class Engine {
	
	private Algorithm used_algorithm;
	
	private PipeBox pipeBox;
	private Board board;
	Timer timer = new Timer();
	
	public Engine(Algorithm alg, PipeBox pbox) {
		pipeBox = pbox;
		board = Board.getInstance();
		used_algorithm = alg;
	}
	
	public void start() {
		
		long running_time;
		
		//TODO: Notify frontend observers
		Deque<Pipe> longestPath = new LinkedList<Pipe>();
		Deque<Pipe> currPath = new LinkedList<Pipe>();
		
		Tile origin = board.getTile(board.getXFlow(), board.getYFlow());
		
		timer.startClock();
		
		switch (used_algorithm){
			
			case RecursiveBacktracking:
				RecursiveBacktracking(origin.getNext(board.getDirFlow()), board.getDirFlow(), currPath, longestPath);
				break;
		}
		
		running_time = timer.stopClock();
		System.out.println("DEBUG> Running time: " + running_time);
		
	}
	
	public void RecursiveBacktracking(Tile destiny_tile, Dir destiny_dir, Deque<Pipe> current, Deque<Pipe> longest){
		board.print();
		Pipe new_pipe;
		Dir new_destiny;
		
		//Solution found
		if (destiny_tile == null){
			System.out.println("Solution found");
			if(current.size() > longest.size()){
				while(!longest.isEmpty())
					longest.pop();
				Copy(current, longest);
			}
			return;
		}
		
		
		//Blocked
		if (destiny_tile.isBlocked()){
			System.out.println("Blocked");
			return;
		}
		
		//There's a pipe
		if (destiny_tile.hasPipe()){
			System.out.println("Has pipe");
			if (destiny_tile.getPipe().getId() == PipeBox.CROSS_PIPE_ID){
				current.push(destiny_tile.getPipe());
				RecursiveBacktracking(destiny_tile.getNext(destiny_dir), destiny_dir, current, longest);
				current.pop();
			}
			return;
		}
		
		
		//No more pipes left
		if(pipeBox.isEmpty()){
			System.out.println("Pipebox empty");
			return;
		}
		
		for (int i = 0; i < pipeBox.getPipeSize(); i++){
			System.out.println("For");
			new_pipe = pipeBox.getItem(i);
			new_destiny = new_pipe.flow(destiny_dir);
			
			if (pipeBox.hasItem(i) && new_pipe.canFlow(destiny_dir)){
				
				pipeBox.remove(i);
				destiny_tile.setPipe(new_pipe);
				current.push(new_pipe);
				
				RecursiveBacktracking(destiny_tile.getNext(new_destiny), new_destiny, current, longest);
				
				current.pop();
				destiny_tile.removePipe();
				pipeBox.add(i);
			}
		}
		
	}
	
	public static void Copy(Queue<Pipe> from, Queue<Pipe> to) {
		for (Pipe aux: from){
			to.add(aux);
		}
		
	}
	
}
