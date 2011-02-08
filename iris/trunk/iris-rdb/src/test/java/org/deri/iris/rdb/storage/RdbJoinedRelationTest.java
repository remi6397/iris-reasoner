package org.deri.iris.rdb.storage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.factory.Factory;
import org.junit.After;
import org.junit.Test;

public class RdbJoinedRelationTest extends AbstractRdbRelationTest {

	private RdbJoinedRelation joinedRelation;

	@After
	public void shutDown() throws SQLException, IOException {
		if (joinedRelation != null) {
			joinedRelation.drop();
		}

		super.shutDown();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testAdd() throws SQLException {
		IRdbRelation leftRelation = relation1;
		IRdbRelation rightRelation = relation2;

		joinedRelation = new RdbJoinedRelation(connection, leftRelation,
				rightRelation);

		joinedRelation.add(tuple1);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testAddAll() throws SQLException {
		IRdbRelation leftRelation = relation1;
		IRdbRelation rightRelation = relation2;

		joinedRelation = new RdbJoinedRelation(connection, leftRelation,
				rightRelation);

		joinedRelation.addAll(leftRelation);
	}

	@Test
	public void testGetAndContains() throws SQLException {
		IRdbRelation leftRelation = relation1;
		IRdbRelation rightRelation = relation2;

		leftRelation.add(tuple1);
		// [('string', 1337),]

		rightRelation.add(tuple2);
		// [(1337, 'string')]

		List<Integer> joinIndices = new ArrayList<Integer>();

		// Join on attr2 and attr3 of product relation.
		joinIndices.add(1);
		joinIndices.add(2);

		List<List<Integer>> indices = new ArrayList<List<Integer>>();
		indices.add(joinIndices);

		joinedRelation = new RdbJoinedRelation(connection, leftRelation,
				rightRelation, indices);
		// [('string', 1337, 1337, 'string')]

		Assert.assertEquals(1, joinedRelation.size());

		List<ITerm> terms = new ArrayList<ITerm>();
		terms.addAll(tuple1);
		terms.addAll(tuple2);

		ITuple resultTuple = Factory.BASIC.createTuple(terms);
		Assert.assertEquals(resultTuple, joinedRelation.get(0));
		Assert.assertTrue(joinedRelation.contains(resultTuple));
	}

	@Test
	public void testJoin() throws SQLException {
		IRdbRelation leftRelation = relation1;
		IRdbRelation rightRelation = relation2;

		leftRelation.add(tuple1);
		leftRelation.add(tuple2);
		// [('string', 1337), (1337, 'string)]

		rightRelation.add(tuple2);
		rightRelation.add(tuple3);
		// [(1337, 'string'), ('string', 'string')]

		List<Integer> joinIndices = new ArrayList<Integer>();

		// Join on attr2 and attr3 of product relation.
		joinIndices.add(1);
		joinIndices.add(2);

		List<List<Integer>> indices = new ArrayList<List<Integer>>();
		indices.add(joinIndices);

		joinedRelation = new RdbJoinedRelation(connection, leftRelation,
				rightRelation, indices);
		// [('string', 1337, 1337, 'string'),
		// (1337, 'string', 'string', 'string')]

		// Test size of result relation.
		Assert.assertEquals(2, joinedRelation.size());
		
		// Test arity of result relation.
		Assert.assertEquals(4, joinedRelation.getArity());
	}

	@Test
	public void testCartessianProduct() throws SQLException {
		IRdbRelation leftRelation = relation1;
		IRdbRelation rightRelation = relation2;

		leftRelation.add(tuple1);
		leftRelation.add(tuple2);
		// [('string', 1337), (1337, 'string)]

		rightRelation.add(tuple1);
		rightRelation.add(tuple2);
		// [('string', 1337), (1337, 'string)]

		joinedRelation = new RdbJoinedRelation(connection, leftRelation,
				rightRelation);
		// [('string', 1337, 'string', 1337), ('string', 1337, 1337, 'string'),
		// (1337, 'string', 'string', 1337), (1337, 'string', 1337, 'string')]

		Assert.assertEquals(4, joinedRelation.size());
	}

}
