package evaluator;

public class DummyEvaluator implements IEvaluator {
	
	private boolean[] ActivePlayerBuffer = new boolean[4];
	private byte[][] PositionsBuffer = new byte[6][2];

	public final int Evaluate(IEvaluatorBoard Board) {
		int Score = 0;
		
		Board.GetActivePlayers(ActivePlayerBuffer);
		boolean Win = true;
		
		for(int i = 1; i < 4; i++)
		{
			if(!ActivePlayerBuffer[i])
			{
				Score += 15;
				Win = false;
			}
		}
		
		if(Win)
		{
			return Integer.MAX_VALUE;
		}
		
		Board.GetPositions(0, PositionsBuffer);
		for(int i = 0; i < 6; i++)
		{
			if(PositionsBuffer[i][0] != -1)
			{
				Score += 50;
			}
			else
			{
				Score -= 100;
			}
		}
		
		for(int i = 1; i < 4; i++)
		{
			if(ActivePlayerBuffer[i])
			{
				Board.GetPositions(i, PositionsBuffer);
				if(PositionsBuffer[i][0] == -1)
				{
					Score += 10;
				}				
			}
		}
		
		return Score;
	}

}
