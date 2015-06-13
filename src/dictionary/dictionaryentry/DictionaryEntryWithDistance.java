package dictionary.dictionaryentry;

public class DictionaryEntryWithDistance extends DictionaryEntry{
	
	// Variable
	
	private Integer distance;
	
	// Constructors
	
	/**
	 * 
	 * @param entry Entrada del diccionario
	 * @param distance Distancia de similitud entre el texto de la entrada con otro determinado.
	 */
	
	public DictionaryEntryWithDistance(DictionaryEntry entry, Integer distance) {
		super(entry.getText(), entry.getCategory());
		this.setDistance(distance);
	}
	
	// Getters And Setters
	
	/**
	 * 
	 * @return Distancia de similitud.
	 */

	public Integer getDistance() {
		return distance;
	}
	
	/**
	 * 
	 * @param distance Distancia de simulitud.
	 */

	public void setDistance(Integer distance) {
		this.distance = distance;
	}
	
}
