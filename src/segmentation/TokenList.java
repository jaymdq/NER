package segmentation;

import java.util.Vector;

public class TokenList {

	private Vector<Token> list;
	
	public TokenList(){
		this.list = new Vector<Token>();
	}
	
	public void addToken(Token newToken){
		list.add(newToken);
	}
	
	public TokenList subSequence(){
	
		return null;
	}
	
	
	public String toString(){
		return this.list.toString();
	}
	
	//TODO Mas funcionalidad
}
