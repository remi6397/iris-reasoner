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
package org.deri.iris.querycontainment;

import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.IProgram;
import org.deri.iris.compiler.Parser;
import org.deri.iris.querycontainment.QueryContainment;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * Tests the query containment functionality.
 * </p>
 * <p>
 * $Id: QueryContainmentTest.java,v 1.1 2007-11-07 16:13:31 nathaliest Exp $
 * </p>
 * @author Nathalie Steinmetz, DERI Innsbruck
 * @version $Revision: 1.1 $
 */
public class QueryContainmentTest extends TestCase {

	public static Test suite() {
		return new TestSuite(QueryContainmentTest.class, QueryContainmentTest.class.getSimpleName());
	}

	/**
	 * Tests whether one query is contained within another query.
	 * 
	 * @throws Exception 
	 */
	public void testQueryContaiment() throws Exception {
		final String prog = "vehicle(?X) :- car(?X).";
		final String q1 = "?-car(?x).";
		final String q2 = "?-vehicle(?x).";
		final IProgram program = Parser.parse(prog);
		final IProgram query1Prog = Parser.parse(q1);
		final IQuery query1 = query1Prog.getQueries().iterator().next();
		final IProgram query2Prog = Parser.parse(q2);
		final IQuery query2 = query2Prog.getQueries().iterator().next();
		final QueryContainment queryCont = new QueryContainment(program);

		boolean result = queryCont.checkQueryContainment(query1, query2);
		
		assertTrue(result);
	}
	
	/**
	 * Tests whether one query is not contained within another query.
	 * 
	 * @throws Exception 
	 */
	public void testQueryContaiment2() throws Exception {
		final String prog = "vehicle(?X) :- car(?X).";
		final String q1 = "?-vehicle(?X).";
		final String q2 = "?-car(?X).";
		final IProgram program = Parser.parse(prog);
		final IProgram query1Prog = Parser.parse(q1);
		final IQuery query1 = query1Prog.getQueries().iterator().next();
		final IProgram query2Prog = Parser.parse(q2);
		final IQuery query2 = query2Prog.getQueries().iterator().next();
		final QueryContainment queryCont = new QueryContainment(program);

		boolean result = queryCont.checkQueryContainment(query1, query2);
		
		assertFalse(result);
	}
	
}
