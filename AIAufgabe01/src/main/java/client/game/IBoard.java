package client.game;

import evaluator.IEvaluatorBoard;
import lenz.htw.kimpl.Move;

public interface IBoard extends IEvaluatorBoard {
	void ProcessMove(Move Move);

	void RotateToPlayerSpace(Move Move);
	void RotateToGameSpace(Move Move);
}
