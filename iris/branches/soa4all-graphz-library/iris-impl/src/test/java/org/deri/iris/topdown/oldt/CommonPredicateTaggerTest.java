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
package org.deri.iris.topdown.oldt;

import java.util.HashSet;

import junit.framework.TestCase;

import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.compiler.Parser;
import org.deri.iris.compiler.ParserException;
import org.deri.iris.evaluation.topdown.CommonPredicateTagger;
import org.deri.iris.factory.Factory;

public class CommonPredicateTaggerTest extends TestCase {

	public void testImmediateRecursion() throws ParserException {
		String program =
		    "s(?X, ?Y) :- s(?X, ?Z), r(?Z, ?Y)." +
		    "s(?X, ?Y) :- r(?X, ?Y)." +
		    
		    "?- s(1, ?Y).";
		
		HashSet<IPredicate> expectedSet = new HashSet<IPredicate>();
		expectedSet.add( Factory.BASIC.createPredicate("r", 2) );
		
		Parser parser = new Parser();
		parser.parse( program );
		
		CommonPredicateTagger tagger = new CommonPredicateTagger(parser.getRules(), parser.getQueries().get(0));
		System.out.println(expectedSet + " " + tagger.getMemoPredicates());
		assertEquals( true , expectedSet.containsAll( tagger.getMemoPredicates() ) );
		assertEquals( expectedSet.size(), tagger.getMemoPredicates().size() );
	}
	
	public void testCommonPredicates() throws ParserException {
		String program =
		    "p(?X) :- a(?X), b(?X), a(?X), b(?X), c(?X), a(?X)." +
		    
		    "?- p(1).";
		
		HashSet<IPredicate> expectedSet = new HashSet<IPredicate>();
		expectedSet.add( Factory.BASIC.createPredicate("a", 1) );
		
		Parser parser = new Parser();
		parser.parse( program );
		
		CommonPredicateTagger tagger = new CommonPredicateTagger(parser.getRules(), parser.getQueries().get(0));
		System.out.println(expectedSet + " " + tagger.getMemoPredicates());
		assertEquals( true , expectedSet.containsAll( tagger.getMemoPredicates() ) );
		assertEquals( expectedSet.size(), tagger.getMemoPredicates().size() );
	}
	
	public void testSiblingExample() throws Exception
	{
		String program =
			"parent( '1a', '2a' )." +
			"parent( '2a', '3a' )." +

			"parent( '1b', '2b' )." +
			"parent( '1b', '2c' )." +

			"parent( '2b', '3b' )." +
			"parent( '2b', '3c' )." +
			"parent( '2c', '3d' )." +
			"parent( '2c', '3e' )." +
			
			"parent( '3b', '4b' )." +
			"parent( '3e', '4e' )." +

			"sibling(?X,?Y) :- parent(?Z,?X), parent(?Z,?Y), ?X != ?Y." +
			"cousin(?X,?Y) :- parent(?XP,?X), parent(?YP,?Y), sibling(?XP,?YP).\n" +
			"cousin(?X,?Y) :- parent(?XP,?X), parent(?YP,?Y), cousin(?XP,?YP).\n" +

			"?- cousin(?X,?Y).\n";
		
		HashSet<IPredicate> expectedSet = new HashSet<IPredicate>();
		expectedSet.add( Factory.BASIC.createPredicate("parent", 2) );
		
		Parser parser = new Parser();
		parser.parse( program );
		
		CommonPredicateTagger tagger = new CommonPredicateTagger(parser.getRules(), parser.getQueries().get(0));
		System.out.println(expectedSet + " " + tagger.getMemoPredicates());
		assertEquals( true , expectedSet.containsAll( tagger.getMemoPredicates() ) );
		assertEquals( expectedSet.size(), tagger.getMemoPredicates().size() );
	}
	
}
