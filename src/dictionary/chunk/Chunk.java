package dictionary.chunk;

public class Chunk extends AbsChunk{

	// Variables
	private int start;
	private int end;
	
	// Constructors
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
	
	// Methods
	
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
	
	@Override
	public String toString(){
		return "TEXT: \"" + text + "\" START: " + start + " END: " + end + " CATEGORY: " + categoryType + " SCORE: " + score;
	}
	
}
