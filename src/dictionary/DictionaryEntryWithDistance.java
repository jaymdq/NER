package dictionary;

public class DictionaryEntryWithDistance extends DictionaryEntry{
	
	private Integer distance;
	
	/**
	 * 
	 * @param entry Entrada del diccionario
	 * @param distance Distancia de similitud entre el texto de la entrada con otro determinado.
	 */
	
	public DictionaryEntryWithDistance(DictionaryEntry entry, Integer distance) {
		super(entry);
		this.setDistance(distance);
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
	
}
