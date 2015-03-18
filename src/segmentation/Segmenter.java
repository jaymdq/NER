package segmentation;

import java.util.StringTokenizer;
import java.util.Vector;

public class Segmenter {

	//TODO ver si se puede agregar mas funcionalidad tipo separar y borrar "," cosas por el estilo
	public Segmenter(){

	}

	public Vector<String> getListOfTokens(String text){
		Vector<String> out = new Vector<String>();
		StringTokenizer st = new StringTokenizer(text);
		while( st.hasMoreTokens() ){
			out.add(st.nextToken());
		}
		return out;
	}

}
