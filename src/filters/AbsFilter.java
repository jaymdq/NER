package filters;

public abstract class AbsFilter {
	
	//Protected Variables
	protected AbsFilter next = null;
	
	//Abstract Methods
	public abstract boolean filter(Object obj);
	
	//Template Methods
	public void setNext(AbsFilter next){
		this.next = next;
	}
	
}
