package examples.arff;

import java.util.Vector;

import dictionary.chunk.Chunk;
import examples.arff.filter.ParamFilterAbs;

public class ArffParameter {

	private String name;
	private String type;
	private ParamFilterAbs filter = null;
	
	public ArffParameter(String name){
		this.setName(name);
	}
	
	public ArffParameter(String name, ParamFilterAbs filter){
		this.setName(name);
		this.setFilter(filter);
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public void setFilter(ParamFilterAbs filter){
		this.filter = filter;
	}
	
	public ParamFilterAbs getFilter(){
		return this.filter;
	}
	
	public void setType(String[] types) {
		if(types == null || types.length <= 0) return ;
		String type = types[0]; 
		for(int i = 1; i < types.length; i++)
			type += ", "+types[i];
		this.type = "{" + type + "}";
	}
	
	public String getType() {
		return type;
	}
	
	public String getValue(Vector<Chunk> chunks){
		if(this.filter == null) return this.getType();
		return this.filter.apply(chunks);
	}
	
	public String toString(){
		return "@attribute "+this.getName()+" "+this.getType();		
	}
}