package client.agent.gametree;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.Vector;

import lenz.htw.kimpl.Move;
import client.agent.IAgent;
import client.game.IBoard;
import evaluator.IEvaluator;

public class GameTreeAgent implements IAgent {

	private Random Rng = new Random();
	private TreeNode[] NodePool = new TreeNode[5000];
	private int NodePoolIndex = 0;
	private List<TreeNode> BestLeafs = new Vector<TreeNode>();
	
	private int Alpha = Integer.MIN_VALUE;
	private int Beta = Integer.MAX_VALUE;
	private IStackHistoryBoard Board = null;
	
	private IEvaluator Evaluator = null;

	public GameTreeAgent(IEvaluator Evaluator)
	{
		this.Evaluator = Evaluator;
	}
	
	public final Move CalculateMove(IBoard GameBoard) {
		Reset();

		Board.Setup(GameBoard);
		
		//Start GameTree Calculation
		ComputeTreeLevel(1, null);
		
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
	
	private final void ComputeTreeLevel(int MaxPlayerRounds, TreeNode Parent)
	{
		LinkedList<TreeNode> SortedList = new LinkedList<TreeNode>();
		byte[][] PiecePositions = Board.GetPiecePositions();
		
		for(int i = 0; i < 6; i++)
		{
			byte[] Position = PiecePositions[i];
			if(Position[0] > 7)
			{
				continue;
			}
			
			if(Board.MovePieceForward(Position[0], Position[1]))
			{
				SetupNode(SortedList, Parent, Position[0], Position[1], Position[0], Position[1] + 1);
			}
			
			if(Board.MovePieceLeft(Position[0], Position[1]))
			{
				SetupNode(SortedList, Parent, Position[0], Position[1], Position[0] - 1, Position[1] + 1);
			}
			
			if(Board.MovePieceRight(Position[0], Position[1]))
			{
				SetupNode(SortedList, Parent, Position[0], Position[1], Position[0] + 1, Position[1] + 1);
			}
		}
		
		if(Board.IsPlayersView() && (Parent != null));
		{
			MaxPlayerRounds--;
		}
		
		if(MaxPlayerRounds > 0)
		{
			// Make Rekursive Call through List
			ListIterator<TreeNode> It = SortedList.listIterator();
			Board.RotateBoard();
			while(It.hasNext())
			{
				TreeNode Node = It.next();
				ComputeTreeLevel(MaxPlayerRounds, Node);
			}
			Board.Pop();
		}
		else
		{
			//if last level reached run Alpha-Beta/Min-Max
				
		}
	}
	
	private final void SetupNode(LinkedList<TreeNode> SortedList, TreeNode Parent, int fromX, int fromY, int ToX, int ToY)
	{
		int Value =  Evaluator.Evaluate(Board);
		TreeNode NewNode = NodePool[NodePoolIndex++];
		NewNode.Setup(Parent, Value, fromX, fromY, ToX, ToY);
		Board.Pop();
		
		ListIterator<TreeNode> It = SortedList.listIterator();
		boolean Inserted = false;
		
		while(It.hasNext())
		{
			TreeNode Element = It.next();
			if(Element.Value <= Value)
			{
				It.add(NewNode);
				Inserted = true;
				break;
			}
		}
		
		if(!Inserted)
		{
			SortedList.add(NewNode);
		}
	}
	

}
