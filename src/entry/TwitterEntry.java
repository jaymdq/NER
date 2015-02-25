package entry;
import java.util.Vector;

import twitter4j.Status;
import twitter4j.TwitterObjectFactory;

public class TwitterEntry extends AbsEntry {


	private TwitterEntry (){
		this.statusList  = new Vector<Status>();
	}
	
	public static AbsEntry getInstance() {
		if (instance == null){
			instance = new TwitterEntry();
		}
		return instance;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String getTextFromStatus() {
		String readLine,text = null;
		try {
			if ( ( readLine = this.entryBuffer.readLine() ) != null)   {
				this.statusList.add(TwitterObjectFactory.createStatus(readLine));
				//TODO filtro
				
				text = ( (Status) this.statusList.lastElement() ).getText();
				
			}else
				entryData.close();
		} catch (Exception e) {
			log.error("Error leyendo el archivo de origen");
		}
		return text;
	}

	

}
