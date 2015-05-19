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
	
	private IStackHistoryBoard Board = new StackHistoryBoard();
	private IEvaluator Evaluator = null;

	public GameTreeAgent(IEvaluator Evaluator)
	{
		this.Evaluator = Evaluator;
	}
	
	public final Move CalculateMove(IBoard GameBoard) {
		Reset();

		Board.Setup(GameBoard);
		
		//Start GameTree Calculation
		ComputeTreeLevel(1, null, Integer.MIN_VALUE, Integer.MAX_VALUE);
		
		//No valid move found
		if(BestLeafs.isEmpty())
		{
			//return invalid one
			return null;
		}
		else
		{
			int RandomLeaf = Rng.nextInt(BestLeafs.size());
			return BestLeafs.get(RandomLeaf).Move;
		}
	}
	
	private final void Reset()
	{
		NodePoolIndex = 0;
		BestLeafs.clear();
	}
	
	private final int ComputeTreeLevel(int MaxPlayerRounds, TreeNode Parent, int Alpha, int Beta)
	{
		//Abort if leaf is reached
		if(MaxPlayerRounds == 0)
		{
			return Parent.Value;
		}
		
		boolean IsPlayerMove = Board.IsPlayersView();
		int NodePoolIndexBuffer = NodePoolIndex;
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
		int ValueBuffer = IsPlayerMove ? Alpha : Beta;
		Board.RotateBoard();
		//If no valid move is possible
		if(SortedList.isEmpty())
		{
			if(IsPlayerMove)
			{
				ValueBuffer = Integer.MIN_VALUE;
			}
			else
			{
				Board.Forfeit();
				ValueBuffer = ComputeTreeLevel(MaxPlayerRounds, Parent, Alpha, Beta);
				Board.Pop();
			}
		}
		else 
		{
			while(It.hasNext())
			{
				TreeNode Node = It.next();
				int AlphaTmp = IsPlayerMove ? ValueBuffer : Alpha;
				int BetaTmp = !IsPlayerMove ? Beta : ValueBuffer;
				Board.DoMove(Node.Move);
				int Value = ComputeTreeLevel(MaxPlayerRounds, Node, AlphaTmp, BetaTmp);
				Board.Pop();
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
					if(ValueBuffer >= Beta)
					{
						break;
					}
				}
	
				//If not Player turn return worst value
				if(!IsPlayerMove && (Value < ValueBuffer))
				{
					ValueBuffer = Value;
					if(ValueBuffer <= Alpha)
					{
						break;
					}
				}
			}
		}
		
		Board.Pop();
		
		//Release Nodes to pool
		NodePoolIndex = NodePoolIndexBuffer;
		
		return ValueBuffer;
	}
	
	private final void SetupNode(LinkedList<TreeNode> SortedList, TreeNode Parent, int fromX, int fromY, int ToX, int ToY)
	{
		TreeNode NewNode = null;
		if(NodePoolIndex >= 5000)
		{
			NewNode = new TreeNode();
			NodePoolIndex++;
			System.out.println("Tree Node Pool too small! Current Value: " + NodePoolIndex);
		}
		else
		{
			NewNode = NodePool[NodePoolIndex++];
		}
		
		int Value =  Evaluator.Evaluate(Board);
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
