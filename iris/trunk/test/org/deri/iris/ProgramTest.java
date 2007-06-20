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

import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.IProgram;
import org.deri.iris.compiler.Parser;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * Tests various functionalities of the program.
 * </p>
 * <p>
 * $Id: ProgramTest.java,v 1.1 2007-06-20 12:21:05 poettler_ric Exp $
 * </p>
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision: 1.1 $
 */
public class ProgramTest extends TestCase {

	public static Test suite() {
		return new TestSuite(ProgramTest.class, ProgramTest.class.getSimpleName());
	}

	/**
	 * Tests whether it is possible to construct two programs which are
	 * independent of each other. Means, that one program should not contain
	 * the other's objects.
	 */
	public void testMultiblePrograms() {
		final String prog0 = "a(?X) :- b(?X).\n" + 
			"b('a').";
		final String prog1 = "x(?X) :- y(?X).\n" + 
			"y('a').";
		final IProgram p0 = Parser.parse(prog0);
		final IProgram p1 = Parser.parse(prog1);
		for (final IPredicate p : p0.getPredicates()) {
			assertFalse(p + " must not be contained in p1: " + p1.getPredicates()
				, p1.getPredicates().contains(p));
		}
	}
}
