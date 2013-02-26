package at.sti2.streamingiris.api.querycontainment;

import java.util.List;

import at.sti2.streamingiris.api.basics.IQuery;
import at.sti2.streamingiris.api.terms.IVariable;
import at.sti2.streamingiris.storage.IRelation;

/**
 * <p>
 * This interface defines a query containment check, i.e. a check for whether
 * one query is contained within another query. The query containment is checked
 * using the 'Frozen Facts' algorithm (This algorithm is presented in
 * Ramakrishnan, R., Y. Sagiv, J. D. Ullman and M. Y. Vardi (1989). Proof-Tree
 * Transformation Theorems and their Applications. 8th ACM Symposium on
 * Principles of Database Systems, pp. 172 - 181, Philadelphia).
 * </p>
 * <p>
 * The query containment check can only be performed over positive queries that
 * do not contain built-ins and disjunctions.
 * </p>
 * <p>
 * Example: <br />
 * In the following Query1 is contained within Query2:<br />
 * Program: vehicle(?x) :- car(?x).<br />
 * Query1: car(?x).<br />
 * Query2: vehicle(?x).<br />
 * </p>
 * <p>
 * An implementation of this interface is supposed to get an implementation of
 * the IProgram as input parameter and to produce a boolean value as output of
 * the query containment check.
 * </p>
 * 
 * @author Nathalie Steinmetz, DERI Innsbruck
 */
public interface IQueryContainment {

	/**
	 * <p>
	 * Checks whether query1 is contained within query2.
	 * </p>
	 * 
	 * @param query1
	 *            The query for which to check whether it is contained within
	 *            query2.
	 * @param query2
	 *            The query for which to check whether it contains query1.
	 * @return True if query1 is contained within query2, otherwise false.
	 */
	public boolean checkQueryContainment(IQuery query1, IQuery query2)
			throws Exception;

	/**
	 * <p>
	 * Return the containment mappings that result from the containment check.
	 * </p>
	 * 
	 * @return a set of substitutions resulting from the query containment
	 *         check.
	 */
	public IRelation getContainmentMappings();

	/**
	 * Return the variables from the query in 'execution' order.
	 * 
	 * @return The variables in order.
	 */
	public List<IVariable> getVariableBindings();
}
