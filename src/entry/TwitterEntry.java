package entry;
import java.util.Vector;

import twitter4j.Status;
import twitter4j.TwitterObjectFactory;

public class TwitterEntry extends AbsEntry {

	// Variables
	
	// Constructors
	
	private TwitterEntry (){
		this.statusList  = new Vector<Status>();
	}
	
	// Getters And Setters
	
	public static AbsEntry getInstance() {
		if (instance == null){
			instance = new TwitterEntry();
		}
		return instance;
	}
	
	// Methods
	
	@SuppressWarnings("unchecked")
	@Override
	public String getTextFromStatus() {
		String readLine,text = null;
		try {
			if ( ( readLine = this.entryBuffer.readLine() ) != null)   {
				this.statusList.add(TwitterObjectFactory.createStatus(readLine));
				text = (this.filter.filter(this.statusList.lastElement())) ? this.getTextFromStatus() : ( (Status) this.statusList.lastElement() ).getText();
				
			}else
				entryData.close();
		} catch (Exception e) {
			log.error("Error leyendo el archivo de origen");
		}
		return text;
	}

}
