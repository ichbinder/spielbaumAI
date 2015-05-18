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
	private List<TreeNode> BestLeafs = new Vector<TreeNode>(6);
	
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
	
	private final int ComputeTreeLevel(int MaxPlayerRounds, TreeNode Parent)
	{
		//Abort if leaf is reached
		if(MaxPlayerRounds == 0)
		{
			return Parent.Value;
		}
		
		boolean IsPlayerMove = Board.IsPlayersView();
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
		
		if(IsPlayerMove && (Parent != null));
		{
			MaxPlayerRounds--;
		}
		
		// Make Rekursive Call through List
		ListIterator<TreeNode> It = SortedList.listIterator();
		int ValueBuffer = IsPlayerMove ? Integer.MIN_VALUE : Integer.MAX_VALUE;
		Board.RotateBoard();
		while(It.hasNext())
		{
			TreeNode Node = It.next();
			int Value = ComputeTreeLevel(MaxPlayerRounds, Node);
			//If Player turn return best value & save move when root node
			if(IsPlayerMove && (Value >= ValueBuffer))
			{
				//If Root Child add it as possible move
				if(Parent == null)
				{
					//If better than previous move wipe it
					if(Value > ValueBuffer)
					{
						BestLeafs.clear();
					}
					
					BestLeafs.add(Node);
				}
				
				ValueBuffer = Value;
			}

			//If not Player turn return worst value
			if(!IsPlayerMove && (Value < ValueBuffer))
			{
				ValueBuffer = Value;
			}
		}
		Board.Pop();
		
		return ValueBuffer;
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
