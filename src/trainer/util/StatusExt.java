package trainer.util;

import twitter4j.Status;
import twitter4j.TwitterObjectFactory;

public class StatusExt {
	private String json;
	private Status status;
	
	public StatusExt(Status status){
		this.status = status;
		this.json = TwitterObjectFactory.getRawJSON(status);
	}
	
	public Status getStatus(){
		return this.status;
	}
	
	public String getJSON(){
		return this.json;
	}
}
