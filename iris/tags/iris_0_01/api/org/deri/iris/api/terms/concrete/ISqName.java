package org.deri.iris.api.terms.concrete;

import org.deri.iris.api.terms.IStringTerm;

public interface ISqName extends IStringTerm<ISqName> {
	public abstract IIri getNamespace();

	public abstract String getName();
}
