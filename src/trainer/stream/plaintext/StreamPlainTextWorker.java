package trainer.stream.plaintext;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import trainer.stream.StreamWorkerAbs;

public class StreamPlainTextWorker extends StreamWorkerAbs {
	
	private String fileActive;
	private PlainTextFormatAbs format;
	
	public StreamPlainTextWorker(String path, PlainTextFormatAbs format){
		this.fileActive = path;
		this.statusList = new Vector<Object>(2);
		if(format == null)
			format = new PlainTextFormatSimple();
		this.format = format;
	}
	
	@Override
	public String getNextTweet(){
		String out = null;
		if(this.getTotalTweets() > 0){
			Object tmp = this.statusList.remove(0);
			if(this.counter != null) this.updateCounter();
			out = this.format.getText(tmp);
		}
		return out;
	}
	
	@Override
	protected void execute() {
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		try {
			fileReader = new FileReader( new File(this.fileActive) );
			bufferedReader = new BufferedReader(fileReader);
			
			String line;
			
			while( (line=bufferedReader.readLine()) != null && !this.isInterrupted()){
				this.notify(line);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(fileReader != null)
					fileReader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
