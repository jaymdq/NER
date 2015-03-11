package dictionary;

public class DictionaryEntry {
	private String text;
	private String category;
	private Double score;
	
	public DictionaryEntry(String text, String category, Double score) {
		this.setText(text);
		this.setCategory(category);
		this.setScore(score);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}
	
	public String toString(){
		return "[" + text + "|" + category + "|" + score + "]"; 
	}

}
