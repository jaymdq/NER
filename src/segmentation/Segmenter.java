package segmentation;

import java.util.StringTokenizer;

public class Segmenter {


	public Segmenter(){

	}

	public TokenList getListOfTokens(String text){
		TokenList out = new TokenList();
		StringTokenizer st = new StringTokenizer(text);
	
		while( st.hasMoreTokens() ){
			Token newToken = new Token(st.nextToken());
			out.addToken(newToken);
		}

		return out;
	}
	
	public TokenList calcular(TokenList list){
		
		return null;
	}

}
