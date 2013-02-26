package at.sti2.streamingiris.utils.equivalence;

import java.util.Collections;
import java.util.Set;

import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.utils.DisjointSets;

/**
 * An utility class to define equivalence between terms. Uses the disjoint-set
 * data structure.
 * 
 * @see at.sti2.streamingiris.utils.DisjointSets
 * @author Adrian Marte
 */
public class TermEquivalence implements IEquivalentTerms {

	/**
	 * The disjoint-set data structure.
	 */
	private final DisjointSets<ITerm> disjointSets = new DisjointSets<ITerm>();

	/**
	 * Creates a new term equivalence relation.
	 */
	public TermEquivalence() {
	}

	public boolean areEquivalent(ITerm x, ITerm y) {
		if (x.equals(y)) {
			return true;
		}

		return disjointSets.areInSameSet(x, y);
	}

	public void setEquivalent(ITerm x, ITerm y) {
		disjointSets.putInSameSet(x, y);
	}

	public ITerm findRepresentative(ITerm term) {
		ITerm representative = disjointSets.find(term);

		if (representative == null) {
			return term;
		}

		return representative;
	}

	public Set<ITerm> getEquivalent(ITerm term) {
		Set<ITerm> set = disjointSets.getSetOf(term);

		if (set.isEmpty()) {
			return Collections.singleton(term);
		}

		return set;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}

		if (!(obj instanceof TermEquivalence)) {
			return false;
		}

		TermEquivalence thatEquivalence = (TermEquivalence) obj;

		return disjointSets.equals(thatEquivalence.disjointSets);
	}

	@Override
	public int hashCode() {
		return disjointSets.hashCode();
	}

	@Override
	public String toString() {
		return disjointSets.toString();
	}

}
