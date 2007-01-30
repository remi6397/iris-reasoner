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
package org.deri.iris;

import java.util.HashMap;
import java.util.Map;

import org.deri.iris.api.IExecutor;
import org.deri.iris.api.IProgram;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.evaluation.IBottomUpEvaluator;
import org.deri.iris.api.evaluation.algebra.IExpressionEvaluator;
import org.deri.iris.api.storage.IRelation;
import org.deri.iris.evaluation.common.AdornedProgram;
import org.deri.iris.evaluation.magic.MagicSetImpl;
import org.deri.iris.evaluation.seminaive.SeminaiveEvaluation;

/**
 * <p>
 * Executes a programm.
 * </p>
 * <p>
 * $Id: Executor.java,v 1.7 2007-01-30 19:35:52 darko Exp $
 * </p>
 * 
 * @author Richard Pöttler
 * @author Darko Anicic, DERI Innsbruck
 * @version $Revision: 1.7 $
 */
public class Executor implements IExecutor {

	/** The program to execute. */
	private IProgram prog;

	/** The bottom-up evaluator. */
	private IBottomUpEvaluator evaluator;

	/** The evaluation mehtod. */
	private IExpressionEvaluator method;

	/**
	 * <p>
	 * Creates a new evaluator with a given programm and evaluator.
	 * </p>
	 * 
	 * @param p
	 *            The program
	 * @throws NullPointerException
	 *             If the program or the evaluator is {@code null}
	 */
	public Executor(final IProgram p, final IExpressionEvaluator m) {
		if (p == null || m == null) {
			throw new NullPointerException(
					"The program and the expression evaluator must not be null");
		}
		this.prog = p;
		this.method = m;
	}

	public IRelation computeSubstitution(final IQuery q) {
		if (q == null) {
			throw new NullPointerException("The query must not be null");
		}
		if (q.getQueryLenght() != 1) {
			throw new IllegalArgumentException(
					"The length of the query literals must be 1");
		}
		
		System.out.println("query: " + q);

		// applying the magic sets
		final AdornedProgram ap = new AdornedProgram(prog.getRules(), q);
		
		final MagicSetImpl ms = new MagicSetImpl(ap);
		// Tests the stratum of the newly constructed program. If not strtified
		// re-set the program to the original one.
		IProgram p = ms.createProgram(prog);
		if (!p.isStratified()) {
			p = prog;
		}

		// compute the fixed point for this magic program
		evaluator = new SeminaiveEvaluation(method, p);
		evaluator.evaluate();
		evaluator.runQuery(q);

		return this.evaluator.getResultSet().getResults().get(
				q.getQueryLiteral(0).getPredicate());
	}

	public Map<IPredicate, IRelation> computeSubstitutions() {
		final Map<IPredicate, IRelation> res = new HashMap<IPredicate, IRelation>();
		for (final IQuery q : prog.getQueries()) {
			res
					.put(q.getQueryLiteral(0).getPredicate(),
							computeSubstitution(q));
		}
		return res;
	}

	public boolean execute() {
		// somehow there's nothing to do.
		return true;
	}
}
