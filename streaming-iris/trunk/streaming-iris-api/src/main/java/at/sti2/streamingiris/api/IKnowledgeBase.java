package at.sti2.streamingiris.api;

import java.util.List;
import java.util.Map;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.ProgramNotStratifiedException;
import at.sti2.streamingiris.RuleUnsafeException;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.basics.IQuery;
import at.sti2.streamingiris.api.basics.IRule;
import at.sti2.streamingiris.api.terms.IVariable;
import at.sti2.streamingiris.storage.IRelation;

/**
 * The interface of a knowledge-base as seen by a user of IRIS.
 */
public interface IKnowledgeBase {

	/**
	 * Executes all queries against the knowledge base and publishes the results
	 * to all registered listeners.
	 */
	void execute();

	/**
	 * Execute a query over this knowledge-base. The results are not published.
	 * 
	 * @param query
	 *            The query to evaluate.
	 * @return The relation of results.
	 * @throws ProgramNotStratifiedException
	 *             If the program (knowledge-base)can not be stratified
	 * @throws RuleUnsafeException
	 *             If the program (knowledge-base) contains an unsafe rule.
	 * @throws EvaluationException
	 *             If the execution of a query fails for any other reason.
	 */
	IRelation execute(IQuery query) throws ProgramNotStratifiedException,
			RuleUnsafeException, EvaluationException;

	/**
	 * Evaluate a query and optionally return the variable bindings. The results
	 * are not published.
	 * 
	 * @param query
	 *            The query to evaluate.
	 * @param outputVariables
	 *            If this is not null, it will be filled with the variable
	 *            bindings of the result relation, i.e. there will be one
	 *            variable instance for each term (in one row) of the results
	 *            set
	 * @return The relation of results.
	 * @throws ProgramNotStratifiedException
	 *             If the program (knowledge-base)can not be stratified
	 * @throws RuleUnsafeException
	 *             If the program (knowledge-base) contains an unsafe rule.
	 * @throws EvaluationException
	 *             If the execution of a query fails for any other reason.
	 */
	IRelation execute(IQuery query, List<IVariable> variableBindings)
			throws ProgramNotStratifiedException, RuleUnsafeException,
			EvaluationException;

	/**
	 * Register a query at this knowledge-base. This query will then be
	 * periodically executed and the results get streamed to the socket with
	 * this host and port.
	 * 
	 * @param query
	 *            The query to evaluate.
	 * @param host
	 *            The host of the socket that listens to this query.
	 * @param port
	 *            The port of the socket that listens to this query.
	 * @throws ProgramNotStratifiedException
	 *             If the program (knowledge-base)can not be stratified
	 * @throws RuleUnsafeException
	 *             If the program (knowledge-base) contains an unsafe rule.
	 * @throws EvaluationException
	 *             If the execution of a query fails for any other reason.
	 */
	void registerQueryListener(IQuery query, String host, int port)
			throws ProgramNotStratifiedException, RuleUnsafeException,
			EvaluationException;

	/**
	 * Deregister a query at this knowledge-base for the given socket defined by
	 * host and port.
	 * 
	 * @param query
	 *            The query to evaluate.
	 * @param host
	 *            The host of the socket.
	 * @param port
	 *            The port of the socket.
	 */
	void deregisterQueryListener(IQuery query, String host, int port);

	/**
	 * Get the rules hidden within the knowledge-base.
	 * 
	 * @return The unmodifiable list of rules.
	 */
	List<IRule> getRules();

	/**
	 * This method adds facts to the knowledge base during IRIS is running.
	 * 
	 * @param newFacts
	 *            The new facts to be added
	 * @throws EvaluationException
	 */
	void addFacts(Map<IPredicate, IRelation> newFacts)
			throws EvaluationException;

	/**
	 * Shuts down the Knowledge Base and terminates all corresponding threads.
	 */
	void shutdown();

	/**
	 * Deletes obsolete facts from the knowledge base.
	 * 
	 * @throws EvaluationException
	 */
	void cleanKnowledgeBase() throws EvaluationException;
}