package org.deri.iris.rdb.storage;

import java.sql.Connection;
import java.sql.SQLException;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.storage.IRelation;

/**
 * This relation does not return duplicate tuples.
 */
public class RdbTempRelation extends AbstractRdbRelation {
	
	private static final String PREFIX = "temp_";

	private final int arity;

	private String tableName;

	private RdbRelation relation;

	public RdbTempRelation(Connection connection, int arity)
			throws SQLException {
		super(connection);
		this.arity = arity;

		// UUID uuid = UUID.randomUUID();
		// this.tableName = uuid.toString();
		this.tableName = PREFIX + hashCode();

		this.relation = new RdbRelation(connection, tableName, arity);
	}

	@Override
	public int getArity() {
		return arity;
	}

	@Override
	public String getTableName() {
		return RdbUtils.quoteIdentifier(tableName);
	}

	@Override
	public void drop() {
		close();
		relation.drop();
	}

	@Override
	public boolean add(ITuple tuple) {
		return relation.add(tuple);
	}

	@Override
	public boolean addAll(IRelation relation) {
		return relation.addAll(relation);
	}

	@Override
	public int size() {
		return relation.size();
	}

	@Override
	public ITuple get(int index) {
		return relation.get(index);
	}

	@Override
	public boolean contains(ITuple tuple) {
		return relation.contains(tuple);
	}

	@Override
	public void close() {
		relation.close();
	}

	@Override
	public String toString() {
		return relation.toString();
	}

	@Override
	public CloseableIterator<ITuple> iterator() {
		return relation.iterator();
	}

}
