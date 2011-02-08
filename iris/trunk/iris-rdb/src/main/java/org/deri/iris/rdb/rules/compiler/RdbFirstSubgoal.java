package org.deri.iris.rdb.rules.compiler;

import java.sql.Connection;
import java.sql.SQLException;

import org.deri.iris.EvaluationException;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.facts.IFacts;
import org.deri.iris.rdb.storage.IRdbRelation;
import org.deri.iris.rdb.storage.RdbView;
import org.deri.iris.storage.IRelation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RdbFirstSubgoal extends RdbRuleElement {

	private static Logger logger = LoggerFactory
			.getLogger(RdbFirstSubgoal.class);

	private final Connection connection;

	private final IPredicate predicate;

	private final ITuple viewCriteria;

	private IRdbRelation view;

	public RdbFirstSubgoal(Connection connection, IPredicate predicate,
			IRdbRelation relation, ITuple viewCriteria) throws SQLException {
		this.connection = connection;
		this.predicate = predicate;
		this.viewCriteria = viewCriteria;

		if (relation.getArity() == 0) {
			view = relation;
		} else {
			view = new RdbView(connection, relation, viewCriteria,
					String.valueOf(hashCode()));
		}
	}

	@Override
	public IRdbRelation process(IRdbRelation input) throws EvaluationException {
		return view;
	}

	@Override
	public ITuple getOutputTuple() {
		return viewCriteria;
	}

	@Override
	public RdbRuleElement getDeltaSubstitution(IFacts deltas) {
		IRelation relation = deltas.get(predicate);

		if (relation instanceof IRdbRelation) {
			try {
				return new RdbFirstSubgoal(connection, predicate,
						(IRdbRelation) relation, viewCriteria);
			} catch (SQLException e) {
				logger.error("Failed to create delta substitution for " + this);
			}
		}

		return null;
	}

	@Override
	public void dispose() {
		// Only close the view created by the constructor.
		if (view != null && view.getArity() != 0) {
			view.close();
		}
	}
}
