package dictionary;

public class DictionaryEntryWithDistance {

	private Integer distance;
	private DictionaryEntry entry;
	
	public DictionaryEntryWithDistance(DictionaryEntry entry, Integer distance) {
		this.setDistance(distance);
		this.setEntry(entry);
	}

	public Integer getDistance() {
		return distance;
	}

	public void setDistance(Integer distance) {
		this.distance = distance;
	}

	public DictionaryEntry getEntry() {
		return entry;
	}

	public void setEntry(DictionaryEntry entry) {
		this.entry = entry;
	}
	
}
