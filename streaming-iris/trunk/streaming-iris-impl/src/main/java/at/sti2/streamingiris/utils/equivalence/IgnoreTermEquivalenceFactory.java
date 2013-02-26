package at.sti2.streamingiris.utils.equivalence;


/**
 * A factory that creates data-structures that ignore term equivalence.
 * 
 * @author Adrian Marte
 */
public class IgnoreTermEquivalenceFactory implements IEquivalentTermsFactory {

	public IEquivalentTerms createEquivalentTerms() {
		return new IgnoreTermEquivalence();
	}

}
