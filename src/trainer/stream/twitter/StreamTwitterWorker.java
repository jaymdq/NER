package trainer.stream.twitter;

import java.util.Vector;

import trainer.stream.StreamWorkerAbs;
import twitter4j.FilterQuery;
import twitter4j.Status;
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
	
	public StreamTwitterWorker(String tags){
		this.init(tags, Language.SPANISH);
	}
	
	public StreamTwitterWorker(String tags, Language lang){
		this.init(tags, lang);
	}
	
	private void init(String tags, Language lang) {
		this.filterQuery = new FilterQuery();
		this.filterQuery.language(new String[] { lang.get() });
		this.filterQuery.track(this.parseTags(tags));
		this.statusList = new Vector<Object>();
		this.twitterStream = new TwitterStreamFactory().getInstance();
	}
	
	private String[] parseTags(String tags) {
		String[] tagsOut = tags.split(",");
		for(String tag: tagsOut){
			tag.trim();
		}
		return tagsOut;
	}
	
	public String getNextTweet(){
		String out = null;
		if(this.getTotalTweets() > 0){
			Status tmp = (Status)this.statusList.remove(0);
			if(this.counter != null) this.updateCounter();
			out = tmp.getText();
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
}
