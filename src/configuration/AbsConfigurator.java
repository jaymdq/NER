package configuration;

import java.util.HashMap;
import java.util.Vector;

import segmentation.Segmenter;

public abstract class AbsConfigurator {

	// Variables
	protected String name;
	protected HashMap<String,Object> parameters;
	private int operationStatus;
	
	// Constructors
	public AbsConfigurator(String name){
		this.setName(name);
		this.parameters = new HashMap<String,Object>();
		setOperationStatus(0);
	}
	
	// Getters and Setters
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name = name;
	}
		
	protected Object getParameter(String key){
		return parameters.get(key);
	}
	
	protected void setParameter(String key, Object obj){
		this.parameters.put(key, obj);
	}
		
	public int getOperationStatus() {
		return operationStatus;
	}

	public void setOperationStatus(int operationStatus) {
		this.operationStatus = operationStatus;
	}

	// Methods
	
	public Object configure(String params){
		Vector<String> listOfParams = Segmenter.getSegmentation(params);
		
		operationStatus = checkParameters(listOfParams);
		if (operationStatus > 0)
			return null;
		
		return configureObject();
	}
	
	protected abstract int checkParameters(Vector<String> params);
	protected abstract Object configureObject();
	public abstract String getErrorReason();
	
	
}
