package org.deri.iris.api.terms.concrete;

import org.deri.iris.api.terms.ITerm;

public interface GYear extends ITerm, Cloneable, Comparable<GYear> {
	public abstract int getYear();
}
