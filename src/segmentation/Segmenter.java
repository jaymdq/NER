package segmentation;

import java.util.StringTokenizer;
import java.util.Vector;

public class Segmenter {

	private String text;
	private Vector<String> tokens;
	private int lastTokenStartPosition;
	private int lastTokenEndPosition;
	private int actualTokenPosition;


	public Segmenter(String text){
		this.text = text;
		this.tokens = getSegmentation(text);
		this.lastTokenStartPosition = 0;
		this.lastTokenEndPosition = 0;
		this.actualTokenPosition = 0;
	}

	public Vector<String> getSegmentation(){
		return tokens;
	}

	public static Vector<String> getSegmentation(String text){
		Vector<String> out = new Vector<String>();
		StringTokenizer st = new StringTokenizer(text);
		while( st.hasMoreTokens() ){
			out.add(st.nextToken());
		}
		return out;
	}

	public String getNextToken(){
		if (actualTokenPosition < tokens.size()){
			String actualToken = tokens.get(++actualTokenPosition);
			lastTokenStartPosition = text.indexOf(actualToken);
			lastTokenEndPosition = lastTokenStartPosition + actualToken.length();
			return actualToken;	
		}
		else
			return null;
	}

	public void resetPositionToStart(){
		this.actualTokenPosition = 0;
	}

	public int getLastTokenStartPosition() {
		return lastTokenStartPosition;
	}

	public int getLastTokenEndPosition() {
		return lastTokenEndPosition;
	}

	public String getText() {
		return text;
	}

}
