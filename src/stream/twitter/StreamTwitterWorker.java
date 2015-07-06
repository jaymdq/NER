package stream.twitter;

import java.util.Vector;

import stream.StatusExt;
import stream.StreamWorkerAbs;
import twitter4j.FilterQuery;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;


public class StreamTwitterWorker extends StreamWorkerAbs {
	
	public enum Language {
		SPANISH ("es"), ENGLISH ("en");
		
		private String lang;
		Language (String lang) {
			this.lang = lang;
		}
		public String get(){
			return this.lang;
		}
	}
	
	private TwitterStream twitterStream;
	private FilterQuery filterQuery;
	
	public StreamTwitterWorker(String tags, int searchBy){
		this.init(tags, Language.SPANISH, searchBy);
	}
	
	public StreamTwitterWorker(String tags, int searchBy, Language lang){
		this.init(tags, lang, searchBy);
	}
	
	private void init(String tags, Language lang, int searchBy) {
		this.statusList = new Vector<Object>();
		this.twitterStream = new TwitterStreamFactory().getInstance();
		this.filterQuery = new FilterQuery();
		this.filterQuery.language(new String[] { lang.get() });
		switch(searchBy){
		case 0:
			this.filterQuery.track(this.parseTags(tags));
		case 1:
			this.filterQuery.follow(this.parseUsers(tags));
		}
	}
	
	private long[] parseUsers(String tags) {
		String[] users = parseTags(tags);
		long[] out = new long[users.length];
		Twitter tf = new TwitterFactory().getInstance();
		for(int i = 0; i < users.length; i++){
			
			try {
				out[i] = tf.showUser( users[i] ).getId();
			} catch (TwitterException e) {
				e.printStackTrace();
			}
		}
			
		return out;
	}

	private String[] parseTags(String tags) {
		String[] tagsOut = tags.split(",");
		for(int i=0; i < tagsOut.length; i++){
			tagsOut[i] = tagsOut[i].trim();  
		}
		return tagsOut;
	}
	
	public String getNextTweet(){
		String out = null;
		if(this.getTotalTweets() > 0){
			StatusExt tmp = (StatusExt)this.statusList.remove(0);
			if(this.observerList.size() > 0) this.updateObservers();
			out = tmp.getStatus().getText();
		}
		return out;
	}
	
	public Object getNextObject(){
		Object out = null;
		if(this.getTotalTweets() > 0){
			StatusExt tmp = (StatusExt)this.statusList.remove(0);
			if(this.observerList.size() > 0) this.updateObservers();
			out = tmp.getStatus();
		}
		return out;
	}

	@Override
	protected void execute() {
		this.twitterStream.addListener(new TwitterListener(this));
		this.twitterStream.filter(this.filterQuery);
	}
	
	@Override
	public void interrupt() {
		this.twitterStream.shutdown();
	}

	@Override
	protected String formatForSave(Object obj) {
		StatusExt tmp = (StatusExt)obj;
		return tmp.getJSON();
	}
	
}
