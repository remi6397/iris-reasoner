package org.deri.iris.rdb.storage;

import java.sql.SQLException;

import junit.framework.Assert;

import org.junit.Test;

public class RdbRelationTest extends AbstractRdbRelationTest {

	@Test
	public void testAdd() {
		Assert.assertTrue(relation1.add(tuple1));
		Assert.assertTrue(relation1.add(tuple2));
		Assert.assertFalse(relation1.add(tuple1));
		Assert.assertFalse(relation1.add(tuple2));
	}

	@Test
	public void testAddAll() {
		relation1.add(tuple1);
		relation1.add(tuple2);
		relation2.add(tuple1);
		relation2.add(tuple3);

		Assert.assertTrue(relation1.addAll(relation2));
		Assert.assertEquals(3, relation1.size());
	}

	@Test
	public void testContains() {
		Assert.assertFalse(relation1.contains(tuple1));

		Assert.assertTrue(relation1.add(tuple1));
		Assert.assertTrue(relation1.add(tuple2));

		Assert.assertTrue(relation1.contains(tuple1));
		Assert.assertTrue(relation1.contains(tuple2));

		Assert.assertFalse(relation1.add(tuple1));
	}

	@Test
	public void testCreateInternalName() throws SQLException {
		RdbRelation otherRelation = new RdbRelation(connection, "$foobar", 2);
		Assert.assertTrue(otherRelation.add(tuple1));
		otherRelation.drop();
	}

	@Test
	public void testCreateUrlName() throws SQLException {
		RdbRelation otherRelation = new RdbRelation(connection,
				"http://foo.com", 2);
		Assert.assertTrue(otherRelation.add(tuple1));
		otherRelation.drop();
	}

}
