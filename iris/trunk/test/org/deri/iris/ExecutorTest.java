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

import static org.deri.iris.MiscHelper.createFact;
import static org.deri.iris.MiscHelper.createLiteral;
import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.TERM;

import java.util.Map;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deri.iris.api.IProgram;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.evaluation.Evaluator;
import org.deri.iris.evaluation.algebra.ExpressionEvaluator;
import org.deri.iris.factory.Factory;

/**
 * <p>
 * Tests the Executor.
 * </p>
 * <p>
 * $Id: ExecutorTest.java,v 1.2 2006-12-19 18:20:31 darko Exp $
 * </p>
 * 
 * @author Richard Pöttler
 * @version $Revision: 1.2 $
 */
public class ExecutorTest extends TestCase {
	private IProgram p;

	public void setUp() {
		// s('w')
		// s('b')
		// s('a')
		//		
		// r('a')
		// r('c')
		//
		// p('d')
		//
		// q(?X) :- s(?X), not p(?X)
		// p(?X) :- r(?X)
		// p(?X) :- p(?X) // is commented out -> would produce loop in sip
		// ?- q(?X)
		p = Factory.PROGRAM.createProgram();
		p.addFact(createFact("s", "w"));
		p.addFact(createFact("s", "b"));
		p.addFact(createFact("s", "a"));
		p.addFact(createFact("r", "a"));
		p.addFact(createFact("r", "c"));
		p.addFact(createFact("p", "d"));
		p.addRule(BASIC.createRule(BASIC.createHead(createLiteral("q", "X")),
				BASIC.createBody(createLiteral("s", "X"), BASIC.createLiteral(
						false, BASIC.createPredicate("p", 1), BASIC
						.createTuple(TERM.createVariable("X"))))));
		p.addRule(BASIC.createRule(BASIC.createHead(createLiteral("p", "X")),
				BASIC.createBody(createLiteral("r", "X"))));
//		p.addRule(BASIC.createRule(BASIC.createHead(createLiteral("p", "X")),
//				BASIC.createBody(createLiteral("p", "X"))));
		p.addQuery(BASIC.createQuery(createLiteral("q", "X")));
	}

	public void testComputeSubstitution() {
		Executor e = new Executor(p, new ExpressionEvaluator());
		for (final Map.Entry<IQuery, Set<ITuple>> me : e.computeSubstitutions()
				.entrySet()) {
			System.out.println("q: " + me.getKey());
			for (final ITuple t : me.getValue()) {
				System.out.println(t);
			}
		}
	}

	public static Test suite() {
		return new TestSuite(ExecutorTest.class, ExecutorTest.class
				.getSimpleName());
	}
}
