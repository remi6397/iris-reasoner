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
package org.deri.iris.builtins;

import java.util.HashSet;
import java.util.Set;

import org.deri.iris.api.IKnowledgeBase;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.storage.IRelation;

class ExecutionHelper
{
	/**
	 * Eexecutes a program and print results.
	 * 
	 * @param p	A program to be evaluated.
	 */
	public static void executeTest(final IKnowledgeBase p, IQuery q, IRelation res)throws Exception{
		System.out.println("--- input ---");
		for (final IRule rule : p.getRules()) {
			System.out.println(rule);
		}
		System.out.println("--- facts ---");
//		System.out.println(p.keySet());
//		for (final IPredicate pred : p.getPredicates()) {
//			System.out.printf("%s -> %s\n", pred.getPredicateSymbol(), p
//					.getFacts(pred));
//			for (ITuple t : p.getFacts(pred)) {
//				System.out.println(pred.getPredicateSymbol() + t);
//			}
//		}
		
//		IExpressionEvaluator method = new ExpressionEvaluator();
//		IExecutor exec = new Executor(p, method);
//		exec.execute();
		System.out.println("Result: ");
		IRelation actual = p.execute( q );
//		Map<IPredicate, IMixedDatatypeRelation> results = exec.computeSubstitutions();
//		ProgramTest.printResults(results);
		
//		assertTrue(results.get(results.keySet().iterator().next()).containsAll(res));
		junit.framework.Assert.assertTrue(same(res, actual ));
	}
		
	public static boolean same( IRelation actualResults, IRelation expectedResults )
	{
		Set<ITuple> actual = new HashSet<ITuple>();
		Set<ITuple> expected = new HashSet<ITuple>();
		
		for( int t = 0; t < actualResults.size(); ++t )
			actual.add( actualResults.get( t ) );
		
		for( int t = 0; t < expectedResults.size(); ++t )
			expected.add( expectedResults.get( t ) );
		
		return actual.equals( expected );
	}

}
