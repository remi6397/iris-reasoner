package org.deri.iris.api.terms.concrete;

import org.deri.iris.api.terms.IStringTerm;

public interface SqName extends IStringTerm, Cloneable, Comparable<SqName> {
	public abstract Iri getNamespace();

	public abstract String getName();
}
