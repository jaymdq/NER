package trainer.stream;

import java.util.Vector;

import trainer.util.LBCounter;

public abstract class StreamWorkerAbs extends Thread {
	protected Vector<Object> statusList;
	protected LBCounter counter = null;
	
	public int getTotalTweets(){
		return this.statusList.size();
	}
	
	public void notify(Object text) {
		this.statusList.addElement(text);
		if(this.counter != null) this.updateCounter();
	}
	
	public abstract String getNextTweet();
	
	public Vector<String> getForSave(){
		Vector<String> out = new Vector<String>();
		for(Object obj: this.statusList){
			out.add(formatForSave(obj));
		}
		return out;
	}
	
	public void setCounter(LBCounter counter){
		this.counter = counter;
	}
	
	protected void updateCounter(){
		this.counter.updateCounter(this.getTotalTweets());
	}

	public void run(){
		execute();
	}
	
	protected abstract void execute();
	
	protected abstract String formatForSave(Object obj);
	
}
