package client.agent.gametree;

import java.util.List;
import java.util.Random;
import java.util.Vector;

import lenz.htw.kimpl.Move;
import client.agent.IAgent;
import client.game.IBoard;

public class GameTreeAgend implements IAgent {

	private Random Rng = new Random();
	private TreeNode[] NodePool = new TreeNode[5000];
	private int NodePoolIndex = 0;
	private List<TreeNode> BestLeafs = new Vector<TreeNode>();
	
	private int Alpha = Integer.MIN_VALUE;
	private int Beta = Integer.MAX_VALUE;
	private IStackHistoryBoard Board = null;

	public final Move CalculateMove(IBoard GameBoard) {
		Reset();
		
		//GameBoart to Board;
		
		//Start GameTree Calculation
		
		//No valid move found
		if(BestLeafs.isEmpty())
		{
			//return invalid one
			return null;
		}
		else
		{
			int RandomLeaf = Rng.nextInt(BestLeafs.size());
			return LeafToMove(BestLeafs.get(RandomLeaf));
		}
	}
	
	private final Move LeafToMove(TreeNode Node) 
	{
		
		return null;
	}

	private final void Reset()
	{
		NodePoolIndex = 0;
		Alpha = Integer.MIN_VALUE;
		Beta = Integer.MAX_VALUE;
		
		BestLeafs.clear();
	}
	
	private final void ComputeTreeLevel(IStackHistoryBoard Board, int TreeLevel, int MaxRounds)
	{
		
		//Foreach Piece 
		//	calculate every possible Move
		//  Evaluate and put in in a sorted list
		
		// Make Rekursive Call through List
		
		//if last level reached run Alpha-Beta/Min-Max
	}
	

}
