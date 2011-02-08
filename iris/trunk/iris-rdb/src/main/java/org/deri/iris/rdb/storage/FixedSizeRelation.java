package org.deri.iris.rdb.storage;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.factory.Factory;
import org.deri.iris.storage.IRelation;

/**
 * A relation that contains a specified number of empty tuples. This relation
 * returns duplicate tuples.
 */
public class FixedSizeRelation implements IRelation {

	private static final ITuple EMPTY_TUPLE = Factory.BASIC.createTuple();

	private int size;

	public FixedSizeRelation(int size) {
		this.size = size;
	}

	@Override
	public boolean add(ITuple tuple) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(IRelation relation) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public ITuple get(int index) {
		if (index < size) {
			// Always return an empty tuple.
			return EMPTY_TUPLE;
		}

		return null;
	}

	@Override
	public boolean contains(ITuple tuple) {
		if (tuple.size() == 0) {
			return true;
		}

		return false;
	}

	@Override
	public String toString() {
		return RdbUtils.toString(this);
	}

}
