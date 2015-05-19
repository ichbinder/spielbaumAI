package client.agent;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import lenz.htw.kimpl.Move;
import client.game.IBoard;

public class RandomAgent implements IAgent {
	
	private Random Rng = new Random();
	
	public Move CalculateMove(IBoard GameBoard) {
		
		byte[][] Positions = new byte[6][2];
		GameBoard.GetPositions(0, Positions);
		
		List<Integer> Indices = new LinkedList<Integer>();
		for(int i = 0; i < 6; i++)
		{
			Indices.add(i);
		}
		
		
		while(true)
		{
			if(Indices.isEmpty())
			{
				return null;
			}
		
			int ListIndex = -1;
			int PieceIndex = -1;

			ListIndex = Rng.nextInt(Indices.size());
			PieceIndex = Indices.get(ListIndex);
			Indices.remove(ListIndex);
			System.out.println("Looking for Index - " + PieceIndex);
			
			if(Positions[PieceIndex][0] == -1)
			{
				System.out.println("No Piece!");
				continue;
			}
			
			byte X = Positions[PieceIndex][0];
			byte Y = Positions[PieceIndex][1];

			System.out.println("Piece " + X + " - " + Y);
			
			byte NewY = (byte)(Y + 1);
			
			if(NewY > 7)
			{
				System.out.println("Piece at end of Board");
				continue;
			}
			
			if((X != 0) && (GameBoard.GetField((byte)(X - 1), NewY) > 0))
			{
				return new Move(X,Y, X - 1, NewY);
			}

			if((X != 7) && (GameBoard.GetField((byte)(X + 1), NewY) > 0))
			{
				return new Move(X,Y, X + 1, NewY);
			}
			
			if(GameBoard.GetField(X, NewY) == -1)
			{
				return new Move(X,Y, X, NewY);
			}

			System.out.println("No Move found!");
		}
	}
	
}
