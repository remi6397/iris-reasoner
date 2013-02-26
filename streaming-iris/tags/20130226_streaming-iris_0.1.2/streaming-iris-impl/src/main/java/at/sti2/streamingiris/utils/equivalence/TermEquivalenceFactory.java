package at.sti2.streamingiris.utils.equivalence;


/**
 * Creates a data structure that uses the disjoint-set data structure to
 * establish equivalence relations between terms.
 * 
 * @author Adrian Marte
 */
public class TermEquivalenceFactory implements IEquivalentTermsFactory {

	public IEquivalentTerms createEquivalentTerms() {
		return new TermEquivalence();
	}

}
