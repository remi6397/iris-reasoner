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
package org.deri.iris.graph;

import static org.deri.iris.factory.Factory.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org._3pq.jgrapht.Edge;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.basics.Body;
import org.deri.iris.basics.Head;
import org.deri.iris.basics.Literal;
import org.deri.iris.basics.Predicate;
import org.deri.iris.basics.Rule;

/**
 * @author richi
 * 
 */
public class PredicateGraphTest extends TestCase {
	private static final IPredicate a = BASIC.createPredicate("a", 0);

	private static final IPredicate b = BASIC.createPredicate("b", 0);

	private static final IPredicate c = BASIC.createPredicate("c", 0);

	private static final IPredicate d = BASIC.createPredicate("d", 0);

	private static final IPredicate e = BASIC.createPredicate("e", 0);

	private static final ILiteral pa = BASIC.createLiteral(true, a);

	private static final ILiteral pb = BASIC.createLiteral(true, b);

	private static final ILiteral pc = BASIC.createLiteral(true, c);

	private static final ILiteral pd = BASIC.createLiteral(true, d);

	private static final ILiteral pe = BASIC.createLiteral(true, e);

	private static final ILiteral na = BASIC.createLiteral(false, a);

	private static final ILiteral nb = BASIC.createLiteral(false, b);

	private static final ILiteral nc = BASIC.createLiteral(false, c);

	private static final ILiteral nd = BASIC.createLiteral(false, d);

	private static final ILiteral ne = BASIC.createLiteral(false, e);

	public static Test suite() {
		return new TestSuite(PredicateGraphTest.class, PredicateGraphTest.class
				.getSimpleName());
	}

	public void testDetectCycles() {
		final PredicateGraph pg = constructGraph_1();
		assertTrue("There are cycles in the graph", pg.detectCycles());
	}

	public void testCountNegativesForCycle() {
		final PredicateGraph pg = constructGraph_1();
		assertEquals("The negative edges aren't calculated correctly", 1, pg
				.countNegativesForCycle());
	}

	public void testFindVertexesForCycle() {
		final PredicateGraph pg = constructGraph_1();
		final Set<IPredicate> reference = new HashSet<IPredicate>();
		reference.add(a);
		reference.add(c);
		reference.add(d);
		reference.add(e);
		final Set testing = pg.findVertexesForCycle();

		assertEquals("The count of the returned vertexes "
				+ "and the reference set must be equal", reference.size(),
				testing.size());
		assertTrue(
				"The returned set must contain all elements of the referneces",
				testing.containsAll(reference));
	}

	public void testFindEdgesForCycle() {
		final PredicateGraph pg = constructGraph_1();
		final Set<Edge> reference = new HashSet<Edge>();
		reference.add(new LabeledDirectedEdge(a, c, false));
		reference.add(new LabeledDirectedEdge(c, d, true));
		reference.add(new LabeledDirectedEdge(d, e, true));
		reference.add(new LabeledDirectedEdge(e, a, true));
		final Set<Edge> testing = pg.findEdgesForCycle();

		assertEquals("The count of the returned vertexes "
				+ "and the reference set must be equal", reference.size(),
				testing.size());
		assertTrue(
				"The returned set must contain all elements of the referneces",
				containsAllOnEquals(testing, reference));
	}

	private static PredicateGraph constructGraph_1() {
		PredicateGraph pg = new PredicateGraph();

		// constructing the rules
		IRule ra = BASIC.createRule(BASIC.createHead(pa), BASIC.createBody(pe));
		IRule rb = BASIC.createRule(BASIC.createHead(pb), BASIC.createBody(pa,
				nc));
		IRule rc = BASIC.createRule(BASIC.createHead(pc), BASIC.createBody(na));
		IRule rd = BASIC.createRule(BASIC.createHead(pd), BASIC.createBody(pc,
				ne));
		IRule re = BASIC.createRule(BASIC.createHead(pe), BASIC.createBody(pd,
				nc));

		// adding the rules to the graph
		pg.addRule(ra);
		pg.addRule(rb);
		pg.addRule(rc);
		pg.addRule(rd);
		pg.addRule(re);

		return pg;
	}

	/**
	 * Stupid workarround, because the ***** from jgrapht haven't implemented
	 * the hashCode method and are using HashSets/HashMaps.</br>This method
	 * simply determines whether every element in s0 is contained int s1 using
	 * the equals method.
	 * 
	 * @param s0
	 *            the source set from where to take the elements
	 * @param s1
	 *            the set where to search for the elements
	 * @return true if all elements of s0 are contained in s1
	 */
	private static boolean containsAllOnEquals(final Set s0, final Set s1) {
		if (s1.size() < s0.size()) {
			return false;
		}
		for (Object o0 : s0) {
			boolean found = false;
			for (Object o1 : s1) {
				if (o0.equals(o1)) {
					found = true;
					break;
				}
			}
			if (!found) {
				return false;
			}
		}
		return true;
	}
}
