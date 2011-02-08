package org.deri.iris.rdb.storage;

import java.io.IOException;
import java.sql.SQLException;

import junit.framework.Assert;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.factory.Factory;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class RdbViewTest extends AbstractRdbRelationTest {

	private static IVariable variable;

	private static ITuple viewCriteria;

	private RdbView view;

	@Before
	public void setUp() throws IOException, ClassNotFoundException,
			SQLException {
		super.setUp();

		view = new RdbView(connection, relation1, viewCriteria);
	}

	@After
	public void shutDown() throws SQLException, IOException {
		view.drop();

		super.shutDown();
	}

	@BeforeClass
	public static void beforeClass() {
		AbstractRdbRelationTest.beforeClass();

		variable = Factory.TERM.createVariable("X");

		viewCriteria = Factory.BASIC.createTuple(stringTerm, variable);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testAdd() throws SQLException {
		view.add(tuple1);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testAddAll() throws SQLException {
		view.addAll(relation1);
	}

	@Test
	public void testSizeAndContains() {
		relation1.add(tuple1);
		relation1.add(tuple2);
		relation1.add(tuple3);

		Assert.assertEquals(2, view.size());
		Assert.assertTrue(view.contains(tuple1));
		Assert.assertTrue(view.contains(tuple3));
	}

	@Test
	public void testGet() {
		relation1.add(tuple1);

		Assert.assertEquals(1, view.size());
		Assert.assertEquals(tuple1, view.get(0));
	}
	
	@Test
	public void testGetArity() {
		relation1.add(tuple1);

		Assert.assertEquals(2, view.getArity());
	}

}
