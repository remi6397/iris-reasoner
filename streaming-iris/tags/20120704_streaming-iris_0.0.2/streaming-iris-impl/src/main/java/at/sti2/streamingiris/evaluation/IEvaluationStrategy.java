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
package at.sti2.streamingiris.evaluation;

import java.util.List;


import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.ProgramNotStratifiedException;
import at.sti2.streamingiris.RuleUnsafeException;
import at.sti2.streamingiris.api.basics.IQuery;
import at.sti2.streamingiris.api.terms.IVariable;
import at.sti2.streamingiris.facts.IFacts;
import at.sti2.streamingiris.storage.IRelation;

/**
 * Interface for all evaluation strategy implementations.
 */
public interface IEvaluationStrategy {
	/**
	 * Evaluate a query and optionally return the variable bindings.
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
	 *             If the evaluation fails for any other reason.
	 */
	IRelation evaluateQuery(IQuery query, List<IVariable> outputVariables)
			throws ProgramNotStratifiedException, RuleUnsafeException,
			EvaluationException;

	/**
	 * Evaluates all rules against the currently available facts.
	 * 
	 * @param facts
	 *            New facts to be added.
	 * @param timestamp
	 *            The timestamp when the facts become obsolete.
	 * @throws EvaluationException
	 *             If an evaluation exception occurs.
	 */
	void evaluateRules(IFacts facts, long timestamp) throws EvaluationException;
}
