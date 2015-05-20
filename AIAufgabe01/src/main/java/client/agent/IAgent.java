package client.agent;

import client.game.IBoard;
import lenz.htw.kimpl.Move;

public interface IAgent {
	Move CalculateMove(IBoard GameBoard);
	
	void PrintStats();
}
