package client.agent;

import java.util.Random;

import lenz.htw.kimpl.Move;
import client.game.IBoard;

public class RandomAgent implements IAgent {
	
	private Random Rng = new Random();
	
	public Move CalculateMove(IBoard GameBoard) {
		
		byte[][] Positions = new byte[6][2];
		GameBoard.GetPositions(0, Positions);
		
		while(true)
		{
			System.out.println("Starting Random Endless Loop");
		
			byte Index = -1;

			do {
				Index = (byte)Rng.nextInt(6);
				System.out.println("Looking for Index - " + Index);
			} while(Positions[Index][0] == -1);
			
			byte X = Positions[Index][0];
			byte Y = Positions[Index][1];

			System.out.println("Piece " + X + " - " + Y);
			
			byte NewY = (byte)(Y + 1);
			
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
