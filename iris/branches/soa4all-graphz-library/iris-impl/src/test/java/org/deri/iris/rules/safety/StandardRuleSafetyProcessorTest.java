/*
 * Copyright 2011, Semantic Technology Institute (STI) Innsbruck, 
 * University of Innsbruck, Technikerstrasse 21a, 6020 Innsbruck, Austria.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.deri.iris.rules.safety;

import junit.framework.TestCase;

import org.deri.iris.RuleUnsafeException;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.compiler.Parser;
import org.deri.iris.compiler.ParserException;

public class StandardRuleSafetyProcessorTest extends TestCase
{
	StandardRuleSafetyProcessor mProcessor;

	@Override
    protected void setUp() throws Exception
    {
		mProcessor = new StandardRuleSafetyProcessor( true, true );
    }
	
	public void testSimpleSafe() throws Exception
	{
		IRule rule = makeRule( "p(?x) :- q(?x)." );
		
		mProcessor.process( rule );
	}
	
	public void testConstantsInHeadAndEmptyBodySafe() throws Exception
	{
		IRule rule = makeRule( "p(2) :- ." );
		
		mProcessor.process( rule );
	}
	
	public void testConstantsInHeadAndNegationInBodySafe() throws Exception
	{
		IRule rule = makeRule( "p(2) :- not q(3)." );
		
		mProcessor.process( rule );
	}
	
	public void testConstantsInHeadAndNegationWithVariablesInBodySafe() throws Exception
	{
		IRule rule = makeRule( "p(2) :- not q(?x)." );
		
		mProcessor.process( rule );
	}
	
	public void testThroughEqualitySafe() throws Exception
	{
		IRule rule = makeRule( "p(?y) :- q(?x), ?x = ?y." );
		
		mProcessor.process( rule );
	}
	
	public void testThroughInequalityUnSafe() throws Exception
	{
		IRule rule = makeRule( "p(?y) :- q(?x), ?x != ?y." );
		
		checkUnsafe( rule );
	}
	
	public void testArithmeticTernarySafe() throws Exception
	{
		IRule rule = makeRule( "p(?z) :- q(?x, ?y), ?x + ?y = ?z." );
		
		mProcessor.process( rule );
	}
	
	public void testArithmeticTernaryUnSafe() throws Exception
	{
		IRule rule = makeRule( "p(?z) :- q(?x, ?y), ?x + ?y = ?z." );

		mProcessor = new StandardRuleSafetyProcessor( true, false );
		checkUnsafe( rule );
	}
	
	public void testSimpleUnSafe() throws Exception
	{
		IRule rule = makeRule( "p(?x) :- q(?y)." );
		
		checkUnsafe( rule );
	}

	public void testNegatedVariablesSafe() throws Exception
	{
		IRule rule = makeRule( "p(?x) :- q(?x), not r(?y)." );
		
		mProcessor.process( rule );
	}

	public void testNegatedVariablesUnSafe() throws Exception
	{
		IRule rule = makeRule( "p(?x) :- q(?x), not r(?y)." );
		
		mProcessor = new StandardRuleSafetyProcessor( false, true );
		checkUnsafe( rule );
	}

	private void checkUnsafe( IRule rule )
	{
		try
		{
			mProcessor.process( rule );
			fail( "RuleUnsafeException not thrown." );
		}
		catch( RuleUnsafeException e )
		{
		}
	}
	
	private IRule makeRule( String strRule ) throws ParserException
	{
		Parser parser = new Parser();
		parser.parse( strRule );
		
		return parser.getRules().get( 0 );
	}
}
