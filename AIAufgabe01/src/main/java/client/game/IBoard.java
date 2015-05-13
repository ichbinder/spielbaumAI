package client.game;

import lenz.htw.kimpl.Move;

public interface IBoard {
	void ProcessMove(Move Move);

	void RotateToPlayerSpace(Move Move);
	void RotateToGameSpace(Move Move);
	
	public byte[][] GetPosition(byte PlayerID);
	byte GetField(byte x, byte y);
}
