package org.deri.iris.api.terms.concrete;

import org.deri.iris.api.terms.ITerm;

public interface GYearMonth extends ITerm, Cloneable, Comparable<GYearMonth> {
	public abstract int getYear();

	public abstract int getMonth();
}
