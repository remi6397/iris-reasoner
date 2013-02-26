package at.sti2.streamingiris.builtins.string;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IXMLLiteral;
import at.sti2.streamingiris.builtins.EqualBuiltin;

/**
 * <p>
 * Represents the RIF built-in predicate pred:XMLLiteral-equal.
 * </p>
 */
public class XMLLiteralEqualBuiltin extends EqualBuiltin {

	/** The predicate defining this built-in. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"XMLLITERAL_EQUAL", 2);

	public XMLLiteralEqualBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	@Override
	protected ITerm computeMissingTerm(int missingTermIndex, ITerm[] terms) {
		if (checkTypes(missingTermIndex, terms, IXMLLiteral.class)) {
			return super.computeMissingTerm(missingTermIndex, terms);
		}

		return null;
	}

}
