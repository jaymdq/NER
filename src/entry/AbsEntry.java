package entry;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Vector;

import org.apache.log4j.Logger;

import filters.AbsFilter;


public abstract class AbsEntry {

	// Variables
	@SuppressWarnings("rawtypes")
	protected Vector statusList;
	protected static AbsEntry instance = null;
	protected DataInputStream entryData;
	protected BufferedReader entryBuffer;
	protected AbsFilter filter;
	protected final static Logger log = Logger.getLogger("Entrada");
	 
	// Constructors
	
	// Getters And Setters
	
	public Object getStatusElementAt(int pos){
		return statusList.elementAt(pos);
	}
	
	public int getStatusSize(){
		return statusList.size();
	}
	
	public void setFilter(AbsFilter filter){
		this.filter = filter;
	}
	
	// Methods
	
	public abstract String getTextFromStatus();
	
	public boolean setSourceFile(String path){
		File file = new File(path);
		try {
			FileInputStream fstream = new FileInputStream(file);
			this.entryData = new DataInputStream(fstream);
			this.entryBuffer = new BufferedReader(new InputStreamReader(this.entryData));
		} catch (FileNotFoundException e) {
			log.error("No existe el archivo: \""+ path+"\"");
			return false;
		}
		return true;
	}
	
}
