package dictionary.chunk;

public class ChunkEvent extends AbsChunk implements Comparable<ChunkEvent>{ 
	
	// Variables
	
	// Constructors
	
	public ChunkEvent( String text, String categoryType, Double probBursty) {
		this.setText(text);
		this.setCategoryType(categoryType);
		this.setScore(probBursty);
	}
	
	// Getters And Setters
	
	// Methods
	
	@Override
	public boolean equals(Object obj) {
		ChunkEvent chunkEvent = (ChunkEvent) obj;
		return this.getText().equals(chunkEvent.getText()) && this.getCategoryType().equals(chunkEvent.getCategoryType());
	}

	@Override
	public int compareTo(ChunkEvent chunkEvent) {
		return this.getScore().compareTo(chunkEvent.getScore());
	}	
	
	@Override
	public String toString() {
		return "TEXT: \"" + text + "\" CATEGORY: " + categoryType + " ProbBursty: " + score;
	}
	
}
