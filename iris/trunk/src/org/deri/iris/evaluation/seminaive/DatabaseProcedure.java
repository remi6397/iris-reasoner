/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions by 
 * built-in predicates, default negation (under well-founded semantics), 
 * function symbols and contexts. 
 * 
 * Copyright (C) 2006  Digital Enterprise Research Institute (DERI), 
 * Leopold-Franzens-Universitaet Innsbruck, Technikerstrasse 21a, 
 * A-6020 Innsbruck. Austria.
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
package org.deri.iris.evaluation.seminaive;

import java.util.Map;

import org.deri.iris.api.IEDB;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.evaluation.seminaive.IEvaluationProcedure;
import org.deri.iris.api.evaluation.seminaive.model.ITree;
import org.deri.iris.api.storage.IRelation;

/**
 * TODO. To implement
 * 
 * @author Paco Garcia, University of Murcia
 * @date 08-sep-2006
 */
public class DatabaseProcedure implements IEvaluationProcedure {

	/**
	 * @param pi
	 *            Rule to evaluate
	 * @param EDB
	 *            Extensional Database tuples
	 * @param Q
	 *            Tuples already discovered
	 * @return new tuples discovered for the rule evaluated
	 */
	public IRelation eval(ITree pi, IEDB EDB) {
		// R1,..., Rk & pi = body;
		// Q1,...,Qm; = Q

		// TODO Get the results
		// Pbody.addAll(evaluator.evaluate());
		return null;
	}

	/**
	 * 
	 * @param t
	 *            Rule to evaluate
	 * @param EDB
	 *            Extensional Database tuples
	 * @param P
	 *            All the tuples discovered so far
	 * @param AQ
	 *            Tuples discovered during the last iteration
	 * @return new tuples discovered for the rule evaluated
	 */
	public IRelation eval_incr(ITree pi, IEDB EDB, Map<IPredicate, IRelation> AQ) {

		// TODO Set parameters
		// R1,..., Rk & pi = body;
		// Q1,...,Qm; = Q
		// TODO Get the results
		// Pbody.addAll(evaluator.evaluate());
		return null;
	}
}