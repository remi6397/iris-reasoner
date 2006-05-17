package org.deri.iris.api.terms.concrete;

import java.net.URI;
import org.deri.iris.api.terms.IStringTerm;

public interface Iri extends IStringTerm, Comparable<Iri>, Cloneable {
	public abstract URI getURI();
}
