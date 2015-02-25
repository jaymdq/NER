package filters;

import twitter4j.Status;

public class RetweetFilter extends AbsFilter {
	
	@Override
	public boolean filter(Object obj) {
		boolean isRetweet = ((Status) obj).isRetweet();
		return ( !isRetweet && this.next != null ) ? this.next.filter(obj) : isRetweet;
	}

}
