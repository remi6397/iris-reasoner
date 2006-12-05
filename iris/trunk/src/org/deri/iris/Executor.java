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

import java.util.Map;
import java.util.Set;

import org.deri.iris.api.IProgram;
import org.deri.iris.api.IExecutor;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.evaluation.IEvaluator;
import org.deri.iris.api.evaluation.seminaive.IEvaluationProcedure;
import org.deri.iris.api.evaluation.seminaive.model.ITree;
import org.deri.iris.evaluation.common.AdornedProgram;
import org.deri.iris.evaluation.magic.MagicSetImpl;
import org.deri.iris.evaluation.seminaive.InMemoryProcedure;
import org.deri.iris.evaluation.seminaive.NaiveEvaluation;
import org.deri.iris.evaluation.seminaive.Rule2Relation;

/**
 * <p>
 * Executes a programm.
 * </p>
 * <p>
 * $Id: Executor.java,v 1.1 2006-12-05 08:11:29 richardpoettler Exp $
 * </p>
 * 
 * @author Richard Pöttler
 * @author Darko Anicic, DERI Innsbruck
 * @version $Revision: 1.1 $
 */
public class Executor implements IExecutor {

	/** The program to execute. */
	private IProgram prog;

	/** The evaluator. */
	private IEvaluator evaluator;

	/** The evaluation mehtod. */
	private EvaluationMethod method;

	/**
	 * Creates a new evaluator with a given programm and evaluator.
	 * 
	 * @param p
	 *            the program
	 * @param e
	 *            the evaluator
	 * @throws NullPointerException
	 *             if the program or the evaluator is {@code null}
	 */
	Executor(final IProgram p, final IEvaluator e) {
		if ((p == null) || (e == null)) {
			throw new NullPointerException(
					"The program and evaluator must not be null");
		}
		this.prog = p;
		this.evaluator = e;
	}

	public void setEvaluationMethod(EvaluationMethod method) {
		if (method == null) {
			throw new NullPointerException("The method must not be null");
		}
		this.method = method;
	}

	public Set<ITuple> computeSubstitution(final IQuery q) {
		// TODO: test stratification
		// TODO: choice between the procedures
		final MagicSetImpl ms = new MagicSetImpl(new AdornedProgram(prog
				.getRules(), q));
		final IProgram p = ms.createProgram(prog);
		// translating the query and the rules to the relational algebra model
		final Rule2Relation rr = new Rule2Relation();
		final Map<IPredicate, ITree> ruleT = rr.evalRule(p.getRules());
		final Map<IPredicate, ITree> queryT = rr.evalQueries(p.getQueries().iterator());
		// run the evalutaion
		final IEvaluationProcedure proc = new InMemoryProcedure(null, p);
		final IEvaluator e = new NaiveEvaluation(proc, p, ruleT, queryT);
		e.evaluate();
		
		return null;
	}

	public Set computeSubstitutions() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean execute() {
		// TODO Auto-generated method stub
		return false;
	}
}
