package dictionary;

public class Chunk {

	private String text;
	private int start;
	private int end;
	private String categoryType;
	
	public Chunk(int start, int end, String categoryType, String text){
		this.start = start;
		this.end = end;
		this.categoryType = categoryType;
		this.text = text.substring(start, end);
	}

	public int start(){
		return start;
	}
	
	public int end(){
		return end;
	}
	
	public String type(){
		return categoryType;
	}
	
	public String toString(){
		return "TEXT: \"" + text + "\" START: " + start + " END: " + end + " CATEGORY: " + categoryType;
	}
	
}
