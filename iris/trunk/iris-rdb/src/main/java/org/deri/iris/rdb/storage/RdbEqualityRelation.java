package org.deri.iris.rdb.storage;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.storage.IRelation;

/**
 * A relation that stores equivalent terms. This relation does not return
 * duplicate tuples.
 */
public class RdbEqualityRelation extends AbstractRdbRelation {

	private static final String TABLE_NAME = "__equals__";

	private static Map<Connection, RdbEqualityRelation> relations;

	private RdbRelation relation;

	static {
		relations = new HashMap<Connection, RdbEqualityRelation>();
	}

	private RdbEqualityRelation(Connection connection) throws SQLException {
		super(connection);

		this.relation = new RdbRelation(connection, TABLE_NAME, 2);
	}

	/**
	 * Returns a singleton instance for the specified connection. The instances
	 * are kept in a weak hash map, where the connection is the key and the
	 * relation is the value. When this method is called for two different
	 * connections, two different instances of this relation are returned.
	 * 
	 * @param connection
	 *            The connection to return the singleton instance for.
	 * @return A singleton instance of this relation for the specified
	 *         connection.
	 * @throws SQLException
	 *             If the relation can not be created.
	 */
	public static RdbEqualityRelation getInstance(Connection connection)
			throws SQLException {
		RdbEqualityRelation relation = relations.get(connection);

		if (relation == null) {
			relation = new RdbEqualityRelation(connection);
			relations.put(connection, relation);
		}

		return relation;
	}

	@Override
	public int getArity() {
		return relation.getArity();
	}

	@Override
	public String getTableName() {
		return RdbUtils.quoteIdentifier(TABLE_NAME);
	}

	@Override
	public void drop() {
		// Do nothing.
	}

	@Override
	public boolean add(ITuple tuple) {
		return relation.add(tuple);
	}

	@Override
	public boolean addAll(IRelation relation) {
		return this.relation.addAll(relation);
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
