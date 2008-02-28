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
