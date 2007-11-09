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
package org.deri.iris.rules;

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.TERM;
import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.builtins.EqualBuiltin;

public class LocalStratifierTest extends TestCase
{
	final List<IRule> mRules = new ArrayList<IRule>();
	
	
	@Override
    protected void setUp() throws Exception
    {
		
    }

	@Override
    protected void tearDown() throws Exception
    {
		mRules.clear();
    }


	public void testInit()
	{
		List<ILiteral> head = new ArrayList<ILiteral>();
		
		// p(X,Y)
		ILiteral literal = BASIC.createLiteral(true, 
				BASIC.createAtom(
					BASIC.createPredicate("p", 2), 
					BASIC.createTuple(
						TERM.createVariable( "X" ),
						TERM.createVariable( "Y" )
					) ) );
		head.add(literal);

		// r(X)
		List<ILiteral> body = new ArrayList<ILiteral>();

		literal = BASIC.createLiteral(true, 
				BASIC.createAtom(
					BASIC.createPredicate("r", 1),
					BASIC.createTuple(
						TERM.createVariable( "X" )
					)));
		body.add(literal);

		// not p(Z,Y)
		literal = BASIC.createLiteral(false, 
				BASIC.createAtom(
					BASIC.createPredicate("p", 2), 
					BASIC.createTuple(
						TERM.createVariable( "Z" ),
						TERM.createVariable( "Y" )
						)));
		body.add(literal);

		// X=b
		literal = BASIC.createLiteral(true,
					new EqualBuiltin(
									TERM.createVariable( "X" ),
									TERM.createString( "b" )
									)
									);
		body.add(literal);
		
		// Z=a
		literal = BASIC.createLiteral(true,
						new EqualBuiltin(
										TERM.createVariable( "Z" ),
										TERM.createString( "a" )
										)
										);
		body.add(literal);
		
		IRule rule = BASIC.createRule( head, body );
		
		//LocalStratifier ls = new LocalStratifier
		RuleManipulator rm = new RuleManipulator();
		
		IRule rule2 = rm.replaceVariablesWithConstants( rule );
		
		IRule rule3 = rm.removeUnnecessaryEqualityBuiltins( rule2 );

		System.out.println( rule );
		System.out.println( "Re-written to:" );
		System.out.println( rule2 );
		System.out.println( "After removing redundant equalities:" );
		System.out.println( rule3 );

		IRule rule4 = rm.addEquality( rule3, TERM.createVariable( "Y" ), TERM.createString( "c" ) );
		System.out.println( "After substituting Y=c:" );
		System.out.println( rule4 );
	}
}
