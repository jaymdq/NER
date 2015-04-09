package dictionary;

public class DictionaryEntryWithDistance {
	
	private Integer distance;
	private DictionaryEntry entry;
	
	/**
	 * 
	 * @param entry Entrada del diccionario
	 * @param distance Distancia de similitud entre el texto de la entrada con otro determinado.
	 */
	
	public DictionaryEntryWithDistance(DictionaryEntry entry, Integer distance) {
		this.setDistance(distance);
		this.setEntry(entry);
	}
	
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
	
	/**
	 * 
	 * @return Entrada de diccionario
	 */

	public DictionaryEntry getEntry() {
		return entry;
	}
	
	/**
	 * 
	 * @param entry Entrada del diccionario 
	 */

	public void setEntry(DictionaryEntry entry) {
		this.entry = entry;
	}
	
}
