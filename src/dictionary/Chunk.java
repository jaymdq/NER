package dictionary;

public class Chunk {

	private String text;
	private int start;
	private int end;
	private String categoryType;
	private double score;
	
	/**
	 * 
	 * @param start start hace referencia a la ubicacion del primer caracter del Chunk
	 * @param end endhace referencia a la ubicacion del ultimo caracter del Chunk
	 * @param categoryType Categoria en la que se ubica el text del Chunk
	 * @param text Texto que forma el chunk
	 */
	
	public Chunk(int start, int end, String categoryType, String text, double score){
		this.start = start;
		this.end = end;
		this.categoryType = categoryType;
		this.text = text.substring(start, end);
		this.score = score;
	}
	
	/**
	 * 
	 * @return Texto del Chunk
	 */
	public String getText(){
		return this.text;
	}
	
	/**
	 * 
	 * @return Posicion del caracter de inicio del Chunk en el texto
	 */

	public int start(){
		return start;
	}
	
	/**
	 * 
	 * @return Posicion del ultimo caracter del Chunk en el texto
	 */
	
	public int end(){
		return end;
	}
	
	/**
	 * 
	 * @return Categoria en la cual se clasificio el Chunk
	 */
	
	public String type(){
		return categoryType;
	}
	
	public String toString(){
		return "\n" + "TEXT: \"" + text + "\" START: " + start + " END: " + end + " CATEGORY: " + categoryType + " SCORE: " + score;
	}

	public double getScore() {
		return score;
	}
	
}
