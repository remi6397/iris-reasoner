package at.sti2.streamingiris.builtins.list;

import junit.framework.TestCase;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.api.terms.IConcreteTerm;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IList;
import at.sti2.streamingiris.factory.Factory;
import at.sti2.streamingiris.terms.concrete.IntTerm;

public abstract class AbstractListBuiltinTest extends TestCase {

	protected static final ITerm X = Factory.TERM.createVariable("X");

	protected static final ITerm Y = Factory.TERM.createVariable("Y");

	protected static final ITerm Z = Factory.TERM.createVariable("Z");

	protected static final IConcreteTerm ZERO = new IntTerm(0);

	protected static final IConcreteTerm ONE = new IntTerm(1);

	protected static final IConcreteTerm TWO = new IntTerm(2);

	protected static final IConcreteTerm THREE = new IntTerm(3);

	protected static final IConcreteTerm FOUR = new IntTerm(4);

	protected static final ITuple EMPTY_TUPLE = Factory.BASIC.createTuple();

	protected static final IList EMPTY_LIST = new at.sti2.streamingiris.terms.concrete.List();

	protected ITuple args = null;

	protected ITuple actual = null;

}
