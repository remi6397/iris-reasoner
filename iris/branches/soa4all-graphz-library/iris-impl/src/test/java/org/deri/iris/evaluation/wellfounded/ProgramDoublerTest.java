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
package org.deri.iris.evaluation.wellfounded;

import java.util.List;

import junit.framework.TestCase;

import org.deri.iris.Configuration;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.compiler.Parser;
import org.deri.iris.factory.Factory;
import org.deri.iris.facts.Facts;
import org.deri.iris.storage.simple.SimpleRelationFactory;

public class ProgramDoublerTest extends TestCase
{
	List<IRule> startingRules;
	
	protected void setUp() throws Exception
	{
		String program =
			"p(?x) :- t(?x, ?y, ?z), not p(?y), not p(?z)." +
			"p('b') :- not r('a')." +
			"t( 'a', 'a', 'b')." +
			"t( 'a', 'b', 'a').";
		Parser parser = new Parser();
		parser.parse( program );
		
		startingRules = parser.getRules();
	}

	public void testStartProgram()
	{
		ProgramDoubler doubler = new ProgramDoubler( startingRules, new Facts( new SimpleRelationFactory() ), new Configuration() );
		
		List<IRule> startingRules = doubler.getStartingRuleBase();
		
		assertEquals( 0, startingRules.size() );
	}

	public void testPositiveProgram()
	{
		ProgramDoubler doubler = new ProgramDoubler( startingRules, new Facts( new SimpleRelationFactory() ), new Configuration() );
		
		List<IRule> positiveRules = doubler.getPositiveRuleBase();
		
		for( IRule rule : positiveRules )
		{
			ILiteral head = rule.getHead().get( 0 );
			assertEquals( true, head.isPositive() );
			assertEquals( Factory.BASIC.createPredicate( "p", 1 ), head.getAtom().getPredicate() );
		}
	}

	public void testNegativeProgram()
	{
		ProgramDoubler doubler = new ProgramDoubler( startingRules, new Facts( new SimpleRelationFactory() ), new Configuration() );
		
		List<IRule> negativeRules = doubler.getNegativeRuleBase();
		
		for( IRule rule : negativeRules )
		{
			ILiteral head = rule.getHead().get( 0 );
			assertEquals( true, head.isPositive() );
			assertEquals( Factory.BASIC.createPredicate( "p" + ProgramDoubler.NEGATED_PREDICATE_SUFFIX, 1 ), head.getAtom().getPredicate() );
		}
	}
}
