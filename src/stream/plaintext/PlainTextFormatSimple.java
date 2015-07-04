package stream.plaintext;

public class PlainTextFormatSimple extends PlainTextFormatAbs {

	@Override
	public String getText(Object text) {
		return (String)text;
	}

}
