package segmentation;

import java.util.Vector;

public class Token extends AbsToken {

	private String word;
	private String type = "No identificado"; //TODO check this + separar los tipos en una clase
	
	public Token(String words){
		this.word = words;
	}
	
	@Override	
	public String getContenido(){
		return this.word;
	}
	
	public String toString(){
		return this.word;
	}

	@Override
	public Vector<AbsToken> split() {
		Vector<AbsToken> out = new Vector<AbsToken>();
		out.add(this);
		return out;
	}



	
}
