package at.sti2.streamingiris.storage.simple;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.rules.compiler.Helper;
import at.sti2.streamingiris.rules.compiler.Utils;
import at.sti2.streamingiris.rules.compiler.View;
import at.sti2.streamingiris.storage.IRelation;

public class TestSimpleIndex extends TestCase {
	IRelation mRelation;
	View mView;
	SimpleIndex mIndex;

	protected void setUp() throws Exception {
		mRelation = new SimpleRelation();

		mRelation.add(Helper.createTuple(1, 1, 1));
		mRelation.add(Helper.createTuple(1, 1, 2));
		mRelation.add(Helper.createTuple(1, 1, 3));

		mRelation.add(Helper.createTuple(1, 2, 1));
		mRelation.add(Helper.createTuple(2, 2, 2));

		ITuple viewCriteria = Helper.createTuple("X", "Y", "Z");

		mView = new View(mRelation, viewCriteria, new SimpleRelationFactory());

		mIndex = new SimpleIndex(mView, 0, 1);
	}

	private static List<ITerm> makeKey(Object... objects) {
		List<ITerm> key = new ArrayList<ITerm>(objects.length);

		for (Object o : objects) {
			ITerm term = Helper.createTerm(o);
			key.add(term);
		}

		return key;
	}

	public void testGet() {
		ITuple foreignTuple = Helper.createTuple(3, 2, 1, 1);

		List<ITuple> matchingTuples = mIndex.get(Utils.makeKey(foreignTuple,
				new int[] { 2, 3 }));

		assertNotNull(matchingTuples);
		assertEquals(3, matchingTuples.size());

		matchingTuples = mIndex.get(makeKey(1, 2));

		assertNotNull(matchingTuples);
		assertEquals(1, matchingTuples.size());

		matchingTuples = mIndex.get(makeKey(2, 1));

		assertEquals(0, matchingTuples.size());
	}
}
