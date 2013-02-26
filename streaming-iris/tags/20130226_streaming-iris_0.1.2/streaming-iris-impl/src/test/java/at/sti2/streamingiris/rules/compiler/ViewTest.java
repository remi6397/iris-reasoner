package at.sti2.streamingiris.rules.compiler;

import java.util.List;

import junit.framework.TestCase;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.api.terms.IVariable;
import at.sti2.streamingiris.api.terms.concrete.IIntegerTerm;
import at.sti2.streamingiris.storage.IRelation;
import at.sti2.streamingiris.storage.simple.SimpleRelationFactory;

public class ViewTest extends TestCase {
	IRelation mRelation;
	View mView;

	protected void setUp() throws Exception {
		mRelation = new SimpleRelationFactory().createRelation();

		// f(1,2,g(2,3))
		ITuple t1 = Helper.createTuple(1, 2,
				Helper.createConstructedTerm("g", 2, 3));
		mRelation.add(t1);

		// f(1,2,g(2,1))
		ITuple t2 = Helper.createTuple(1, 2,
				Helper.createConstructedTerm("g", 2, 1));
		mRelation.add(t2);

		// f(X,Y,g(Y,X))
		ITuple viewCriteria = Helper.createTuple("X", "Y",
				Helper.createConstructedTerm("g", "Y", "X"));

		mView = new View(mRelation, viewCriteria, new SimpleRelationFactory());
	}

	public void testVariables() {
		List<IVariable> variables = mView.variables();

		assertEquals(variables.size(), 2);

		assertEquals(variables.get(0).getValue(), "X");
		assertEquals(variables.get(1).getValue(), "Y");
	}

	public void testView() {
		IRelation v = mView;

		assertEquals(1, v.size());

		ITuple tuple = v.get(0);

		assertEquals(((IIntegerTerm) tuple.get(0)).getValue().intValue(), 1);
		assertEquals(((IIntegerTerm) tuple.get(1)).getValue().intValue(), 2);

		// Iterator<ITuple> it = mView.iterator();
		//
		// assertTrue( it.hasNext() );
		// ITuple tuple = it.next();
		//
		// assertEquals( tuple.size(), 2 );
		// assertEquals( ( (IIntegerTerm) tuple.get( 0 )
		// ).getValue().intValue(), 1 );
		// assertEquals( ( (IIntegerTerm) tuple.get( 1 )
		// ).getValue().intValue(), 2 );
		//
		// assertFalse( it.hasNext() );
	}
}
