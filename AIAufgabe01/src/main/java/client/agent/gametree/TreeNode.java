package client.agent.gametree;

import lenz.htw.kimpl.Move;

public class TreeNode {
	public TreeNode Parent = null;
	public int Value = 0;
	public Move Move = new Move(0,0,0,0);
	
	void Setup(TreeNode Parent, int Value, int fromX, int fromY, int ToX, int ToY)
	{
		this.Parent = Parent;
		this.Value = Value;
		this.Move.fromX = fromX;
		this.Move.fromY = fromY;
		this.Move.toX = ToX;
		this.Move.toY = ToY;
	}
}
