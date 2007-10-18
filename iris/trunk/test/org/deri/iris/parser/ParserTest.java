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
package org.deri.iris.parser;

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.BUILTIN;
import static org.deri.iris.factory.Factory.CONCRETE;
import static org.deri.iris.factory.Factory.TERM;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deri.iris.MiscHelper;
import org.deri.iris.api.IProgram;
import org.deri.iris.api.basics.IBody;
import org.deri.iris.api.basics.IHead;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.compiler.Parser;
import org.deri.iris.compiler.ParserException;
import org.deri.iris.factory.Factory;

/**
 * 
 * <p>
 * Tests for the datalog parser.
 * </p>
 * <p>
 * $Id: ParserTest.java,v 1.13 2007-10-18 13:31:13 poettler_ric Exp $
 * </p>
 * @author Joachim Adi Schuetz, DERI Innsbruck
 * @author Richard Pöttler, richard dot poettler at deri dot org
 * @version $Revision: 1.13 $
 */
public class ParserTest extends TestCase {

	Parser pars;
	IProgram prog;
	
	List<ILiteral> literals;
	Set<org.deri.iris.api.basics.IRule> rules;
	
	public static Test suite() {
		return new TestSuite(ParserTest.class, ParserTest.class.getSimpleName());
	}

	/**
	 * setup for Parser tests
	 */
	public void setUp() {
    	prog = Factory.PROGRAM.createProgram();

		literals = new ArrayList<ILiteral>();
		rules = new HashSet<org.deri.iris.api.basics.IRule>();		
	}

	/**
	 * run parser test
	 * @throws ParserException 
	 *
	 */
	protected void runParser(final String expr, final Set<IRule> rul) throws ParserException {
		Parser.parse(expr, prog);
		assertCol(rul, prog.getRules());
	}
	
	/**
	 * s(X, Y) :- p(Y, Z), r(Y, Z)
	 *
	 */
	public void testParser() throws Exception {
		
		// input
		String expr = "s(?X, ?Y) :- p(?X, ?Z), r(?Y, ?Z).";
		
		// result
		literals.add(MiscHelper.createLiteral("s", "X", "Y"));

		IHead head = BASIC.createHead(literals);

		literals.clear();
		
		literals.add(MiscHelper.createLiteral("p", "X", "Z"));
		literals.add(MiscHelper.createLiteral("r", "Y", "Z"));

		IBody body = BASIC.createBody(literals);
		
		rules.add(BASIC.createRule(head, body));

		runParser(expr, rules);
	}
	/**
	 * p(?X,?Y) :- r(?Z, ?Y) and ?X='a'
	 *
	 */
	public void testParser_1a() throws Exception {

		// input
		String expr = "p(?X, ?Y) :- r(?Z, ?Y), ?X='a'.";
		
		// result
		literals.add(MiscHelper.createLiteral("p", "X", "Y"));

		IHead head = BASIC.createHead(literals);

		literals.clear();
		
		literals.add(MiscHelper.createLiteral("r", "Z", "Y"));

		ILiteral literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("X"), TERM.createString("a")));
		literals.add(literal);

		IBody body = BASIC.createBody(literals);
		
		rules.add(BASIC.createRule(head, body));
				
		runParser(expr, rules);
	}
	/**
	 * p(?X,?Y) :- r(?X, ?Y) and ?X!='a'
	 *
	 */
	public void testParser_1b() throws Exception {

		// input
		String expr = "p(?X, ?Y) :- r(?Z, ?Y), ?X!='a'.";

		// result
		literals.add(MiscHelper.createLiteral("p", "X", "Y"));

		IHead head = BASIC.createHead(literals);

		literals.clear();
		
		literals.add(MiscHelper.createLiteral("r", "Z", "Y"));

		ILiteral literal = BASIC.createLiteral(true, BUILTIN.createUnequal(TERM.createVariable("X"), TERM.createString("a")));
		literals.add(literal);

		IBody body = BASIC.createBody(literals);
		
		rules.add(BASIC.createRule(head, body));

		runParser(expr, rules);
	}

	/**
	 * Tests whether all terms are created correctly.
	 */
	public void testTerms() throws Exception {
		final String expr = "ints(1). intl(_integer(2)). \n" + 
			"strs('hallos'). strl(_string('hallol')). \n" + 
			"decs(1.5). decl(_decimal(3.7)). \n" + 
			"sqs(sq#short). sql(_sqname(sq#long)). \n" + 
			"iris(_'http://deri.org/s#short'). iril(_iri('http://deri.org/l#long')). \n" + 
			"bool(_boolean('false')). \n" + 
			"double(_double(4.67)). \n" + 
			"float(_float(4.67)). \n" + 
			"date(_date(2007, 2, 6)). \n" + 
			"datetz(_date(2007, 2, 6, 2, 30)). \n" + 
			"duration(_duration(2007, 2, 6, 12, 45, 11)). \n" + 
			"durationms(_duration(2007, 2, 6, 12, 45, 11, 500)). \n" + 
			"datetimes(_datetime(2007, 2, 6, 12, 45, 11)). \n" + 
			"datetimel(_datetime(2007, 2, 6, 12, 45, 11, 1, 30)). \n" + 
			"datetimelms(_datetime(2007, 2, 6, 12, 45, 11, 500, 1, 30)). \n" + 
			"times(_time(12, 45, 11)). \n" + 
			"timel(_time(12, 45, 11, 1, 30)). \n" + 
			"timelms(_time(12, 45, 11, 500, 1, 30)). \n" + 
			"gday(_gday(6)).\n" + 
			"gmonth(_gmonth(2)).\n" + 
			"gyear(_gyear(2007)).\n" + 
			"gmonthday(_gmonthday(2, 6)).\n" + 
			"gyearmonth(_gyearmonth(2007, 2)).\n" + 
			"base(_base64binary('45df')).\n" + 
			"hex(_hexbinary('a1df')).\n";

		Parser.parse(expr, prog);

		// TODO: test the function term
		// asserting the short int
		IPredicate pred = BASIC.createPredicate("ints", 1);
		assertTrue("Could not find the short int", prog.getFacts(pred).contains(BASIC.createTuple(CONCRETE.createInteger(1))));
		// asserting the long int
		pred = BASIC.createPredicate("intl", 1);
		assertTrue("Could not find the short int", prog.getFacts(pred).contains(BASIC.createTuple(CONCRETE.createInteger(2))));
		// asserting the short string
		pred = BASIC.createPredicate("strs", 1);
		assertTrue("Could not find the long string", prog.getFacts(pred).contains(BASIC.createTuple(TERM.createString("hallos"))));
		// asserting the long string
		pred = BASIC.createPredicate("strl", 1);
		assertTrue("Could not find the long string", prog.getFacts(pred).contains(BASIC.createTuple(TERM.createString("hallol"))));
		// asserting the short decimal
		pred = BASIC.createPredicate("decs", 1);
		assertTrue("Could not find the short decimal", prog.getFacts(pred).contains(BASIC.createTuple(CONCRETE.createDecimal(1.5))));
		// asserting the long decimal
		pred = BASIC.createPredicate("decl", 1);
		assertTrue("Could not find the long decimal", prog.getFacts(pred).contains(BASIC.createTuple(CONCRETE.createDecimal(3.7))));
		// asserting the short sqname
		pred = BASIC.createPredicate("sqs", 1);
		assertTrue("Could not find the short sqname", prog.getFacts(pred).contains(BASIC.createTuple(CONCRETE.createSqName("sq#short"))));
		// asserting the long sqname
		pred = BASIC.createPredicate("sql", 1);
		assertTrue("Could not find the long sqname", prog.getFacts(pred).contains(BASIC.createTuple(CONCRETE.createSqName("sq#long"))));
		// asserting the short iri
		pred = BASIC.createPredicate("iris", 1);
		assertTrue("Could not find the short iri", prog.getFacts(pred).contains(BASIC.createTuple(CONCRETE.createIri("http://deri.org/s#short"))));
		// asserting the long iri
		pred = BASIC.createPredicate("iril", 1);
		assertTrue("Could not find the long iri", prog.getFacts(pred).contains(BASIC.createTuple(CONCRETE.createIri("http://deri.org/l#long"))));
		// asserting the bool
		pred = BASIC.createPredicate("bool", 1);
		assertTrue("Could not find the short bool", prog.getFacts(pred).contains(BASIC.createTuple(CONCRETE.createBoolean(false))));
		// asserting the double
		pred = BASIC.createPredicate("double", 1);
		assertTrue("Could not find the double", prog.getFacts(pred).contains(BASIC.createTuple(CONCRETE.createDouble(4.67))));
		// asserting the float
		pred = BASIC.createPredicate("float", 1);
		assertTrue("Could not find the float", prog.getFacts(pred).contains(BASIC.createTuple(CONCRETE.createFloat(4.67f))));
		// asserting the date
		pred = BASIC.createPredicate("date", 1);
		assertTrue("Could not find the date", prog.getFacts(pred).contains(BASIC.createTuple(CONCRETE.createDate(2007, 2, 6))));
		// asserting the date with timezone
		pred = BASIC.createPredicate("datetz", 1);
		assertTrue("Could not find the date with timezone", prog.getFacts(pred).contains(BASIC.createTuple(CONCRETE.createDate(2007, 2, 6, 2, 30))));
		// asserting the duration
		pred = BASIC.createPredicate("duration", 1);
		assertTrue("Could not find the duration", prog.getFacts(pred).contains(BASIC.createTuple(CONCRETE.createDuration(2007, 2, 6, 12, 45, 11))));
		// asserting the duration with milliseconds
		pred = BASIC.createPredicate("durationms", 1);
		assertTrue("Could not find the duration with milliseconds", 
				prog.getFacts(pred).contains(BASIC.createTuple(CONCRETE.createDuration(2007, 2, 6, 12, 45, 11, 500))));
		// asserting the short datetime
		pred = BASIC.createPredicate("datetimes", 1);
		assertTrue("Could not find the short datetime", prog.getFacts(pred).contains(BASIC.createTuple(CONCRETE.createDateTime(2007, 2, 6, 12, 45, 11))));
		// asserting the long datetime
		pred = BASIC.createPredicate("datetimel", 1);
		assertTrue("Could not find the long datetime", prog.getFacts(pred).contains(BASIC.createTuple(CONCRETE.createDateTime(2007, 2, 6, 12, 45, 11, 1, 30))));
		// asserting the long datetime with milliseconds
		pred = BASIC.createPredicate("datetimelms", 1);
		assertTrue("Could not find the long datetime with milliseconds", 
				prog.getFacts(pred).contains(BASIC.createTuple(CONCRETE.createDateTime(2007, 2, 6, 12, 45, 11, 500, 1, 30))));
		// asserting the short time
		pred = BASIC.createPredicate("times", 1);
		assertTrue("Could not find the short time", prog.getFacts(pred).contains(BASIC.createTuple(CONCRETE.createTime(12, 45, 11))));
		// asserting the long time
		pred = BASIC.createPredicate("timel", 1);
		assertTrue("Could not find the long time", prog.getFacts(pred).contains(BASIC.createTuple(CONCRETE.createTime(12, 45, 11, 1, 30))));
		// asserting the long time with milliseconds
		pred = BASIC.createPredicate("timelms", 1);
		assertTrue("Could not find the long time", prog.getFacts(pred).contains(BASIC.createTuple(CONCRETE.createTime(12, 45, 11, 500, 1, 30))));
		// asserting the gday
		pred = BASIC.createPredicate("gday", 1);
		assertTrue("Could not find the gday", prog.getFacts(pred).contains(BASIC.createTuple(CONCRETE.createGDay(6))));
		// asserting the gmonth
		pred = BASIC.createPredicate("gmonth", 1);
		assertTrue("Could not find the gmonth", prog.getFacts(pred).contains(BASIC.createTuple(CONCRETE.createGMonth(2))));
		// asserting the gyear
		pred = BASIC.createPredicate("gyear", 1);
		assertTrue("Could not find the gyear", prog.getFacts(pred).contains(BASIC.createTuple(CONCRETE.createGYear(2007))));
		// asserting the gmonthday
		pred = BASIC.createPredicate("gmonthday", 1);
		assertTrue("Could not find the gmonthday", prog.getFacts(pred).contains(BASIC.createTuple(CONCRETE.createGMonthDay(2, 6))));
		// asserting the gyearmonth
		pred = BASIC.createPredicate("gyearmonth", 1);
		assertTrue("Could not find the gyearmonth", prog.getFacts(pred).contains(BASIC.createTuple(CONCRETE.createGYearMonth(2007, 2))));
		// asserting the base64 binary
		pred = BASIC.createPredicate("base", 1);
		assertTrue("Could not find the base64binary", prog.getFacts(pred).contains(BASIC.createTuple(CONCRETE.createBase64Binary("45df"))));
		// asserting the hex bin
		pred = BASIC.createPredicate("hex", 1);
		assertTrue("Could not find the hex", prog.getFacts(pred).contains(BASIC.createTuple(CONCRETE.createHexBinary("a1df"))));
	}

	public void testParseBinaryBuiltins() throws Exception {
		final String toParse = "x(?X) :- \n" +
			"1 < 2, \n" + 
			"3 <= 4, \n" + 
			"5 > 6, \n" + 
			"7 >= 8, \n" + 
			"9 = 10, \n" + 
			"11 != 12.";
		Parser.parse(toParse, prog);
		final Collection<ILiteral> body = prog.getRules().iterator().next().getBody().getLiterals();
		assertTrue("Can't find '1 < 2' in " + body, body.contains(
					BASIC.createLiteral(true, BUILTIN.createLess(CONCRETE.createInteger(1), CONCRETE.createInteger(2)))));
		assertTrue("Can't find '3 <= 4' in " + body, body.contains(
					BASIC.createLiteral(true, BUILTIN.createLessEqual(CONCRETE.createInteger(3), CONCRETE.createInteger(4)))));
		assertTrue("Can't find '5 > 6' in " + body, body.contains(
					BASIC.createLiteral(true, BUILTIN.createGreater(CONCRETE.createInteger(5), CONCRETE.createInteger(6)))));
		assertTrue("Can't find '7 >= 8' in " + body, body.contains(
					BASIC.createLiteral(true, BUILTIN.createGreaterEqual(CONCRETE.createInteger(7), CONCRETE.createInteger(8)))));
		assertTrue("Can't find '9 = 10' in " + body, body.contains(
					BASIC.createLiteral(true, BUILTIN.createEqual(CONCRETE.createInteger(9), CONCRETE.createInteger(10)))));
		assertTrue("Can't find '11 != 12' in " + body, body.contains(
					BASIC.createLiteral(true, BUILTIN.createUnequal(CONCRETE.createInteger(11), CONCRETE.createInteger(12)))));
	}

	public void testParseTenaryBuiltins() throws Exception {
		final String toParse = "x(?X) :- \n" +
			"1 + 2 = 3, \n" + 
			"4 - 5 = 6, \n" + 
			"7 * 8 = 9, \n" + 
			"10 / 11 = 12.";
		Parser.parse(toParse, prog);
		final Collection<ILiteral> body = prog.getRules().iterator().next().getBody().getLiterals();
		assertTrue("Can't find '1 + 2 = 3' in " + body, body.contains(
					BASIC.createLiteral(true, BUILTIN.createAddBuiltin(
							CONCRETE.createInteger(1), CONCRETE.createInteger(2), CONCRETE.createInteger(3)))));
		assertTrue("Can't find '4 - 5 = 6' in " + body, body.contains(
					BASIC.createLiteral(true, BUILTIN.createSubtractBuiltin(
							CONCRETE.createInteger(4), CONCRETE.createInteger(5), CONCRETE.createInteger(6)))));
		assertTrue("Can't find '7 * 8 = 9' in " + body, body.contains(
					BASIC.createLiteral(true, BUILTIN.createMultiplyBuiltin(
							CONCRETE.createInteger(7), CONCRETE.createInteger(8), CONCRETE.createInteger(9)))));
		assertTrue("Can't find '10 / 11 = 12' in " + body, body.contains(
					BASIC.createLiteral(true, BUILTIN.createDivideBuiltin(
							CONCRETE.createInteger(10), CONCRETE.createInteger(11), CONCRETE.createInteger(12)))));
	}
	
	/**
	 * Test that the parsing of negated built-ins works as expected.
	 * @throws Exception
	 */
	public void testNegatedPredicateAndBuiltinEquivalence() throws Exception
	{
		String program1 = "p(?X, ?Y) :- q(?X), not LESS( ?X, ?Y ), not ADD( ?X, ?Y, 3 ).";
		String program2 = "p(?X, ?Y) :- q(?X), not ?X < ?Y, not ?X +?Y = 3.";
		
		IProgram prog1 = Factory.PROGRAM.createProgram();
		IProgram prog2 = Factory.PROGRAM.createProgram();

		Parser.parse( program1, prog1 );
		Parser.parse( program2, prog2 );
		
		assertEquals( prog1.getRules(), prog2.getRules() );
	}

	/**
	 * Checks whether two collections contains the same elements. The size of the collecions will be asserted, too.
	 * @param c0 the reference collection
	 * @param c1 the collection to check
	 * @throws NullPointerException if one collection is <code>null</code>
	 */
	private static void assertCol(final Collection<? extends Object> c0, final Collection<? extends Object> c1) {
		if((c0 == null) || (c1 == null)) {
			throw new NullPointerException("The collections must not be null");
		}
		assertEquals("The sizes of the collections must be equal", c0.size(), c1.size());
		for(final Object o : c0) {
			assertTrue("Couldn't find the term: " + o + " in " + c1, c1.contains(o));
		}
	}
}
