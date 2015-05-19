package client.agent.gametree;

import java.util.ArrayList;

import lenz.htw.kimpl.Move;
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
	
	public StackHistoryBoard()
	{
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
	
	public boolean MovePieceForward(byte X, byte Y) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean MovePieceLeft(byte X, byte Y) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean MovePieceRight(byte X, byte Y) {
		// TODO Auto-generated method stub
		return false;
	}

	public void DoMove(Move Move) {
		//Rotate Move
		
		//Process Move
	}
	
	public void Forfeit() {	
		StackEntry Entry = PushStack();
		Entry.StackCommand = Command.Forfeit;
		
		// TODO Update Active Players
	}
	
	public void RotateBoard() {
		StackEntry Entry = PushStack();
		Entry.StackCommand = Command.Rotate;
		
		CurrentDirection = (CurrentDirection + 1) % 4;
	}

	public void Pop() {
		
		StackEntry Entry = PopStack();
		
		switch(Entry.StackCommand)
		{
		case Forfeit:
			// TODO Update Active Players
			break;
		case Move:
			break;
		case Rotate:
			CurrentDirection = (CurrentDirection + 3) % 4;
			break;
		default:
			break;
			
		}
	}

	public byte[][] GetPosition(byte PlayerID) {
		// TODO Auto-generated method stub
		return null;
	}

	public byte GetField(byte x, byte y) {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean[] GetActivePlayers() {
		// TODO Auto-generated method stub
		return null;
	}

	public byte[][] GetPiecePositions() {
		// TODO Auto-generated method stub
		return null;
	}

}
