package evaluator;

public class DummyEvaluator implements IEvaluator {
	
	private boolean[] ActivePlayerBuffer = new boolean[4];
	private byte[][] PositionsBuffer = new byte[6][2];
	
	private final static int ScorePerDefeatedPlayer = 15;
	private final static int ScorePerRiskPiece = 10000;
	private final static int ScorePerPotentialTake = 200;
	private final static int ScorePerLostPiece = 5000;
	private final static int ScorePerOtherLostPiece = 2000;
	private final static int ScorePerOtherRiskPiece = 200;
	private final static int ScorePerOtherPotentialTake = 200;
	
	

	public final int Evaluate(IEvaluatorBoard Board) {
		int Score = 0;
		
		Board.GetActivePlayers(ActivePlayerBuffer);
		boolean Win = true;
		
		for(int i = 1; i < 4; i++)
		{
			if(!ActivePlayerBuffer[i])
			{
				Score += ScorePerDefeatedPlayer;
			}
			else
			{
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
				byte X = PositionsBuffer[i][0];
				byte Y = PositionsBuffer[i][1];
				if(Board.CanBeTaken(X, Y))
				{
					Score -= ScorePerRiskPiece;
				}
				
				if(Board.CanTakeOther(X, Y))
				{
					Score += ScorePerPotentialTake;
				}
			}
			else
			{
				Score -= ScorePerLostPiece;
			}
		}
		
		for(int i = 1; i < 4; i++)
		{
			if(ActivePlayerBuffer[i])
			{
				Board.GetPositions(i, PositionsBuffer);
				if(PositionsBuffer[i][0] == -1)
				{
					Score += ScorePerOtherLostPiece;
				}
				else
				{
					byte X = PositionsBuffer[i][0];
					byte Y = PositionsBuffer[i][1];
					if(Board.CanBeTaken(X, Y))
					{
						Score += ScorePerOtherRiskPiece;
					}
					
					if(Board.CanTakeOther(X, Y))
					{
						Score += ScorePerOtherPotentialTake;
					}
				}
				
				
			}
		}
		
		return Score;
	}
}
