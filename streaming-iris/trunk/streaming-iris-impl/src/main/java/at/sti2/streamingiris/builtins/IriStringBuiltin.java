package at.sti2.streamingiris.builtins;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.IConcreteTerm;
import at.sti2.streamingiris.api.terms.IStringTerm;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;
import at.sti2.streamingiris.utils.equivalence.IEquivalentTerms;

/**
 * <p>
 * Represents the RIF built-in predicate func:iri-string.
 * </p>
 */
public class IriStringBuiltin extends BooleanBuiltin {

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"IRI_STRING", 2);

	/**
	 * Creates the built-in for the specified terms.
	 * 
	 * @param terms
	 *            The terms.
	 * @throws NullPointerException
	 *             If one of the terms is <code>null</code>.
	 * @throws IllegalArgumentException
	 *             If the number of terms submitted is not 2 .
	 */
	public IriStringBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected boolean computeResult(ITerm[] terms) {
		IEquivalentTerms equivalentTerms = getEquivalenceClasses();

		// Check if the two terms are equivalent.
		if (equivalentTerms != null) {
			if (equivalentTerms.areEquivalent(terms[0], terms[1])) {
				return true;
			}
		}

		// Assuming the IRI is represented by some concrete term.
		if (terms[0] instanceof IConcreteTerm
				&& terms[1] instanceof IStringTerm) {
			IConcreteTerm iri = (IConcreteTerm) terms[0];
			IStringTerm string = (IStringTerm) terms[1];

			try {
				// Create an IRI for the string.
				IConcreteTerm stringIri = Factory.CONCRETE.createIri(string
						.toCanonicalString());

				// Check if the two IRIs are equivalent.
				if (equivalentTerms.areEquivalent(iri, stringIri)) {
					return true;
				}

				// Check if the two IRIs are equal.
				return iri.equals(stringIri);
			} catch (IllegalArgumentException e) {
				return false;
			}
		}

		return false;
	}

}
