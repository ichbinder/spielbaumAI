package client.game;

public class Evaluator01
{
	private IBoard gameBoard;
	private byte playerID = (byte) 255;
	private int valuation = 0;
	
	public Evaluator01(IBoard gameBoard, byte playerID)
	{
		this.gameBoard = gameBoard;
		this.playerID = playerID;
	}
	
	public byte calc()
	{
		
		byte[][] Positions = this.gameBoard.GetPosition(this.playerID);
		for (int i = 0; i < Positions.length; i++)
		{
			byte X = Positions[i][0];
			byte Y = Positions[i][1];
			if(gameBoard.GetField(X, Y) == 0);
				this.valuation += 2;
			
		}
		return (byte)this.valuation;
	}
	
	public byte getValuation()
	{
		return (byte)this.valuation;
	}
}
