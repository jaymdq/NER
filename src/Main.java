import twitter4j.TwitterException;

public class Main {

	public static void main(String[] args) throws TwitterException {

		//if (TwitterEntry.getInstance().setSourceFile("tweets.txt") ){
		if (TextEntry.getInstance().setSourceFile("textPlano.txt") ){
			String linea ;
			while ( (linea = TextEntry.getInstance().getTextFromStatus()) != null){
				System.out.println(linea);
			}
			System.out.println("\nLeí todo el archivo");
		}
		
	}

}
