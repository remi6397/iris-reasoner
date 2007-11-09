/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions by 
 * built-in predicates, default negation (under well-founded semantics), 
 * function symbols and contexts. 
 * 
 * Copyright (C) 2007  Digital Enterprise Research Institute (DERI), 
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

import static org.deri.iris.factory.Factory.BASIC;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deri.iris.MiscHelper;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.evaluation.common.AdornedProgram.AdornedPredicate;
import org.deri.iris.evaluation.common.Adornment;

/**
 * <p>
 * Tests for the Facts.
 * </p>
 * <p>
 * $Id: FactsTest.java,v 1.1 2007-10-31 15:15:50 poettler_ric Exp $
 * </p>
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision: 1.1 $
 */
public class FactsTest extends TestCase {

	public static Test suite() {
		return new TestSuite(FactsTest.class, FactsTest.class.getSimpleName());
	}

	/**
	 * Test to check whether normal and adorned predicates access the same
	 * relations/facts.
	 * @see <a href="http://sourceforge.net/tracker/index.php?func=detail&aid=1822055&group_id=167309&atid=842434">bug #1822055: Magic Sets gives different results to Naive and Semi-Naive</a>
	 */
	public void testSameRelations() {
		final IPredicate p = BASIC.createPredicate("p", 1);
		final IPredicate pb = new AdornedPredicate("p", new Adornment[]{Adornment.BOUND});
		final IPredicate pf = new AdornedPredicate("p", new Adornment[]{Adornment.FREE});
		final Facts f = new Facts();
		final ITuple normal = MiscHelper.createTuple("normal");
		final ITuple bound = MiscHelper.createTuple("bound");
		final ITuple free = MiscHelper.createTuple("free");
		// adding facts
		f.addFact(BASIC.createAtom(p, normal));
		f.addFact(BASIC.createAtom(pb, bound));
		f.addFact(BASIC.createAtom(pf, free));
		// assert the normal relation
		assertTrue("Can't find the 'normal' for the normal predicate", f.getFacts(p).contains(normal));
		assertTrue("Can't find the 'bound' for the normal predicate", f.getFacts(p).contains(bound));
		assertTrue("Can't find the 'free' for the normal predicate", f.getFacts(p).contains(free));
		// assert the bound relation
		assertTrue("Can't find the 'normal' for the bound predicate", f.getFacts(pb).contains(normal));
		assertTrue("Can't find the 'bound' for the bound predicate", f.getFacts(pb).contains(bound));
		assertTrue("Can't find the 'free' for the bound predicate", f.getFacts(pb).contains(free));
		// assert the free relation
		assertTrue("Can't find the 'normal' for the free predicate", f.getFacts(pf).contains(normal));
		assertTrue("Can't find the 'bound' for the free predicate", f.getFacts(pf).contains(bound));
		assertTrue("Can't find the 'free' for the free predicate", f.getFacts(pf).contains(free));
	}
}
