package evaluator;

public interface IEvaluatorBoard {
	public byte[][] GetPosition(byte PlayerID);
	byte GetField(byte x, byte y);
	boolean[] GetActivePlayers();
}
