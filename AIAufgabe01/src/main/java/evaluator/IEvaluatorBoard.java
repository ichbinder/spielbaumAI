package evaluator;

public interface IEvaluatorBoard {
	void GetActivePlayers(boolean[] ActivePlayer);
	void GetPositions(int PlayerID, byte[][] Positions);
	byte GetField(byte x, byte y);
}
