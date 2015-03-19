package segmentation;

import java.util.StringTokenizer;
import java.util.Vector;

public class Segmenter {

	private String text;
	private Vector<String> tokens;
	private int lastTokenStartPosition;
	private int lastTokenEndPosition;
	private int actualTokenPosition;
	private boolean onlyCharacters;
	private boolean caseSensitive;

	public Segmenter(String text, boolean caseSensitive, boolean onlyCharacters){
		this.text = text;
		this.tokens = getSegmentation(text);
		this.lastTokenStartPosition = 0;
		this.lastTokenEndPosition = 0;
		this.actualTokenPosition = 0;
		this.onlyCharacters = onlyCharacters;
		this.caseSensitive = caseSensitive;		
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
			String actualToken;
			if (onlyCharacters)
				actualToken = tokens.get(actualTokenPosition).replaceAll("[^A-Za-z0-9]+", "");
			else
				actualToken = tokens.get(actualTokenPosition);
			if (!caseSensitive)
				actualToken = actualToken.toLowerCase();
			actualTokenPosition++;
			lastTokenStartPosition = text.indexOf(actualToken,lastTokenEndPosition);
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

	public boolean isOnlyCharacters() {
		return onlyCharacters;
	}

}
