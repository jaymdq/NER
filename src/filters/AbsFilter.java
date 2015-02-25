package filters;

public abstract class AbsFilter {

	protected AbsFilter next = null;
	
	public abstract boolean filter(Object obj);
	
	public void setNext(AbsFilter next){
		this.next = next;
	}
	
}
