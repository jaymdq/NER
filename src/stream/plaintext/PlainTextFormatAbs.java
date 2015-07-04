package stream.plaintext;

public abstract class PlainTextFormatAbs {
	
	public abstract String getText(Object text);
	public Object getObject(Object text){
		return text;
	};
}
