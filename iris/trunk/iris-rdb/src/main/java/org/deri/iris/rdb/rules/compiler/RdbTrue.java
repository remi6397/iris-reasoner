package org.deri.iris.rdb.rules.compiler;

import java.sql.Connection;
import java.sql.SQLException;

import org.deri.iris.EvaluationException;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.factory.Factory;
import org.deri.iris.facts.IFacts;
import org.deri.iris.rdb.storage.IRdbRelation;
import org.deri.iris.rdb.storage.RdbEmptyTupleRelation;

public class RdbTrue extends RdbRuleElement {

	private static final String TABLE_NAME = "___true___";

	private IRdbRelation view;

	public RdbTrue(Connection connection) throws SQLException {
		view = new RdbEmptyTupleRelation(connection, TABLE_NAME, 1);
	}

	@Override
	public IRdbRelation process(IRdbRelation input) throws EvaluationException {
		return view;
	}

	@Override
	public ITuple getOutputTuple() {
		return Factory.BASIC.createTuple();
	}

	@Override
	public RdbRuleElement getDeltaSubstitution(IFacts deltas) {
		return this;
	}

	@Override
	public void dispose() {
		if (view != null) {
			view.close();
		}
	}
}
