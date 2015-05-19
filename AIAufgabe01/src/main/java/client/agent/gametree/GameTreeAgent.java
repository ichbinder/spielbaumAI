package client.agent.gametree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import lenz.htw.kimpl.Move;
import client.agent.IAgent;
import client.game.IBoard;
import evaluator.IEvaluator;

public final class GameTreeAgent implements IAgent {

	private static final int InitialNodePoolSize = 5000;
	
	private Random Rng = new Random();
	private ArrayList<TreeNode> NodePool = null;
	private int NodePoolIndex = 0;
	private List<TreeNode> BestLeafs = new ArrayList<TreeNode>(18);
	
	private byte[][] PiecePositions = new byte[6][2];
	
	private IStackHistoryBoard Board = new StackHistoryBoard();
	private IEvaluator Evaluator = null;

	public GameTreeAgent(IEvaluator Evaluator)
	{
		this.Evaluator = Evaluator;
		NodePool = new ArrayList<TreeNode>(InitialNodePoolSize);
		for(int i = 0; i < InitialNodePoolSize; i++)
		{
			NodePool.add(new TreeNode());
		}
	}

	public final void PrintStats() {
		System.out.println("Node Pool Size: " + NodePool.size());
		System.out.println("Board Stack Size: " + Board.GetStackPoolSize());
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
		
		//When current player is out, just rotate and skip to next one
		if(!Board.IsCurrentPlayerActive())
		{
			int Value = 0;
			Board.RotateBoard();
			Value = ComputeTreeLevel(MaxPlayerRounds, Parent, Alpha, Beta);
			Board.Pop();
			return Value;
		}
		
		boolean IsPlayerMove = Board.IsPlayersView();
		int NodePoolIndexBuffer = NodePoolIndex;
		LinkedList<TreeNode> SortedList = new LinkedList<TreeNode>();
		Board.GetPiecePositions(PiecePositions);
		
		for(int i = 0; i < 6; i++)
		{
			byte[] Position = PiecePositions[i];
			if(Position[0] == -1)
			{
				continue;
			}
			
			if(Board.MovePieceForward(Position[0], Position[1]))
			{
				SetupNode(SortedList, Parent, Position[0], Position[1], Position[0], Position[1] + 1);
				Board.Pop();
			}
			
			if(Board.MovePieceLeft(Position[0], Position[1]))
			{
				SetupNode(SortedList, Parent, Position[0], Position[1], Position[0] - 1, Position[1] + 1);
				Board.Pop();
			}
			
			if(Board.MovePieceRight(Position[0], Position[1]))
			{
				SetupNode(SortedList, Parent, Position[0], Position[1], Position[0] + 1, Position[1] + 1);
				Board.Pop();
			}
		}
		
		if(IsPlayerMove && (Parent != null))
		{
			MaxPlayerRounds--;
		}
		
		// Make Rekursive Call through List
		ListIterator<TreeNode> It = SortedList.listIterator();
		int ValueBuffer = IsPlayerMove ? Alpha : Beta;
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
				Board.RotateBoard();
				ValueBuffer = ComputeTreeLevel(MaxPlayerRounds, Parent, Alpha, Beta);
				Board.Pop();
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
				Board.RotateBoard();
				int Value = ComputeTreeLevel(MaxPlayerRounds, Node, AlphaTmp, BetaTmp);
				Board.Pop();
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
		
		//Release Nodes to pool
		NodePoolIndex = NodePoolIndexBuffer;
		
		return ValueBuffer;
	}
	
	private final void SetupNode(LinkedList<TreeNode> SortedList, TreeNode Parent, int fromX, int fromY, int ToX, int ToY)
	{
		if(NodePoolIndex >= NodePool.size())
		{
			NodePool.add(new TreeNode());
		}

		TreeNode NewNode = NodePool.get(NodePoolIndex++);
		
		int Value =  Evaluator.Evaluate(Board);
		NewNode.Setup(Parent, Value, fromX, fromY, ToX, ToY);
		
		ListIterator<TreeNode> It = SortedList.listIterator();
		boolean Inserted = false;
		
		while(It.hasNext())
		{
			TreeNode Element = It.next();
			if(Element.Value <= Value)
			{
				It.previous();
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
