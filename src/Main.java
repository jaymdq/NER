import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import entry.TextEntry;
import entry.TwitterEntry;
import filters.RetweetFilter;
import segmentation.Segmenter;
import twitter4j.TwitterException;

public class Main {

	public static void main(String[] args) throws TwitterException {
		
		//TODO 
		//Segmenter s = new Segmenter();
		//System.out.println(s.getListOfTokens("Hola como estas daniel, que transito           feo que hay hoy"));
		
		
		
		RetweetFilter rtf = new RetweetFilter();
		
		TwitterEntry.getInstance().setFilter(rtf);

		if (TwitterEntry.getInstance().setSourceFile("tweets.txt") ){
		//if (TextEntry.getInstance().setSourceFile("textPlano.txt") ){
			String linea ;
			while ( (linea = TwitterEntry.getInstance().getTextFromStatus()) != null){
				System.out.println(linea+"\n");
			}
			
			Logger.getLogger(Main.class).info("Leí todo el archivo");
		}
		
		
	}

}
