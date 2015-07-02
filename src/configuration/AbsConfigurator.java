package configuration;

import java.util.HashMap;
import java.util.Vector;

import segmentation.Segmenter;

public abstract class AbsConfigurator {

	// Variables
	protected String name;
	protected HashMap<String,Object> parameters;
	
	// Constructors
	public AbsConfigurator(String name){
		this.setName(name);
		this.parameters = new HashMap<String,Object>();
	}
	
	// Getters and Setters
	
	protected String getName(){
		return this.name;
	}
	
	protected void setName(String name){
		this.name = name;
	}
		
	protected Object getParameter(String key){
		return parameters.get(key);
	}
	
	protected void setParameter(String key, Object obj){
		this.parameters.put(key, obj);
	}
	
	// Methods
	public Object configure(String params){
		Vector<String> listOfParams = Segmenter.getSegmentation(params);
		
		int operationStatus = checkParameters(listOfParams);
		if (operationStatus > 0)
			return null;
		
		return configureObject();
	}
	
	protected abstract int checkParameters(Vector<String> params);
	protected abstract Object configureObject();
	
	
}
