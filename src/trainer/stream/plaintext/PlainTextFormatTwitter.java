package trainer.stream.plaintext;

import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;

public class PlainTextFormatTwitter extends PlainTextFormatAbs {

	@Override
	public String getText(Object text) {
		Status status = null;
		try {
			status = TwitterObjectFactory.createStatus((String)text);
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(status != null)
			return status.getText();
		return "";
	}

}
