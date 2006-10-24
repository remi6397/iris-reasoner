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
package org.deri.iris.evaluation.magic;

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.TERM;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.graph.LabeledDirectedEdge;

/**
 * <p>
 * Runns various test on the sip.
 * </p>
 * <p>
 * $Id: SipImplTest.java,v 1.1 2006-10-24 10:28:48 richardpoettler Exp $
 * </p>
 * 
 * @author richi
 * @version $Revision: 1.1 $
 * @date $Date: 2006-10-24 10:28:48 $
 */
public class SipImplTest extends TestCase {

	/** The first sip to test */
	private static SIPImpl sip;

	/** The second sip to test */
	private static SIPImpl sip1;

	public static Test suite() {
		return new TestSuite(SipImplTest.class, SipImplTest.class
				.getSimpleName());
	}

	public void setUp() {
		final IRule r = BASIC.createRule(BASIC.createHead(createLiteral("sg",
				"X", "Y")), BASIC.createBody(createLiteral("up", "X", "Z1"),
				createLiteral("sg", "Z1", "Z2"), createLiteral("flat", "Z2",
						"Z3"), createLiteral("sg", "Z3", "Z4"), createLiteral(
						"down", "Z4", "Y")));
		final IQuery q = BASIC.createQuery(BASIC.createLiteral(true, BASIC
				.createPredicate("sg", 2), BASIC.createTuple(TERM
				.createString("john"), TERM.createVariable("X"))));

		sip = new SIPImpl(r, q);
		
		final IRule r1 = BASIC.createRule(BASIC.createHead(createLiteral("rsg",
				"X", "Y")), BASIC.createBody(createLiteral("up", "X", "X1"),
				createLiteral("rsg", "Y1", "X1"), createLiteral("down", "Y1",
						"Y")));
		final IQuery q1 = BASIC.createQuery(BASIC.createLiteral(true, BASIC
				.createPredicate("rsg", 2), BASIC.createTuple(TERM
				.createString("a"), TERM.createVariable("Y"))));

		sip1 = new SIPImpl(r1, q1);
	}

	/**
	 * Tests whether the sip contains all expected edges.
	 */
	public void testForEdges() {
		final Set<LabeledDirectedEdge<Set<IVariable>>> e = new HashSet<LabeledDirectedEdge<Set<IVariable>>>();
		e.add(createEdge(createLiteral("sg", "X", "Y"), createLiteral("up",
				"X", "Z1"), createVarList("X")));
		e.add(createEdge(createLiteral("up", "X", "Z1"), createLiteral("sg",
				"Z1", "Z2"), createVarList("X", "Z1")));
		e.add(createEdge(createLiteral("sg", "Z1", "Z2"), createLiteral("flat",
				"Z2", "Z3"), createVarList("X", "Z1", "Z2")));
		e.add(createEdge(createLiteral("flat", "Z2", "Z3"), createLiteral("sg",
				"Z3", "Z4"), createVarList("X", "Z1", "Z2", "Z3")));
		e.add(createEdge(createLiteral("sg", "Z3", "Z4"), createLiteral("down",
				"Z4", "Y"), createVarList("X", "Z1", "Z2", "Z3", "Z4")));

		assertEquals("The number of edges doesn't match", e.size(), sip
				.getEdges().size());
		assertTrue("The sip must contain all given edges", e.containsAll(sip
				.getEdges()));
		
		
		final Set<LabeledDirectedEdge<Set<IVariable>>> e1 = new HashSet<LabeledDirectedEdge<Set<IVariable>>>();
		e1.add(createEdge(createLiteral("rsg", "X", "Y"), createLiteral("up",
				"X", "X1"), createVarList("X")));
		e1.add(createEdge(createLiteral("up", "X", "X1"), createLiteral("rsg",
				"Y1", "X1"), createVarList("X", "X1")));
		e1.add(createEdge(createLiteral("rsg", "Y1", "X1"), createLiteral("down",
				"Y1", "Y"), createVarList("X", "X1", "Y1")));

		assertEquals("The number of edges doesn't match", e1.size(), sip1
				.getEdges().size());
		assertTrue("The sip must contain all given edges", e1.containsAll(sip1
				.getEdges()));
	}

	/**
	 * Creates a positive literal out of a predicate name and a set of variable
	 * strings.
	 * 
	 * @param pred
	 *            the predicate name
	 * @param vars
	 *            the variable names
	 * @return the constructed literal
	 * @throws NullPointerException
	 *             if the predicate name or the set of variable names is
	 *             {@code null}
	 * @throws NullPointerException
	 *             if the set of variable names contains {@code null}
	 * @throws IllegalArgumentException
	 *             if the name of the predicate is 0 characters long
	 */
	private static ILiteral createLiteral(final String pred,
			final String... vars) {
		if ((pred == null) || (vars == null)) {
			throw new NullPointerException(
					"The predicate and the vars must not be null");
		}
		if (pred.length() <= 0) {
			throw new IllegalArgumentException(
					"The predicate name must be longer than 0 chars");
		}
		if (Arrays.asList(vars).contains(null)) {
			throw new NullPointerException("The vars must not contain null");
		}

		return BASIC.createLiteral(true, BASIC.createPredicate(pred,
				vars.length), BASIC.createTuple(new ArrayList<ITerm>(
				createVarList(vars))));
	}

	/**
	 * Creates a list of IVariables out of a list of strings.
	 * 
	 * @param vars
	 *            the variable names
	 * @return the list of correspoinding variables
	 * @throws NullPointerException
	 *             if the vars is null, or contains null
	 */
	private static List<IVariable> createVarList(final String... vars) {
		if ((vars == null) || Arrays.asList(vars).contains(null)) {
			throw new NullPointerException(
					"The vars must not be null and must not contain null");
		}
		final List<IVariable> v = new ArrayList<IVariable>(vars.length);
		for (final String var : vars) {
			v.add(TERM.createVariable(var));
		}
		return v;
	}

	/**
	 * Creates a edge.
	 * 
	 * @param s
	 *            the source literal
	 * @param t
	 *            the target literal
	 * @param v
	 *            the passed variables (label)
	 * @returs the edge
	 * @throws NullPointerException
	 *             if the source, target or label is {@code null}
	 * @throws NullPointerException
	 *             if the label contains {@code null}
	 */
	private static LabeledDirectedEdge<Set<IVariable>> createEdge(
			final ILiteral s, final ILiteral t, final Collection<IVariable> v) {
		if ((s == null) || (t == null) || (v == null)) {
			throw new NullPointerException(
					"The source, target and variables must not be null");
		}
		if (v.contains(null)) {
			throw new NullPointerException(
					"The variables must not contain null");
		}
		return new LabeledDirectedEdge<Set<IVariable>>(s, t,
				new HashSet<IVariable>(v));
	}
}
