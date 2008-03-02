/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions.
 * 
 * Copyright (C) 2008 Semantic Technology Institute (STI) Innsbruck, 
 * University of Innsbruck, Technikerstrasse 21a, 6020 Innsbruck, Austria.
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
import static org.deri.iris.factory.Factory.CONCRETE;
import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.builtins.EqualBuiltin;
import org.deri.iris.rules.RuleManipulator;

public class RuleManipulatorTest extends TestCase
{
	ILiteral createLiteral( boolean positive, String predicateName, Object ... termObjects )
	{
		List<ITerm> terms = new ArrayList<ITerm>();

		for( Object o : termObjects )
		{
			if( o instanceof Integer )
				terms.add( CONCRETE.createInteger( (Integer) o ) );
			else if( o instanceof String )
				terms.add( TERM.createVariable( (String) o ) );
		}
		
		if( predicateName.equals( "=" ) )
			return BASIC.createLiteral( positive,
							new EqualBuiltin( terms.toArray( new ITerm[ 2 ] ) ) );
		
		return BASIC.createLiteral( positive, 
									BASIC.createAtom(
										BASIC.createPredicate( predicateName, terms.size() ), 
										BASIC.createTuple( terms )
								)
							);
	}

	public void testAll()
	{
		List<ILiteral> head = new ArrayList<ILiteral>();
		
		// p(X,Y)
		head.add( createLiteral( true, "p", "X", "Y" ) );

		// r(X)
		List<ILiteral> body = new ArrayList<ILiteral>();

		body.add( createLiteral( true, "r", "X" ) );

		// not p(Z,Y)
		body.add( createLiteral( false, "p", "Z", "Y" ) );

		// X=2
		body.add( createLiteral( true, "=", "X", 2 ) );
		
		// Z=1
		body.add( createLiteral( true, "=", "Z", 1 ) );
		
		IRule rule = BASIC.createRule( head, body );
		
		//LocalStratifier ls = new LocalStratifier
		RuleManipulator rm = new RuleManipulator();
		
		IRule rule2 = rm.replaceVariablesWithConstants( rule, true );
		IRule rule3 = rm.removeUnnecessaryEqualityBuiltins( rule2 );

		IRule rule4 = rm.addEquality( rule3, TERM.createVariable( "Y" ), CONCRETE.createInteger( 3 ) );

		IRule rule5 = rm.replaceVariablesWithConstants( rule4, false );
		IRule rule6 = rm.removeUnnecessaryEqualityBuiltins( rule5 );

		ILiteral h1 = rule6.getHead().get( 0 );
		assertEquals( h1, createLiteral( true, "p", 2, 3 ) );
		
		ILiteral b1 = rule6.getBody().get( 0 );
		assertEquals( b1, createLiteral( true, "r", 2 ) );

		ILiteral b2 = rule6.getBody().get( 1 );
		assertEquals( b2, createLiteral( false, "p", 1, 3 ) );

		IRule rule7 = rm.addBodyLiteral( rule6, createLiteral( false, "p", 1, 3 ) );
		IRule rule8 = rm.removeDuplicateLiterals( rule7 );
		
		assertEquals( rule8, rule6 );
	}
}
