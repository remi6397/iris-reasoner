package at.sti2.streamingiris.builtins.numeric;

import junit.framework.TestCase;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 */
public abstract class AbstractNumericTest extends TestCase {

	protected static final ITerm X = Factory.TERM.createVariable("X");

	protected static final ITerm Y = Factory.TERM.createVariable("Y");

	protected static final ITerm Z = Factory.TERM.createVariable("Z");

	protected static final ITuple EMPTY_TUPLE = Factory.BASIC.createTuple();

	protected ITuple args = null;

	protected ITuple expected = null;

	protected ITuple actual = null;

	public AbstractNumericTest(String name) {
		super(name);
	}

}
