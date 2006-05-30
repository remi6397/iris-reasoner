package org.deri.iris.api.terms.concrete;

import java.net.URI;
import org.deri.iris.api.terms.IStringTerm;

public interface IIri extends IStringTerm<IIri> {
	public abstract URI getURI();
}
