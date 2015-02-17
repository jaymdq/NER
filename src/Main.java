import twitter4j.TwitterException;



public class Main {

	public static void main(String[] args) throws TwitterException {

		if (TwitterEntry.getInstance().setSourceFile("tweets.txt") ){
			String linea ;
			while ( (linea = TwitterEntry.getInstance().getSTextFromStatus()) != null){
				//System.out.println(linea);
			}
			System.out.println("Leí todo el archivo");
		}

	}


}
