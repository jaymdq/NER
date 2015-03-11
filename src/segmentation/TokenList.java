package segmentation;

import java.util.Vector;

public class TokenList extends AbsToken {

	private Vector<AbsToken> list;
	
	public TokenList(){
		this.list = new Vector<AbsToken>();
	}
	
	public void addToken(AbsToken newToken){
		list.add(newToken);
	}	
	
	public String toString(){
		return this.list.toString();
	}
	
	public AbsToken getTokenAt(int pos){
		return list.elementAt(pos);
	}
	
	public int size(){
		return list.size();
	}

	@Override
	public String getContenido() {
		String out= new String();
		for (AbsToken e: list)
			out+=e.getContenido()+ " ";
	return out;
	}

	@Override
	public Vector<AbsToken> split() {
		Vector<AbsToken> out = new Vector<AbsToken>();

		
		return null;
	}
	
	//TODO Mas funcionalidad
}
