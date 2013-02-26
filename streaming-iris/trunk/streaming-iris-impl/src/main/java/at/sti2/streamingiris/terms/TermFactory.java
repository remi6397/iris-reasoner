package at.sti2.streamingiris.terms;

import java.util.Arrays;
import java.util.Collection;

import at.sti2.streamingiris.api.factory.ITermFactory;
import at.sti2.streamingiris.api.terms.IConstructedTerm;
import at.sti2.streamingiris.api.terms.IStringTerm;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.IVariable;

/**
 * @author richi
 * 
 */
public class TermFactory implements ITermFactory {

	private static final ITermFactory FACTORY = new TermFactory();

	private TermFactory() {
		// this is a singelton
	}

	public IConstructedTerm createConstruct(String name, Collection<ITerm> terms) {
		return new ConstructedTerm(name, terms);
	}

	public IConstructedTerm createConstruct(String name, ITerm... terms) {
		return createConstruct(name, Arrays.asList(terms));
	}

	public IStringTerm createString(String arg) {
		return new StringTerm(arg);
	}

	public IVariable createVariable(String name) {
		return new Variable(name);
	}

	public static ITermFactory getInstance() {
		return FACTORY;
	}

}
