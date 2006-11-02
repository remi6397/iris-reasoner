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

import static org.deri.iris.MiscHelper.createLiteral;
import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.BUILTIN;
import static org.deri.iris.factory.Factory.TERM;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.evaluation.seminaive.MiscOps;

/**
 * <p>
 * Tests the methods in the MiscOps class.
 * </p>
 * <p>
 * $Id: MiscOpsTest.java,v 1.1 2006-11-02 11:00:03 richardpoettler Exp $
 * </p>
 * 
 * @author richi
 * @version $Revision: 1.1 $
 * @date $Date: 2006-11-02 11:00:03 $
 */
public class MiscOpsTest extends TestCase {

	public static TestSuite suite() {
		return new TestSuite(MiscOpsTest.class, MiscOpsTest.class
				.getSimpleName());
	}

	/**
	 * Tests the rectify method.
	 */
	public void testRectrify() {
		final ILiteral hl = BASIC.createLiteral(true, BASIC.createPredicate(
				"p", 3), BASIC.createTuple(TERM.createString("a"), TERM
				.createVariable("X"), TERM.createVariable("Y")));
		final IRule r0 = BASIC.createRule(BASIC.createHead(hl), BASIC
				.createBody(createLiteral("r", "X", "Y")));
		final IRule rec0 = BASIC.createRule(BASIC.createHead(createLiteral("p",
				"r_0", "r_1", "r_2")), BASIC.createBody(createLiteral("r",
				"r_1", "r_2"), BASIC.createLiteral(true, BUILTIN.createEqual(
				TERM.createVariable("r_0"), TERM.createString("a")))));
		assertEquals(rec0, MiscOps.rectify(r0));

		final IRule r1 = BASIC
				.createRule(
						BASIC.createHead(createLiteral("p", "X", "Y", "X")),
						BASIC.createBody(createLiteral("r", "Y", "X")));
		final IRule rec1 = BASIC.createRule(BASIC.createHead(createLiteral("p",
				"r_0", "r_1", "r_2")), BASIC.createBody(createLiteral("r",
				"r_1", "r_0"), BASIC.createLiteral(true, BUILTIN.createEqual(
				TERM.createVariable("r_2"), TERM.createVariable("r_0")))));
		assertEquals(rec1, MiscOps.rectify(r1));
	}
}
