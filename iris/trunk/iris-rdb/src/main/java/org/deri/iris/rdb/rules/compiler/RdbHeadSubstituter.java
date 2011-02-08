package org.deri.iris.rdb.rules.compiler;

import java.sql.Connection;
import java.sql.SQLException;

import org.deri.iris.EvaluationException;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.facts.IFacts;
import org.deri.iris.rdb.storage.IRdbRelation;
import org.deri.iris.rdb.storage.RdbProjectedRelation;
import org.deri.iris.rdb.storage.RdbTempRelation;

public class RdbHeadSubstituter extends RdbRuleElement {

	private final Connection connection;

	private final ITuple outputTuple;

	private ITuple headTuple;

	public RdbHeadSubstituter(Connection connection, ITuple outputTuple,
			ITuple headTuple) {
		this.connection = connection;
		this.outputTuple = outputTuple;
		this.headTuple = headTuple;
	}

	@Override
	public IRdbRelation process(IRdbRelation input) throws EvaluationException {
		if (input.getArity() == 0 && input.size() > 0) {
			if (!headTuple.isGround()) {
				throw new EvaluationException("Found non-ground tuple in the "
						+ "head of a rule with empty body");
			}

			try {
				IRdbRelation temp = new RdbTempRelation(connection,
						headTuple.size());
				temp.add(headTuple);

				return temp;
			} catch (SQLException e) {
				throw new EvaluationException(e.getMessage());
			}
		}

		try {
			return new RdbProjectedRelation(connection, input, headTuple,
					outputTuple);
		} catch (SQLException e) {
			throw new EvaluationException(e.getMessage());
		}
	}

	@Override
	public ITuple getOutputTuple() {
		return headTuple;
	}

	@Override
	public RdbRuleElement getDeltaSubstitution(IFacts deltas) {
		return null;
	}

	@Override
	public void dispose() {
		// Nothing to do.
	}

}
