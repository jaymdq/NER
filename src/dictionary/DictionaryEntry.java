package dictionary;

import java.io.Serializable;

public class DictionaryEntry implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1927419823397010392L;
	
	private String text;
	private String[] category;
	
	//TODO Se puede guardar un link para obtener información sobre las entidades nombradas !.
	
	public DictionaryEntry(String text, String[] category) {
		this.setText(text);
		this.setCategory(category);
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
	
	public String toString(){
		String resultCategory = "";
		for (int i = 0 ; i < category.length - 1; i++)
			resultCategory += category[i] + "|";
		resultCategory += category[category.length - 1];
		return  text + "  [" + resultCategory + "]"; 
	}

}
