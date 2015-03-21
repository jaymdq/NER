package dictionary;

import java.io.Serializable;

public class DictionaryEntry implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1927419823397010392L;
	
	private String text;
	private String[] category;
	private Double score;	//TODO por ahora no lo estamos usando
	
	public DictionaryEntry(String text, String[] category, Double score) {
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

	public String[] getCategory() {
		return category;
	}

	public void setCategory(String[] category) {
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
