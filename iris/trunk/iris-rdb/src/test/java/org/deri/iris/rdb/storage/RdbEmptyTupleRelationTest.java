package org.deri.iris.rdb.storage;

import java.io.IOException;
import java.sql.SQLException;

import junit.framework.Assert;

import org.deri.iris.factory.Factory;
import org.junit.After;
import org.junit.Test;

public class RdbEmptyTupleRelationTest extends AbstractRdbRelationTest {

	private RdbEmptyTupleRelation emptyTupleRelation;

	@After
	public void shutDown() throws SQLException, IOException {
		if (emptyTupleRelation != null) {
			emptyTupleRelation.drop();
		}

		super.shutDown();
	}

	@Test
	public void testAdd() throws SQLException {
		emptyTupleRelation = new RdbEmptyTupleRelation(connection, 5);

		Assert.assertTrue(emptyTupleRelation.add(Factory.BASIC.createTuple()));
		Assert.assertEquals(6, emptyTupleRelation.size());
	}

}
