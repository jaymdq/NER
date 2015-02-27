package segmentation;

public class Token {

	private String words;
	private String type = "No identificado"; //TODO check this + separar los tipos en una clase
	
	public Token(String words){
		this.words = words;
	}
	
	public String toString(){
		return this.words;
	}
	
}
