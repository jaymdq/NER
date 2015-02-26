import org.apache.log4j.Logger;

import entry.TextEntry;
import entry.TwitterEntry;
import filters.RetweetFilter;
import twitter4j.TwitterException;

public class Main {

	public static void main(String[] args) throws TwitterException {
		
		RetweetFilter rtf = new RetweetFilter();
		
		TwitterEntry.getInstance().setFilter(rtf);

		if (TwitterEntry.getInstance().setSourceFile("tweets.txt") ){
		//if (TextEntry.getInstance().setSourceFile("textPlano.txt") ){
			String linea ;
			while ( (linea = TwitterEntry.getInstance().getTextFromStatus()) != null){
				System.out.println(linea);
			}
			
			Logger.getLogger("Main").info("Leí todo el archivo");
		}
		
	}

}
