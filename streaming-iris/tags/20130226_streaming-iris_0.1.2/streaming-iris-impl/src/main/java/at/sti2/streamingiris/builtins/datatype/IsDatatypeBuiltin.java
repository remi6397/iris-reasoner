package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.BASIC;

import java.net.URI;

import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.IConcreteTerm;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IIri;
import at.sti2.streamingiris.builtins.BooleanBuiltin;

/**
 * Represents the RIF built-in <code>isLiteralOfType</code> as defined in
 * http://www.w3.org/2005/rules/wiki/DTB#pred:isLiteralOfType.
 */
public class IsDatatypeBuiltin extends BooleanBuiltin {

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"IS_DATATYPE", 2);

	/**
	 * Constructor. At least two terms must be passed to the constructor,
	 * otherwise an exception will be thrown.
	 * 
	 * @param terms
	 *            The terms.
	 * @throws IllegalArgumentException
	 *             if one of the terms is {@code null}
	 * @throws IllegalArgumentException
	 *             if the number of terms submitted is not 1
	 * @throws IllegalArgumentException
	 *             if t is <code>null</code>
	 */
	public IsDatatypeBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected boolean computeResult(ITerm[] terms) {
		if (terms[0] instanceof IConcreteTerm) {
			IConcreteTerm term = (IConcreteTerm) terms[0];

			if (terms[1] instanceof IIri) {
				URI iri = ((IIri) terms[1]).getURI();

				return iri.equals(term.getDatatypeIRI());
			}
		}

		return false;
	}

}
