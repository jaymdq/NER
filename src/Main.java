import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterObjectFactory;



public class Main {

	public static void main(String[] args) throws TwitterException {
		
		Vector<Status> status = new Vector<Status>();
		
		try{
			File f = new File("tweets.txt");
            FileInputStream fstream = new FileInputStream(f);
            DataInputStream entrada = new DataInputStream(fstream);
            BufferedReader buffer = new BufferedReader(new InputStreamReader(entrada));
            String strLinea;
            while ((strLinea = buffer.readLine()) != null)   {
            	status.add(TwitterObjectFactory.createStatus(strLinea));
            }
            entrada.close();
        }catch (Exception e){
            System.err.println("Ocurrio un error: " + e.getMessage());
        }
		
		System.out.println("Longitud: "+status.size());
		
	}
	

}
