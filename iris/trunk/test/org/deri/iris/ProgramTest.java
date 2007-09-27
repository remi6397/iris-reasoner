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

import java.util.ArrayList;
import java.util.List;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.storage.IMixedDatatypeRelation;
import org.deri.iris.api.terms.IStringTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.IProgram;
import org.deri.iris.basics.BasicFactory;
import org.deri.iris.compiler.Parser;
import org.deri.iris.storage.RelationFactory;
import org.deri.iris.terms.TermFactory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * Tests various functionalities of the program.
 * </p>
 * <p>
 * $Id: ProgramTest.java,v 1.4 2007-09-27 14:51:00 bazbishop237 Exp $
 * </p>
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision: 1.4 $
 */
public class ProgramTest extends TestCase {

	public static Test suite() {
		return new TestSuite(ProgramTest.class, ProgramTest.class.getSimpleName());
	}

	/**
	 * Tests whether it is possible to construct two programs which are
	 * independent of each other. Means, that one program should not contain
	 * the other's objects.
	 * @throws Exception 
	 */
	public void testMultiblePrograms() throws Exception {
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
	
	public void test_AddFacts_GetFacts()
	{
		// Build a good list of names
		List<String> predicateNames = new ArrayList<String>();
		
		for ( char n = 'a'; n <= 'z'; ++n )
		{
			String name = "" + n;
			predicateNames.add( name );
		}
		
		final IProgram p = ProgramFactory.getInstance().createProgram();

		final int maxArity = 10;
		int termValue = 1;

		// Test a program with progressively larger predicate arities.
		// Put stuff in...
		for ( int arity = 1; arity <= maxArity; ++arity )
		{
			for ( String name : predicateNames )
			{
				p.addFacts( makePredicate( name, arity ), makeRelation( arity, termValue ) );
				++termValue;
			}
		}

		termValue = 1;

		// ... check we get the same stuff out.
		for ( int arity = 1; arity <= maxArity; ++arity )
		{
			for ( String name : predicateNames )
			{
				IMixedDatatypeRelation r = p.getFacts( makePredicate( name, arity ) );
				
				assertTrue( r != null );
				assertEquals( r.getArity(), arity );
				assertEquals( r.size(), 1 );
				
				IStringTerm strTerm = (IStringTerm) r.first().getTerm( 0 );
				
				assertEquals( Integer.parseInt( strTerm.getValue() ), termValue );

				++termValue;
			}
		}
	}
	
	IMixedDatatypeRelation makeRelation( int arity, int termValue )
	{
		IMixedDatatypeRelation relation = RelationFactory.getInstance().getMixedRelation( arity );

		relation.add( makeTuple( arity, termValue ) );
		
		return relation;
	}
	
	ITuple makeTuple( int arity, int termValue )
	{
		ArrayList<ITerm> terms = new ArrayList<ITerm>();
		
		// Add a collection of string terms that actually contain an integer.
		for ( int i = 0; i < arity; ++i )
			terms.add( TermFactory.getInstance().createString( Integer.toString( termValue ) ) );
		
		return BasicFactory.getInstance().createMinimalTuple( terms );
	}
	
	IPredicate makePredicate( String name, int arity )
	{
		return BasicFactory.getInstance().createPredicate( name, arity );
	}
}