package segmentation;

import java.util.Vector;

public abstract class AbsToken {

	public abstract String getContenido();
	public abstract Vector<AbsToken> split();
}
