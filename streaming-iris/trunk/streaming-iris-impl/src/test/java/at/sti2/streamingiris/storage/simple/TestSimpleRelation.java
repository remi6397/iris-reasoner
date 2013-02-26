package at.sti2.streamingiris.storage.simple;

import junit.framework.TestCase;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.rules.compiler.Helper;
import at.sti2.streamingiris.storage.IRelation;

public class TestSimpleRelation extends TestCase {
	IRelation mRelation;

	protected void setUp() throws Exception {
		mRelation = new SimpleRelation();
	}

	public void testAll() {
		// Ensure the relation is empty
		assertEquals(mRelation.size(), 0);

		// Insert a new tuple
		ITuple t1 = Helper.createTuple(2, 1);
		mRelation.add(t1);
		assertEquals(mRelation.size(), 1);
		assertEquals(mRelation.get(0), t1);

		// Try adding same tuple again and it should not accept it
		mRelation.add(t1);
		assertEquals(mRelation.size(), 1);
		assertEquals(mRelation.get(0), t1);

		// Add a new tuple
		ITuple t2 = Helper.createTuple(2, 2);
		mRelation.add(t2);
		assertEquals(mRelation.size(), 2);
		assertEquals(mRelation.get(0), t1);
		assertEquals(mRelation.get(1), t2);

		// Create a new relation and check that addAll() works.
		IRelation r2 = new SimpleRelation();
		r2.addAll(mRelation);
		assertEquals(r2.size(), 2);
		assertEquals(r2.get(0), t1);
		assertEquals(r2.get(1), t2);

		// Now check that it is not possible to add t1 and t2 to the new
		// relation.
		r2.add(t1);
		r2.add(t2);
		assertEquals(r2.size(), 2);
	}
}
