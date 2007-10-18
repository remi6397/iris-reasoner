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
package org.deri.iris.basics;

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.CONCRETE;
import static org.deri.iris.factory.Factory.TERM;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deri.iris.ObjectTests;
import org.deri.iris.MiscHelper;
import org.deri.iris.api.basics.ILiteral;

/**
 * <p>
 * Tests for the query.
 * </p>
 * <p>
 * $Id$
 * </p>
 *
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision$
 */
public class QueryTest extends TestCase {

	private static final Body BODY;

	private static final Body BODYMORE;

	static {
		List<ILiteral> tempLiterals = new ArrayList<ILiteral>();

		ILiteral literal = BASIC.createLiteral(true, 
				BASIC.createAtom(
					BASIC.createPredicate("sin", 1), 
					BASIC.createTuple(CONCRETE.createInteger(1))));
		tempLiterals.add(literal);

		tempLiterals.add(MiscHelper.createLiteral("cos", "X"));
		tempLiterals.add(MiscHelper.createLiteral("date", "J", "K", "L"));

		BODY = new Body(tempLiterals);

		tempLiterals = new ArrayList<ILiteral>(BODY.getLiterals());

		tempLiterals.set(2, MiscHelper.createLiteral("date", "J", "K", "M"));

		BODYMORE = new Body(tempLiterals);
	}

	public static Test suite() {
		return new TestSuite(QueryTest.class, QueryTest.class.getSimpleName());
	}

	public void testEquals() {
		ObjectTests.runTestEquals(new Query(BODY), new Query(BODY), new Query(
				BODYMORE));
		ObjectTests.runTestEquals(new Query(BODY), new Query(BODY), new Query(
				null));
	}

	public void testHashCode() {
		ObjectTests.runTestHashCode(new Query(BODY), new Query(BODY));
	}
}
