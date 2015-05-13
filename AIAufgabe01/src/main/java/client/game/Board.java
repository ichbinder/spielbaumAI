package client.game;

import lenz.htw.kimpl.Move;

public final class Board implements IBoard {

	private byte PlayerID = (byte) 255;
	private byte[][] Field = new byte[8][8];
	private byte[][] Index = new byte[8][8];
	private byte[][][] Position = new byte[4][6][2];
	
	public Board(byte PlayerID)
	{
		this.PlayerID = PlayerID;
		
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
		Field[x][y] = (byte)(PlayerID + 1);
		Index[x][y] = i;
		Position[PlayerID][i][0] = x;
		Position[PlayerID][i][1] = y;
	}
	
	public static final void RotateMove(Move Move, byte CounterClockwiseSteps)
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
		RotateMove(Move, (byte)(4 - PlayerID));
	}
	
	public final void RotateToGameSpace(Move Move)
	{
		RotateMove(Move, (byte)(PlayerID));
	}
	
	public final void ProcessMove(Move Move) 
	{
		RotateToPlayerSpace(Move);
		System.out.println(PlayerID + ": Recieved rotated Move " + Move);
		MovePiece(Move);
	}

	private final void MovePiece(Move Move)
	{		
		byte PlayerID = (byte)(Field[Move.fromX][Move.fromY] - 1);	
		byte PositionIndex = Index[Move.fromX][Move.fromY];	
		
		//Check if other is taken
		if(Field[Move.toX][Move.toY] != 0)
		{
			byte TakenPlayerID = (byte)(Field[Move.toX][Move.toY] - 1);
			byte TakenPositionIndex = Index[Move.toX][Move.toY];
			
			Position[TakenPlayerID][TakenPositionIndex][0] = 127;
			Position[TakenPlayerID][TakenPositionIndex][1] = 127;
		}		
	
		//Move
		Field[Move.fromX][Move.fromY] = 0;
		Index[Move.fromX][Move.fromY] = 0;
		Field[Move.toX][Move.toY] = (byte)(PlayerID + 1);
		Index[Move.toX][Move.toY] = PositionIndex;		
		Position[PlayerID][PositionIndex][0] = (byte)Move.toX;	
		Position[PlayerID][PositionIndex][1] = (byte)Move.toY;		
	}
	
	public byte[][] GetPosition(byte PlayerID)
	{
		return Position[PlayerID - 1];
	}
	
	public byte GetField(byte x, byte y) {
		return Field[x][y];
	}
}