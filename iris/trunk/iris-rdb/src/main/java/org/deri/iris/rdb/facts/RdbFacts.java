package org.deri.iris.rdb.facts;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.factory.Factory;
import org.deri.iris.facts.FiniteUniverseFacts;
import org.deri.iris.facts.IFacts;
import org.deri.iris.rdb.storage.IRdbRelation;
import org.deri.iris.rdb.storage.RdbEqualityRelation;
import org.deri.iris.rdb.storage.RdbRelation;
import org.deri.iris.rdb.storage.RdbTrueRelation;
import org.deri.iris.rdb.storage.RdbUniverseView;
import org.deri.iris.rules.RuleHeadEqualityRewriter;
import org.deri.iris.storage.IRelation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Facts that are stored in a relational database. A relation corresponds to a
 * table in a database, and is created according to the guidelines defined in
 * {@link IRdbRelation}.
 * </p>
 * <p>
 * A prefix can be specified, which is used as a prefix for the name of the
 * table representing a relation.
 * </p>
 */
public class RdbFacts implements IRdbFacts {

	private static final Logger logger = LoggerFactory
			.getLogger(RdbFacts.class);

	public static IPredicate TRUE_PREDICATE = Factory.BASIC.createPredicate(
			"true", 0);

	private final String prefix;

	private Map<IPredicate, IRdbRelation> relations;

	private final Connection connection;

	public RdbFacts(Connection connection) {
		this(connection, "");
	}

	public RdbFacts(Connection connection, String prefix) {
		this.connection = connection;
		this.prefix = prefix;
		this.relations = new HashMap<IPredicate, IRdbRelation>();
	}

	@Override
	public Set<IPredicate> getPredicates() {
		return relations.keySet();
	}

	@Override
	public IRdbRelation get(IPredicate predicate) {
		IRdbRelation relation = relations.get(predicate);

		if (relation == null) {
			try {
				if (predicate.equals(FiniteUniverseFacts.UNIVERSE)) {
					relation = RdbUniverseView.getInstance(connection);
				} else if (predicate.equals(TRUE_PREDICATE)) {
					relation = RdbTrueRelation.getInstance(connection);
				} else if (predicate.equals(RuleHeadEqualityRewriter.PREDICATE)) {
					relation = RdbEqualityRelation.getInstance(connection);
				} else {
					relation = new RdbRelation(connection, predicate, prefix);
				}

				relations.put(predicate, relation);
			} catch (SQLException e) {
				logger.error("Failed to create RDB relation for " + predicate,
						e);
			}
		}

		// Returns null, if no relation could be created.
		return relation;
	}

	@Override
	public void addAll(IFacts source) {
		for (IPredicate predicate : source.getPredicates()) {
			IRdbRelation targetRelation = get(predicate);
			IRelation sourceRelation = source.get(predicate);

			if (targetRelation != null && sourceRelation != null) {
				targetRelation.addAll(sourceRelation);
			}
		}
	}

	@Override
	public void dropAll() {
		for (IPredicate predicate : getPredicates()) {
			IRdbRelation relation = get(predicate);

			if (relation != null) {
				relation.drop();
			}
		}

		relations.clear();
	}

}
