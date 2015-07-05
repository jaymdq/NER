package examples.arff.filter;

import java.util.Vector;

import dictionary.chunk.AbsChunk;

public class EventFilter extends ParamFilterAbs {
	
	private int limit;
	
	public EventFilter(int toCheck){
		this.setLimit(toCheck);
	}
	
	public void setLimit(int limit) {
		this.limit = limit;
	}
	
	@Override
	public String apply(Vector<AbsChunk> chunks) {
		String out = "";
		for(int i=0; i < this.limit; i++){
			boolean found = false;
			for(int j=0; !found && j < chunks.size(); j++){
				AbsChunk c = chunks.elementAt(j);
				found = c.getCategoryType().contains(this.values[i]);
			}
			if( found && !out.contains(this.values[i]) ){
				out += this.values[i]+" ";
				
			}
		}
		out = out.trim();
		return out;	
	}
}