package score;

public class Score {

	private static Score instance = null;
	private double exactScore = 1.0;	
	
	private Score(){

	}

	public static Score getInstance(){
		if (instance == null){
			instance = new Score();
		}
		return instance;
	}
	
	public double getExactScore(){
		return this.exactScore;
	}

	public double getAproximatedScore(int distance, int entryTextLength){
		return 1 - ((double) distance / entryTextLength);
	}
	
}
