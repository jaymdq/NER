package configuration;

import java.util.Vector;

import utils.Pair;
import dictionary.chunk.Chunk;
import event.twevent.FixedWindow;
import event.twevent.Twevent;

public class TweventConfigurator extends AbsEventDetectionConfigurator {

	// Variables
	
	// Constructors

	public TweventConfigurator(String name, Vector<Pair<String, Vector<Chunk>>> tweets) {
		super(name, tweets);
	}
	
	// Getters and Setters

	// Methods
	
	@Override
	protected int checkParameters(Vector<String> params) {
		int out = 0;
		
		setParameter("-l",0.0);
		
		for (int i = 0; i < params.size(); i++){
			String param = params.elementAt(i);
			if (param.toLowerCase().equals("-l")){
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
			}
		}
		
		return out;
	}

	@Override
	protected Object configureObject() {
		Twevent out = null;

		//Parameters
		double lowerLimit = (double) getParameter("-l");
		
		out = new Twevent(getTweets(),new FixedWindow(tweets.size()),lowerLimit);
				
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
		default:
			out = "Invalid argument";
			break;
		}
		
		return out;
	}

	//Vector< Pair< String, Vector<Chunk> > >  tweets, FixedWindow fixedWindow, double loweLimit
	
	

}
