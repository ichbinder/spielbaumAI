package client.agent.gametree;

import java.util.ArrayList;

import lenz.htw.kimpl.Move;
import client.game.Board;
import client.game.IBoard;

public final class StackHistoryBoard implements IStackHistoryBoard {
	
	private static final int InitialStackSize = 50;
	
	private enum Command { Move, Rotate, Forfeit };

	class StackEntry {
		public Command StackCommand;
		public byte FromX;
		public byte FromY;
		public byte ToX;
		public byte ToY;
		public byte TakenPiece;
		public boolean DefeatedPlayer;	
	}
	
	private ArrayList<StackEntry> Stack = null;
	private int StackPointer = 0;
	
	private int CurrentDirection = 0;
	private Move MoveBuffer = null;	

	private byte[][] Field = new byte[8][8];
	private byte[][] Index = new byte[8][8];
	private byte[][][] Position = new byte[4][6][2];
	private boolean[] ActivePlayers = new boolean[4];
	
	public StackHistoryBoard()	
	{
		MoveBuffer = new Move(0,0,0,0);
		Stack = new ArrayList<StackEntry>(InitialStackSize);
		for(int i = 0; i < InitialStackSize; i++)
		{
			Stack.add(new StackEntry());
		}
	}
	
	public final int GetStackSize()
	{
		return Stack.size();
	}
	
	private final void CopyMove(Move Move)
	{
		MoveBuffer.fromX = Move.fromX;
		MoveBuffer.fromY = Move.fromY;
		MoveBuffer.toX = Move.toX;
		MoveBuffer.toY = Move.toY;
	}
	
	public final void Setup(IBoard CurrentBoard) {
		StackPointer = 0;
		CurrentDirection = 0;
	}

	public final boolean IsPlayersView() {
		return (CurrentDirection == 0);
	}
	
	private final StackEntry PushStack()
	{
		if(StackPointer == Stack.size())
		{
			Stack.add(new StackEntry());
		}
		
		return Stack.get(StackPointer++);
	}
	
	private final StackEntry PopStack()
	{
		return Stack.get(--StackPointer);	
	}
	
	public final boolean MovePieceForward(byte X, byte Y) {
		if((Y + 1) > 7)
		{
			return false;
		}

		MoveBuffer.fromX = X;
		MoveBuffer.fromY = Y;
		MoveBuffer.toX = X;
		MoveBuffer.toY = Y + 1;
		
		Board.RotateMove(MoveBuffer, CurrentDirection);
		
		if(Field[MoveBuffer.toX][MoveBuffer.toY] != -1)
		{
			return false;
		}

		ProcessMove(MoveBuffer);
		
		return true;
	}

	public final boolean MovePieceLeft(byte X, byte Y) {	
		if((Y + 1) > 7)
		{
			return false;
		}
		
		if((X - 1) < 0)
		{
			return false;
		}
	
		MoveBuffer.fromX = X;
		MoveBuffer.fromY = Y;
		MoveBuffer.toX = X - 1;
		MoveBuffer.toY = Y + 1;
		
		Board.RotateMove(MoveBuffer, CurrentDirection);
		
		byte FieldValue = Field[MoveBuffer.toX][MoveBuffer.toY];
		if((FieldValue == -1) || (FieldValue == CurrentDirection))
		{
			return false;
		}
	
		ProcessMove(MoveBuffer);
		
		return true;
	}

	public final boolean MovePieceRight(byte X, byte Y) {
		if((Y + 1) > 7)
		{
			return false;
		}
		
		if((X + 1) > 7)
		{
			return false;
		}
	
		MoveBuffer.fromX = X;
		MoveBuffer.fromY = Y;
		MoveBuffer.toX = X + 1;
		MoveBuffer.toY = Y + 1;
		
		Board.RotateMove(MoveBuffer, CurrentDirection);
		
		byte FieldValue = Field[MoveBuffer.toX][MoveBuffer.toY];
		if((FieldValue == -1) || (FieldValue == CurrentDirection))
		{
			return false;
		}
	
		ProcessMove(MoveBuffer);
		
		return true;
	}

	public final void DoMove(Move Move) {
		CopyMove(Move);
		//Rotate Move
		Board.RotateMove(MoveBuffer, CurrentDirection);
		//Process Move
		ProcessMove(MoveBuffer);
	}
	
	private final void ProcessMove(Move Move)
	{
		StackEntry Entry = PushStack();
		Entry.StackCommand = Command.Move;
		Entry.FromX = (byte) Move.fromX;
		Entry.FromY = (byte) Move.fromY;
		Entry.ToX = (byte) Move.toX;
		Entry.ToY = (byte) Move.toY;
		
		byte ToField = Field[Entry.ToX][Entry.ToY];

		// Check if Piece and/or Player was defeated
		if(ToField != -1)
		{
			Entry.TakenPiece = ToField;
			byte ToIndex = Index[Entry.ToX][Entry.ToY];
			Index[Entry.ToX][Entry.ToY] = -1;
			Position[ToField][ToIndex][0] = -1;
			Position[ToField][ToIndex][1] = -1;
			
			if(ActivePlayers[ToField])
			{
				boolean PieceLeft = false;
				for(int i = 0; i < 6; i++)
				{
					if(Position[ToField][i][0] != -1)
					{
						PieceLeft = true;
						break;
					}
				}
				
				if(!PieceLeft)
				{
					ActivePlayers[ToField] = false;
					Entry.DefeatedPlayer = true;
				}
			}
		}
		else
		{
			Entry.TakenPiece = -1;
		}
		
		// Make Move
		byte FromField = Field[Entry.FromX][Entry.FromY];
		Field[Entry.FromX][Entry.FromY] = -1;
		byte FromIndex = Index[Entry.FromX][Entry.FromY];
		
		Field[Entry.ToX][Entry.ToY] = FromField;
		Index[Entry.ToX][Entry.ToY] = FromIndex;
		Position[FromField][FromIndex][0] = Entry.ToX;
		Position[FromField][FromIndex][1] = Entry.ToY;	
	}
	
	private final void UndoMove(StackEntry Entry)
	{
		//Move Piece Back
		byte ToField = Field[Entry.ToX][Entry.ToY];
		byte ToIndex = Index[Entry.ToX][Entry.ToY];
		
		Field[Entry.FromX][Entry.FromY] = ToField;
		Index[Entry.FromX][Entry.FromY] = ToIndex;
		Position[ToField][ToIndex][0] = Entry.FromX;
		Position[ToField][ToIndex][1] = Entry.FromY;
		
		if(Entry.TakenPiece == -1)
		{
			Field[Entry.ToX][Entry.ToY] = -1;
			Index[Entry.ToX][Entry.ToY] = -1;
		}
		else
		{
			Field[Entry.ToX][Entry.ToY] = Entry.TakenPiece;
			byte PositionIndex = 0;
			for(; PositionIndex < 6; PositionIndex++)
			{
				if(Position[Entry.TakenPiece][PositionIndex][0] == -1)
				{
					break;
				}
			}
			
			Index[Entry.ToX][Entry.ToY] = PositionIndex;
			Position[Entry.TakenPiece][PositionIndex][0] = Entry.ToX;
			Position[Entry.TakenPiece][PositionIndex][1] = Entry.ToY;
			
			if(Entry.DefeatedPlayer)
			{
				ActivePlayers[Entry.TakenPiece] = true;
			}		
		}		
	}
	
	public final void Forfeit() {	
		StackEntry Entry = PushStack();
		Entry.StackCommand = Command.Forfeit;
		Entry.TakenPiece = (byte) CurrentDirection;
		
		// Update Active Players
		ActivePlayers[Entry.TakenPiece] = false;
	}
	
	public final void RotateBoard() {
		StackEntry Entry = PushStack();
		Entry.StackCommand = Command.Rotate;
		
		CurrentDirection = (CurrentDirection + 1) % 4;
	}

	public final void Pop() {
		
		StackEntry Entry = PopStack();
		
		switch(Entry.StackCommand)
		{
		case Forfeit:
			ActivePlayers[Entry.TakenPiece] = true;
			break;
		case Move:
			UndoMove(Entry);
			break;
		case Rotate:
			CurrentDirection = (CurrentDirection + 3) % 4;
			break;
		default:
			break;
			
		}
	}

	private final byte RotateX(byte X, byte Y, int CounterClockwiseSteps)
	{	
		/*
		 * 		x,y
		 * 			7-y,x
		 * 			7-x,7-y
		 * 			y,7-x
		 */	
	
		switch(CounterClockwiseSteps) 
		{
			case 1: //Right
				return (byte) (7 - Y);
			case 2: //Opposite
				return (byte) (7 - X);
			case 3: //Left
				return Y;
			default: 
				return X;
		}
	}
	
	private final byte RotateY(byte X, byte Y, int CounterClockwiseSteps)
	{	
		/*
		 * 		x,y
		 * 			7-y,x
		 * 			7-x,7-y
		 * 			y,7-x
		 */	
		
		switch(CounterClockwiseSteps) 
		{
			case 1: //Right
				return X;
			case 2: //Opposite
				return (byte) (7 - Y);
			case 3: //Left
				return (byte) (7 - X);
			default: 
				return X;
		}
	}
	
	public final byte GetField(byte X, byte Y) {
		return Field[RotateX(X,Y,CurrentDirection)][RotateY(X,Y,CurrentDirection)];
	}

	public final void GetActivePlayers(boolean[] ActivePlayer) {
		for(int i = 0; i < 4; i++)
		{
			ActivePlayer[i] = this.ActivePlayers[(4 + i - CurrentDirection) % 4];
		}
	}

	public final void GetPiecePositions(byte[][] Positions) {
		GetPositions(CurrentDirection, Positions);
	}
	
	public final void GetPositions(int PlayerID, byte[][] Positions) {
		for(int i = 0; i < 6; i++)
		{
			if(Position[PlayerID][i][0] == -1)
			{
				Positions[i][0] = -1;
				Positions[i][1]	= -1;	
			}
			else
			{
				Positions[i][0] = RotateX(Position[PlayerID][i][0],Position[PlayerID][i][1], CurrentDirection);
				Positions[i][1]	= RotateY(Position[PlayerID][i][0],Position[PlayerID][i][1], CurrentDirection);
			}
		}		
	}

	public final boolean IsCurrentPlayerActive() {
		return ActivePlayers[CurrentDirection];
	}

}
