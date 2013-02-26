package at.sti2.streamingiris.api.terms.concrete;

import at.sti2.streamingiris.api.terms.IConcreteTerm;

/**
 * <p>
 * An interface for representing a sQName identifier.
 * </p>
 * 
 * @author Richard Pöttler
 */
public interface ISqName extends IConcreteTerm {
	/**
	 * Return the wrapped type.
	 */
	public String getValue();

	/**
	 * Returns the namespace.
	 * 
	 * @return The namespace.
	 */
	public IIri getNamespace();

	/**
	 * Returns the name.
	 * 
	 * @return The name.
	 */
	public String getName();
}
