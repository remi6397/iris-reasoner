/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions.
 * 
 * Copyright (C) 2008 Semantic Technology Institute (STI) Innsbruck, 
 * University of Innsbruck, Technikerstrasse 21a, 6020 Innsbruck, Austria.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 */
package org.deri.iris.api;

import java.util.List;
import java.util.Map;

import org.deri.iris.EvaluationException;
import org.deri.iris.ProgramNotStratifiedException;
import org.deri.iris.RuleUnsafeException;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.storage.IRelation;

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
	 * periodically executed and the results get published.
	 * 
	 * @param query
	 *            The query to evaluate.
	 * @throws ProgramNotStratifiedException
	 *             If the program (knowledge-base)can not be stratified
	 * @throws RuleUnsafeException
	 *             If the program (knowledge-base) contains an unsafe rule.
	 * @throws EvaluationException
	 *             If the execution of a query fails for any other reason.
	 */
	void registerQuery(IQuery query) throws ProgramNotStratifiedException,
			RuleUnsafeException, EvaluationException;

	/**
	 * Deregister a query at this knowledge-base.
	 * 
	 * @param query
	 *            The query to evaluate.
	 */
	void deregisterQuery(IQuery query);

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
	 * Adds a listener (socket) to the knowledge base who will receive the
	 * results.
	 * 
	 * @param address
	 *            The address of the socket.
	 * @param port
	 *            The port of the socket.
	 */
	void addListener(String address, int port);

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

	/**
	 * Deletes a listener (socket) from the knowledge base.
	 * 
	 * @param host
	 *            The address of the socket.
	 * @param port
	 *            The port of the socket.
	 */
	void deleteListener(String host, int port);
}