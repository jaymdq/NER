package examples.arff.filter;

import java.util.Vector;

import dictionary.chunk.Chunk;

public class TokenFilter extends ParamFilterAbs {

	private Vector<String> categoryList;
	
	public TokenFilter(Vector<String> categoryList){
		
	}
	
	public void addCategory(Vector<String> categoryList){
		this.categoryList.addAll(categoryList);
	}
	
	public void addCategory(String category){
		this.categoryList.addElement(category);
	}
			
	@Override
	public String apply(Vector<Chunk> chunks) {
		boolean exist = false;
		for(int i=0; !exist && i < chunks.size(); i++){
			Chunk c = chunks.elementAt(i);
			exist = this.categoryList.contains(c.getCategoryType());
		}
		if(exist)
			return this.values[1];
		
		return this.values[0];
	}

}
