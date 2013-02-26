package at.sti2.streamingiris.utils.equivalence;

import java.util.Collections;
import java.util.Set;

import at.sti2.streamingiris.api.terms.ITerm;

/**
 * An ineffective term equivalence implementation, i.e. this term equivalence
 * relation does not keep track of equivalent terms, but only provides the basic
 * required functionality. In particular:
 * <ul>
 * <li>
 * <code>areEquivalent</code> only checks for equality of the two terms using
 * the corresponding <code>equals</code> method.</li>
 * <li><code>findRepresentative</code> returns the term itself.</li>
 * <li><code>getEquivalent</code> returns a singleton set containing the term
 * itself.</li>
 * <li><code>setEquivalent</code> does nothing.</li>
 * </ul>
 * 
 * @author Adrian Marte
 */
public class IgnoreTermEquivalence implements IEquivalentTerms {

	public boolean areEquivalent(ITerm x, ITerm y) {
		return x.equals(y);
	}

	public ITerm findRepresentative(ITerm term) {
		return term;
	}

	public Set<ITerm> getEquivalent(ITerm term) {
		return Collections.singleton(term);
	}

	public void setEquivalent(ITerm x, ITerm y) {
		// Do nothing.
	}

}
