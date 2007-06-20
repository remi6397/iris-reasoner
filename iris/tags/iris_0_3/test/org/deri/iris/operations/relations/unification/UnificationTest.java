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

package org.deri.iris.operations.relations.unification;

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.TERM;
import static org.deri.iris.factory.Factory.TUPLE_OPERATION;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.operations.tuple.IUnification;
import org.deri.iris.api.terms.IConstructedTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.operations.tuple.Multiequation;
import org.deri.iris.operations.tuple.Unification;
import org.deri.iris.operations.tuple.Unification.UnificationResult;

/**
 * @author Darko Anicic, DERI Innsbruck
 * @date   08.09.2006 16:59:01
 */
public class UnificationTest extends TestCase {

	private static IUnification unifier = null;

	
	public static Test suite() {
		return new TestSuite(UnificationTest.class, UnificationTest.class.getSimpleName());
	}
	
	/**
	 * Unify following two terms:
	 * 
	 * f(x1, g(x2, x3), x2, b)
	 * f(g(h(a, x5), x2), x1, h(a, x4), x4)
	 * 
	 * Result:
	 * [[[0]{initVar} = ([f[x1, x1, x2, x4]]), 
	 * [0]{x1} = ([g[x2, x3]]), 
	 * [0]{x2, x3} = ([h[a, x4]]), 
	 * [0]{x4, x5} = ([b])]]
	 *
	 */
	public static void testProgram0(){
		// create: g(x2, x3)
		IConstructedTerm g1 = TERM.createConstruct("g", 
				TERM.createVariable("x2"), TERM.createVariable("x3"));
		
		// create: f(x1, g(x2, x3), x2, b)
		IConstructedTerm f1 = TERM.createConstruct("f", 
				TERM.createVariable("x1"), g1, TERM.createVariable("x2"),
				TERM.createString("b"));
		
		// create: h(a, x5)
		IConstructedTerm h1 = TERM.createConstruct("h", 
				TERM.createString("a"), TERM.createVariable("x5"));
		
		// create: g(h(a, x5), x2)
		IConstructedTerm g2 = TERM.createConstruct("g", h1, 
				TERM.createVariable("x2"));
		
		// create: h(a, x4)
		IConstructedTerm h2 = TERM.createConstruct("h", 
				TERM.createString("a"), TERM.createVariable("x4"));
		
		// create: f(g(h(a, x5), x2), x1, h(a, x4), x4)
		IConstructedTerm f2 = TERM.createConstruct("f", g2, 
				TERM.createVariable("x1"), h2, TERM.createVariable("x4"));
		
		ITuple t0 = BASIC.createTuple(f1, TERM.createVariable("x6"));
		ITuple t1 = BASIC.createTuple(f2, TERM.createString("c"));
		
		//unifier = TUPLE_OPERATION.createUnificationOperator(t0, t1);
		unifier = TUPLE_OPERATION.createUnificationOperator(f1, f2);
		UnificationResult l0= unifier.unify();
		
		List<Multiequation> tailList = new ArrayList<Multiequation>();
		
		Set<ITerm> s = new HashSet<ITerm>();
		s.add(Unification.INIT_VAR_CONSTANT);
		List<ITerm> m = new ArrayList<ITerm>();
		
		// create: f(x1, x1, x2, x4)
		IConstructedTerm f = TERM.createConstruct("f", 
				TERM.createVariable("x1"), TERM.createVariable("x1"), 
				TERM.createVariable("x2"), TERM.createVariable("x4"));
		
		List<ITerm> headList = new ArrayList<ITerm>();
		headList.add(f);
		
		s = new HashSet<ITerm>();
		s.add(TERM.createVariable("x1"));
		m = new ArrayList<ITerm>();
		m.add(g1);
		Multiequation me = new Multiequation(s, m);
		tailList.add(me);
		
		s = new HashSet<ITerm>();
		s.add(TERM.createVariable("x2"));
		s.add(TERM.createVariable("x3"));
		m = new ArrayList<ITerm>();
		m.add(h2);
		me = new Multiequation(s, m);
		tailList.add(me);
		
		s = new HashSet<ITerm>();
		s.add(TERM.createVariable("x4"));
		s.add(TERM.createVariable("x5"));
		m = new ArrayList<ITerm>();
		m.add(TERM.createString("b"));
		me = new Multiequation(s, m);
		tailList.add(me);
		
		
		Assert.assertEquals("The length of multiequation list" +
				" needs to be equal with the result list", 
				l0.getHeads().size() + l0.getTails().size(),
				headList.size() + tailList.size());
		
		Assert.assertTrue("The multiequation head lists" +
				" need to contain the same terms", 
				l0.getHeads().containsAll(headList));
		
		Assert.assertTrue("The multiequation tail lists" +
				" need to contain the same terms", 
				l0.getTails().containsAll(tailList));
	}
	
	public static void testProgram1(){
		// create: p(X1, x1, x3)
		IConstructedTerm p1 = TERM.createConstruct("p", 
				TERM.createVariable("x1"), 
				TERM.createVariable("x1"), 
				TERM.createVariable("x3"));
		
		// create: p(a, b, c)
		IConstructedTerm p2 = TERM.createConstruct("p", 
				TERM.createString("a"),
				TERM.createString("b"),
				TERM.createString("c"));
		
		unifier = TUPLE_OPERATION.createUnificationOperator(p1, p2);
		UnificationResult l0= unifier.unify();
		
		Assert.assertNull("It is not possible to unify these two terms, " +
				"thus the result is null", l0);
	}	
}
