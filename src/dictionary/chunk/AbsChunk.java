package dictionary.chunk;

public abstract class AbsChunk {

	// Variables
	protected String text;
	protected String categoryType;
	protected Double score;
	
	// Setters And Getters

	/**
	 * 
	 * @return Categoria en la cual se clasificio el Chunk
	 */
	
	public String getCategoryType(){
		return categoryType;
	}

	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public Double getScore() {
		return score;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	/**
	 * 
	 * @return Texto del Chunk
	 */
	public String getText(){
		return this.text;
	}
	
	// Methods
	@Override
	public String toString() {	
		return "TEXT: \"" + text + "\"  CATEGORY: " + categoryType ;
	}
	
}
