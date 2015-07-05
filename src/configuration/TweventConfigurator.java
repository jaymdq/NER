package configuration;

import java.util.Vector;

import event.twevent.FixedWindow;
import event.twevent.Twevent;

public class TweventConfigurator extends AbsConfigurator {

	// Variables
	
	// Constructors

	public TweventConfigurator(String name) {
		super(name);
	}
	
	// Getters and Setters

	// Methods
	
	@Override
	protected int checkParameters(Vector<String> params) {
		int out = 0;
		
		setParameter("-l",0.0);
		setParameter("-L",0.0);
		setParameter("-p",0.2);
		setParameter("-P",0.2);
		
		for (int i = 0; i < params.size(); i++){
			String param = params.elementAt(i);
			
			switch(param){
			case "-l":
				//Check if there is another element next to it
				if (i+1 < params.size()){
					try{
						double doubleToAdd = Double.parseDouble(params.elementAt(i+1));
						setParameter("-l",doubleToAdd);
						i++;
					} catch (Exception e) {
						out = 1;
					}
				}else
					out = 2;
				break;
			case "-p":
				if (i+1 < params.size()){
					try{
						double doubleToAdd = Double.parseDouble(params.elementAt(i+1));
						setParameter("-p",doubleToAdd);
						i++;
					} catch (Exception e) {
						out = 3;
					}
				}else
					out = 2;
				break;
			default:
				out = 4;
				break;
			}
		}
		
		return out;
	}

	@Override
	protected Object configureObject() {
		Twevent out = null;

		//Parameters
		double lowerLimit = (double) getParameter("-l");
		double observationProb = (double) getParameter("-p");
		
		out = new Twevent(new FixedWindow(10,observationProb), lowerLimit, observationProb);
				
		return out;
	}

	@Override
	public String getErrorReason() {
		String out = "";
		switch (getOperationStatus()){
		case 0 :
			out = "No errors";
			break;
		case 1 :
			out = "-l/-L DOUBLE";
			break;
		case 2 :
			out = "We need another argument";
			break;
		case 3 :
			out = "-p/-P DOUBLE";
			break;
		default:
			out = "Invalid argument";
			break;
		}
		
		return out;
	}

	//Vector< Pair< String, Vector<Chunk> > >  tweets, FixedWindow fixedWindow, double loweLimit
	
	

}
