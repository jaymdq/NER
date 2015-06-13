package filters;

import twitter4j.Status;

public class RetweetFilter extends AbsFilter {
	
	// Variables
	
	// Constructors
	
	// Getters And Setters
	
	//Methods
	
	@Override
	public boolean filter(Object obj) {
		boolean isRetweet = ((Status) obj).isRetweet();
		return ( !isRetweet && this.next != null ) ? this.next.filter(obj) : isRetweet;
	}

}
