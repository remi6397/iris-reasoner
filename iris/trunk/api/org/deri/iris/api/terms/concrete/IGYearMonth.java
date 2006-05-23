package org.deri.iris.api.terms.concrete;

import org.deri.iris.api.terms.ITerm;

public interface IGYearMonth extends ITerm<IGYearMonth>, Cloneable {
	public abstract int getYear();

	public abstract int getMonth();
}
