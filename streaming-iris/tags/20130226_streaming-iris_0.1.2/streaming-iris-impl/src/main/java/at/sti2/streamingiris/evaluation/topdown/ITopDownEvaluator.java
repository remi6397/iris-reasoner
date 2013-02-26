package at.sti2.streamingiris.evaluation.topdown;

import java.util.List;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.IQuery;
import at.sti2.streamingiris.api.terms.IVariable;
import at.sti2.streamingiris.storage.IRelation;

/**
 * Interface for top-down evaluators
 * 
 * @author gigi
 */
public interface ITopDownEvaluator {

	/**
	 * Evaluate a query
	 * 
	 * @param query
	 *            a query
	 * @return relation containing all resolved tuples.
	 * @throws EvaluationException
	 *             thrown on error
	 */
	public IRelation evaluate(IQuery query) throws EvaluationException;

	/**
	 * Returns a list of output variables, i.e. the variables of the initial
	 * query.
	 * 
	 * @return list of output variables
	 */
	public List<IVariable> getOutputVariables();

}
