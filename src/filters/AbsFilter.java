package filters;

public abstract class AbsFilter {
	
	// Variables
	
	protected AbsFilter next = null;
	
	// Methods
	
	public abstract boolean filter(Object obj);
	
	public void setNext(AbsFilter next){
		this.next = next;
	}
	
}
