package client.game;

import evaluator.IEvaluatorBoard;
import lenz.htw.kimpl.Move;

public final class Board implements IBoard {

	private byte PlayerID = (byte) 255;
	private byte[][] Field = new byte[8][8];
	private byte[][] Index = new byte[8][8];
	private byte[][][] Position = new byte[4][6][2];
	private boolean[] ActivePlayers = new boolean[4];
	private byte NextPlayer = (byte) 255;
	
	public Board(byte PlayerID)
	{
		this.PlayerID = PlayerID;
		this.NextPlayer = (byte)((4 - (int)PlayerID) % 4);
			
		for(byte i = 0; i < 4; i++)
		{
			ActivePlayers[i] = true;
		}
		
		for(int i = 0; i < 8; i++)
		{
			for(int j = 0; j < 8; j++)
			{
				Field[i][j] = -1;
				Index[i][j] = -1;
			}
		}
		
		//Initialize Board
		for(byte i = 1; i <= 6; i++)
		{
			SetField(i, (byte)0, (byte)(i - 1), (byte) 0);
			SetField((byte)7, i, (byte)(i - 1), (byte) 1);
			SetField(i, (byte)7, (byte)(i - 1), (byte) 2);
			SetField((byte)0, i, (byte)(i - 1), (byte) 3);
		}
	}
	
	private void SetField(byte x, byte y, byte i, byte PlayerID)
	{
		Field[x][y] = PlayerID;
		Index[x][y] = i;
		Position[PlayerID][i][0] = x;
		Position[PlayerID][i][1] = y;
	}
	
	public static final void RotateMove(Move Move, int CounterClockwiseSteps)
	{
		if((CounterClockwiseSteps == 0) || (CounterClockwiseSteps == 4))
		{
			return;
		}
		
		int tmpFromX = Move.fromX;
		int tmpFromY = Move.fromY;
		int tmpToX = Move.toX;
		int tmpToY = Move.toY;
		
		/*
		 * 		x,y
		 * 			7-y,x
		 * 			7-x,7-y
		 * 			y,7-x
		 */	
		
		switch(CounterClockwiseSteps) 
		{
			case 1: //Right
				Move.fromX = 7 - tmpFromY;
				Move.fromY = tmpFromX;
				Move.toX = 7 - tmpToY;
				Move.toY = tmpToX;
				break;
			case 2: //Opposite
				Move.fromX = 7 - tmpFromX;
				Move.fromY = 7 - tmpFromY;
				Move.toX = 7 - tmpToX;
				Move.toY = 7 - tmpToY;
				break;
			case 3: //Left
				Move.fromX = tmpFromY;
				Move.fromY = 7 - tmpFromX;
				Move.toX = tmpToY;
				Move.toY = 7 - tmpToX;
				break;
		}
	}
	
	public final void RotateToPlayerSpace(Move Move)
	{
		RotateMove(Move, (4 - PlayerID));
	}
	
	public final void RotateToGameSpace(Move Move)
	{
		RotateMove(Move, PlayerID);
	}
	
	public final void ProcessMove(Move Move) 
	{
		RotateToPlayerSpace(Move);
		//System.out.println(PlayerID + ": Recieved rotated Move " + Move);
		MovePiece(Move);
	}

	private final void MovePiece(Move Move)
	{		
		byte PlayerID = Field[Move.fromX][Move.fromY];	
		byte PositionIndex = Index[Move.fromX][Move.fromY];	
		
		//Check of Player was beaten
		CheckIfPlayerIsBeaten(PlayerID);
		
		//Check if other is taken
		if(Field[Move.toX][Move.toY] != -1)
		{
			byte TakenPlayerID = Field[Move.toX][Move.toY];
			byte TakenPositionIndex = Index[Move.toX][Move.toY];
			
			Position[TakenPlayerID][TakenPositionIndex][0] = -1;
			Position[TakenPlayerID][TakenPositionIndex][1] = -1;
		}		
	
		//Move
		Field[Move.fromX][Move.fromY] = -1;
		Index[Move.fromX][Move.fromY] = -1;
		Field[Move.toX][Move.toY] = PlayerID;
		Index[Move.toX][Move.toY] = PositionIndex;		
		Position[PlayerID][PositionIndex][0] = (byte)Move.toX;	
		Position[PlayerID][PositionIndex][1] = (byte)Move.toY;		
	}
	
	private final void CheckIfPlayerIsBeaten(byte MovingPlayer)
	{
		//It should be others turn, so he is beaten
		if(MovingPlayer != NextPlayer)
		{
			ActivePlayers[NextPlayer] = false;
			NextPlayer = MovingPlayer;
		}
		
		//Get next ActivePlayer
		for(byte i = 1; i < 4; i++)
		{
			byte NewNextPlayer = (byte) ((NextPlayer + i) % 4);
			if(ActivePlayers[NewNextPlayer])
			{
				NextPlayer = NewNextPlayer;
				break;
			}
		}
	}
	
	public byte GetField(byte x, byte y) {
		return Field[x][y];
	}

	public void GetActivePlayers(boolean[] ActivePlayer) {
		for(int i = 0; i == this.ActivePlayers.length; i++)
		{
			ActivePlayer[i] = this.ActivePlayers[i];
		}		
	}

	public void GetPositions(int PlayerID, byte[][] Positions) {
		for(int i = 0; i < 6; i++)
		{
				Positions[i][0] = Position[PlayerID][i][0];
				Positions[i][1]	= Position[PlayerID][i][1];
		}
	}

	public final byte[][] GetField() {
		return Field;
	}

	public final byte[][] GetIndices() {
		return Index;
	}

	public final byte[][][] GetPositions() {
		return Position;
	}

	public final boolean[] GetActivePlayers() {
		return ActivePlayers;
	}
}
