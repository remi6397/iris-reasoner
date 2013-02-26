package at.sti2.streamingiris.utils.equivalence;

import java.util.Set;

import at.sti2.streamingiris.api.terms.ITerm;

/**
 * Defines equivalence between terms.
 * 
 * @author Adrian Marte
 */
public interface IEquivalentTerms {

	/**
	 * Checks if the specified terms are equivalent.
	 * 
	 * @param x
	 *            The first term.
	 * @param y
	 *            The second term.
	 * @return <code>true</code> if the two given terms are equivalent,
	 *         <code>false</code> otherwise.
	 */
	public boolean areEquivalent(ITerm x, ITerm y);

	/**
	 * Defines the specified terms as equivalent.
	 * 
	 * @param x
	 *            The first term.
	 * @param y
	 *            The second term.
	 */
	public void setEquivalent(ITerm x, ITerm y);

	/**
	 * Returns a representative term for the specified terms. If two terms are
	 * equivalent, they have the same representative term.
	 * 
	 * @param term
	 *            The term.
	 * @return The representative term for the specified term.
	 */
	public ITerm findRepresentative(ITerm term);

	/**
	 * Returns the set of terms which are equivalent to the specified term. The
	 * set also contains the term itself.
	 * 
	 * @param term
	 *            The term.
	 * @return The set of terms which are equivalent to the specified term. The
	 *         set also contains the term itself.
	 */
	public Set<ITerm> getEquivalent(ITerm term);

}
