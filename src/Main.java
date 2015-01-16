import twitter4j.Status;
import twitter4j.StatusAdapter;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;


public class Main {

	public static void main(String[] args) throws TwitterException {
		
	
		TwitterObjectFactory t;
		
		StatusAdapter s = (StatusAdapter) TwitterObjectFactory.createStatus("{clave:valor}");
		
		//t.getRawJSON(s);
		
		
	}

}
