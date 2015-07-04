package stream;

import java.util.Vector;

public abstract class StreamWorkerAbs extends Thread {
	protected Vector<Object> statusList;
	protected Vector<StreamObserver> observerList = new Vector<StreamObserver>();
	//protected LBCounter counter = null;
	
	public int getTotalTweets(){
		return this.statusList.size();
	}
	
	public void notify(Object text) {
		this.statusList.addElement(text);
		if(this.observerList.size() > 0) this.updateObservers();
	}
	
	public abstract String getNextTweet();
	
	public abstract Object getNextObject();
	
	public Vector<String> getForSave(){
		Vector<String> out = new Vector<String>();
		for(Object obj: this.statusList){
			out.add(formatForSave(obj));
		}
		return out;
	}
	
	public void addObserver(StreamObserver o){
		this.observerList.addElement(o);
	}
	
	protected void updateObservers(){
		for(StreamObserver o: this.observerList)
			o.update(this);
	}

	public void run(){
		execute();
	}
	
	protected abstract void execute();
	
	protected abstract String formatForSave(Object obj);
	
}
