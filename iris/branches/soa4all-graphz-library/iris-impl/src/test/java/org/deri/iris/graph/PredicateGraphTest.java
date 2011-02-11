/*
 * Copyright 2011, Semantic Technology Institute (STI) Innsbruck, 
 * University of Innsbruck, Technikerstrasse 21a, 6020 Innsbruck, Austria.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.deri.iris.graph;

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.TERM;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.utils.EdgeSetEquality;

import eu.soa4all.graph.Edge;
import eu.soa4all.graph.impl.GraphFactory;

/**
 * <p>
 * Tests the PredicateGraph.
 * </p>
 * <p>
 * $Id$
 * </p>
 * @author Richard Pöttler (richard dot poettler at deri dot org)
 * @version $Revision$
 */
public class PredicateGraphTest extends TestCase {

	/** The cyceled predicate graph. */
	private PredicateGraph pg0;

	/** The cyceled rules. */
	private Set<IRule> r0 = new HashSet<IRule>();

	/** The unrecursive predicate graph. */
	private PredicateGraph pg1;

	/** The unrecursive rules. */
	private Set<IRule> r1 = new HashSet<IRule>();

	private static final ITuple t = BASIC.createTuple(TERM.createVariable("X"));

	private static final IPredicate a = BASIC.createPredicate("a", 1);

	private static final IPredicate b = BASIC.createPredicate("b", 1);

	private static final IPredicate c = BASIC.createPredicate("c", 1);

	private static final IPredicate d = BASIC.createPredicate("d", 1);

	private static final IPredicate e = BASIC.createPredicate("e", 1);

	private static final ILiteral pa = BASIC.createLiteral(true, a, t);

	private static final ILiteral pb = BASIC.createLiteral(true, b, t);

	private static final ILiteral pc = BASIC.createLiteral(true, c, t);

	private static final ILiteral pd = BASIC.createLiteral(true, d, t);

	private static final ILiteral pe = BASIC.createLiteral(true, e, t);

	private static final ILiteral na = BASIC.createLiteral(false, a, t);

	private static final ILiteral nb = BASIC.createLiteral(false, b, t);

	private static final ILiteral nc = BASIC.createLiteral(false, c, t);

	private static final ILiteral nd = BASIC.createLiteral(false, d, t);

	private static final ILiteral ne = BASIC.createLiteral(false, e, t);

	public static Test suite() {
		return new TestSuite(PredicateGraphTest.class, PredicateGraphTest.class
				.getSimpleName());
	}

	public void testDetectCycles() {
		assertTrue("There are cycles in the graph", pg0.detectCycles());
	}

	public void testCountNegativesForCycle() {
		assertEquals("The negative edges aren't calculated correctly", 1, pg0
				.countNegativesForCycle());
	}

	public void testFindVertexesForCycle() {
		final Set<IPredicate> reference = new HashSet<IPredicate>();
		reference.add(a);
		reference.add(c);
		reference.add(d);
		reference.add(e);
		final Set testing = pg0.findVertexesForCycle();

		assertEquals("The predicate sets must be equal", reference, testing);
	}

	public void testFindEdgesForCycle() {
		GraphFactory gf = GraphFactory.getInstance();
		
		final Set<Edge<IPredicate, Boolean>> reference = new HashSet<Edge<IPredicate, Boolean>>();
		reference.add(gf.createEdge(a, c, false));
		reference.add(gf.createEdge(c, d, true));
		reference.add(gf.createEdge(d, e, true));
		reference.add(gf.createEdge(e, a, true));
		final Set<Edge<IPredicate, Boolean>> testing = pg0.findEdgesForCycle();

		EdgeSetEquality.assertEdgeSetEquality("The edge sets must be equal", reference, testing);
	}

	public void testGetDepends() {
		final Set<IPredicate> for_b = new HashSet<IPredicate>();
		for_b.add(c);
		for_b.add(e);
		final Set<IPredicate> for_d = new HashSet<IPredicate>();
		for_d.addAll(for_b);
		for_d.add(b);

		assertEquals("Something wrong with the depends calculation", for_b, pg1
				.getDepends(b));
		assertEquals("Something wrong with the depends calculation", for_d, pg1
				.getDepends(d));
	}

	public void testPredicateComparator() {
		final List<IPredicate> reference = new ArrayList<IPredicate>(Arrays
				.asList(new IPredicate[] { e, c, b, d, a }));
		final List<IPredicate> testing = new ArrayList<IPredicate>(Arrays
				.asList(new IPredicate[] { a, b, c, d, e }));
		Collections.sort(testing, pg1.getPredicateComparator());
		
		assertEquals("The sort order isn't correct", reference, testing);
	}

	public void setUp() {
		// the cyceled predicate graph
		// a(X) :- e(X)
		r0.add(BASIC.createRule(Arrays.asList(pa), Arrays.asList(pe)));
		// b(X) :- a(X), -c(X)
		r0
				.add(BASIC.createRule(Arrays.asList(pb), Arrays.asList(
						pa, nc)));
		// c(X) :- -a(X)
		r0.add(BASIC.createRule(Arrays.asList(pc), Arrays.asList(na)));
		// d(X) :- c(X)
		r0.add(BASIC.createRule(Arrays.asList(pd), Arrays.asList(pc)));
		// e(X) :- d(X)
		r0.add(BASIC.createRule(Arrays.asList(pe), Arrays.asList(pd)));

		pg0 = new PredicateGraph();
		pg0.addRule(r0);

		// the unrecursive predicate graph
		// a(X) :- b(X), c(X), d(X)
		r1.add(BASIC.createRule(Arrays.asList(pa), Arrays.asList(pb, pc, pd)));
		// b(X) :- c(X)
		r1.add(BASIC.createRule(Arrays.asList(pb), Arrays.asList(pc)));
		// c(X) :- e(X)
		r1.add(BASIC.createRule(Arrays.asList(pc), Arrays.asList(pe)));
		// d(X) :- b(X)
		r1.add(BASIC.createRule(Arrays.asList(pd), Arrays.asList(pb)));

		pg1 = new PredicateGraph();
		pg1.addRule(r1);
	}
}
