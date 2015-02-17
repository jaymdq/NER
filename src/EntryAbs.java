import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Vector;

import org.apache.log4j.Logger;


public abstract class EntryAbs {

	//Protected Variables
	@SuppressWarnings("rawtypes")
	protected Vector statusList;
	protected static EntryAbs instance = null;
	protected DataInputStream entryData;
	protected BufferedReader entryBuffer;
	
	//Private Variable
	protected final static Logger log = Logger.getLogger("Entrada");
	 
	//Abstracts Methods
	public abstract String getSTextFromStatus();
	
	//Template Methods
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
