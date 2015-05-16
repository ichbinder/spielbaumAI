package client.game;

public class Evaluator01
{
	private IBoard gameBoard;
	private byte playerID = (byte) 255;
	private byte valuation = (byte) 0;
	
	public Evaluator01(IBoard gameBoard, byte playerID)
	{
		this.gameBoard = gameBoard;
		this.playerID = playerID;
	}

	
	
	public byte getValuation()
	{
		return valuation;
	}
}
