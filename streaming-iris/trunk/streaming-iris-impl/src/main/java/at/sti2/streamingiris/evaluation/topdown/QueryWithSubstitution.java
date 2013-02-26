package at.sti2.streamingiris.evaluation.topdown;

import java.util.Map;

import at.sti2.streamingiris.api.basics.IQuery;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.IVariable;

/**
 * A query with an associated substitution form a branch/subgoal in a top-down
 * evaluation tree.
 * 
 * @author gigi
 * 
 */
public class QueryWithSubstitution {

	private IQuery query;
	private Map<IVariable, ITerm> substitution;

	public QueryWithSubstitution() {
	}

	public QueryWithSubstitution(IQuery query,
			Map<IVariable, ITerm> substitution) {
		super();
		this.query = query;
		this.substitution = substitution;
	}

	public IQuery getQuery() {
		return query;
	}

	public Map<IVariable, ITerm> getSubstitution() {
		return substitution;
	}

	@Override
	public String toString() {
		return query.toString() + " | " + substitution.toString();
	}

}
