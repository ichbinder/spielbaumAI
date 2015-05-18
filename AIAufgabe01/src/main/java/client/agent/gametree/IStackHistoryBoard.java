package client.agent.gametree;

import client.game.IBoard;

public interface IStackHistoryBoard extends IBoard {
	
	void Setup(IBoard CurrentBoard);
	
	byte[][] GetPiecePositions();
	boolean MovePieceForward(byte X, byte Y);
	boolean MovePieceLeft(byte X, byte Y);
	boolean MovePieceRight(byte X, byte Y);
	void RotateBoard();
	void Pop();
	boolean IsPlayersView();
}
