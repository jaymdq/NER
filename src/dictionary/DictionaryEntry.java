package dictionary;

import java.io.Serializable;

public class DictionaryEntry implements Serializable {

	private static final long serialVersionUID = 1927419823397010392L;
	
	private String text;
	private String[] category;
	
	/**
	 * 
	 * @param text Texto correspondiente con la entrada de un diccionario
	 * @param category Lista de categorias a la que hace referencia text
	 */
	
	public DictionaryEntry(String text, String[] category) {
		this.setText(text);
		this.setCategory(category);
	}
	
	/**
	 * 
	 * @return Texto de la entrada del diccionario
	 */

	public String getText() {
		return text;
	}
	
	/**
	 * 
	 * @param text El parametro text es el texto de la entrada.
	 */

	public void setText(String text) {
		this.text = text;
	}
	
	/**
	 * 
	 * @return Lista de categorias que posee la entrada.
	 */

	public String[] getCategory() {
		return category;
	}
	
	/**
	 * 
	 * @param category El parametro category es la lista de categorias correspondientes con la entrada.
	 */

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
	
	public boolean equals(Object obj){
		if (obj instanceof DictionaryEntry){
			DictionaryEntry other = (DictionaryEntry) obj;
			return this.getText().equals(other.getText()) && this.getCategory().equals(other.getCategory());
		}
		return false;
		
	}
}
