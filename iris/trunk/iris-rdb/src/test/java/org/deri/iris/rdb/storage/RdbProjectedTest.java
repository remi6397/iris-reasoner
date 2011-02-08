package org.deri.iris.rdb.storage;

import java.io.IOException;
import java.sql.SQLException;

import junit.framework.Assert;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.factory.Factory;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class RdbProjectedTest extends AbstractRdbRelationTest {

	private static IVariable variable1;

	private static IVariable variable2;

	private static ITuple viewCriteria;

	private static ITuple inputTuple;

	private RdbProjectedRelation projectedRelation;

	@After
	public void shutDown() throws SQLException, IOException {
		if (projectedRelation != null) {
			projectedRelation.drop();
		}

		super.shutDown();
	}

	@BeforeClass
	public static void beforeClass() {
		AbstractRdbRelationTest.beforeClass();

		variable1 = Factory.TERM.createVariable("X");
		variable2 = Factory.TERM.createVariable("Y");

		viewCriteria = Factory.BASIC.createTuple(stringTerm, variable1);
		inputTuple = Factory.BASIC.createTuple(variable2, variable1);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testAdd() throws SQLException {
		IRdbRelation relation = relation1;

		projectedRelation = new RdbProjectedRelation(connection, relation,
				viewCriteria, inputTuple);

		projectedRelation.add(tuple1);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testAddAll() throws SQLException {
		IRdbRelation relation = relation1;

		projectedRelation = new RdbProjectedRelation(connection, relation,
				viewCriteria, inputTuple);

		projectedRelation.addAll(relation2);
	}

	@Test
	public void testSizeAndContains() throws SQLException {
		IRdbRelation relation = relation1;

		relation.add(tuple1);
		relation.add(tuple2);
		relation.add(tuple3);

		projectedRelation = new RdbProjectedRelation(connection, relation,
				viewCriteria, inputTuple);

		// Should only return distinct tuples.
		Assert.assertEquals(2, projectedRelation.size());
		Assert.assertTrue(projectedRelation.contains(tuple1));
		Assert.assertTrue(projectedRelation.contains(tuple3));
	}
	

	@Test
	public void testGetArity() throws SQLException {
		IRdbRelation relation = relation1;

		relation.add(tuple1);

		Assert.assertEquals(2, relation.getArity());
	}

	@Test
	public void testGet() throws SQLException {
		IRdbRelation relation = relation1;

		relation.add(tuple1);
		relation.add(tuple2);

		ITuple inputTuple = Factory.BASIC.createTuple(stringTerm, variable1);
		ITuple viewCriteria = Factory.BASIC.createTuple(stringTerm, variable1);

		// Since we filter on the relation, we need to create a view on it.
		IRdbRelation view = new RdbView(connection, relation, inputTuple);

		projectedRelation = new RdbProjectedRelation(connection, view,
				viewCriteria, inputTuple);

		Assert.assertEquals(1, projectedRelation.size());
		Assert.assertEquals(tuple1, projectedRelation.get(0));
	}

}
