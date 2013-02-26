package at.sti2.streamingiris.api.terms.concrete;

import java.net.URI;

import at.sti2.streamingiris.api.terms.IConcreteTerm;

/**
 * <p>
 * An interface for representing an IRI (Internationalized Resource Identifiers)
 * identifier.
 * </p>
 * 
 * @author Richard Pöttler
 */
public interface IIri extends IConcreteTerm {
	/**
	 * Return the wrapped type.
	 */
	public String getValue();

	/**
	 * Returns the URI.
	 * 
	 * @return The URI.
	 */
	public URI getURI();
}
