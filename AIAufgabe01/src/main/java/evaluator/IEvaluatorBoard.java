package evaluator;

public interface IEvaluatorBoard {
	void GetActivePlayers(boolean[] ActivePlayer);
	void GetPositions(int PlayerID, byte[][] Positions);
	byte GetField(byte x, byte y);
	
	boolean CanTakeOther(byte X, byte Y);
	boolean CanBeTaken(byte X, byte Y);
}
