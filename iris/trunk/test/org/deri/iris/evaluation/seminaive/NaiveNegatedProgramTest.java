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

package org.deri.iris.evaluation.seminaive;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.deri.iris.api.IEDB;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.evaluation.IResultSet;
import org.deri.iris.api.evaluation.seminaive.model.ITree;
import org.deri.iris.api.storage.IRelation;
import org.deri.iris.compiler.Parser;
import org.deri.iris.compiler.ParserImpl;
import org.deri.iris.factory.Factory;

/**
 * @author Darko Anicic, DERI Innsbruck
 * @date   08.11.2006 16:38:43
 */
public class NaiveNegatedProgramTest{
	
	public static void main(String[] args) throws Exception {
		NaiveNegatedProgramTest i = new NaiveNegatedProgramTest();
        i.test();
    }

    public void test() throws Exception{
    	Parser pa = new ParserImpl();
    	IEDB p = Factory.PROGRAM.createEDB();
    	
   	String program = 
   		    
   		/**
  		  * Test 1
  		  * 
  		  * q(X) :- s(X), -p(X)
  		  * p(X) :- r(X)
  		  * p(X) :- p(X)
  		  */
  		
  			"s('w')." +
		    "s('b')." +
		    "s('a')." +
		    
		    "r('a')." +
		    "r('c')." +
		    
		    "p('d')." +
		    
		    "q(?X) :- s(?X), not p(?X)." +
		    "p(?X) :- r(?X)." +
		    "p(?X) :- p(?X)." +
		    "?- q(?X).";
   	
   		pa.compileKB(program, p);
		Set<IRule> rules = p.getRules();
		Iterator<IRule> it = rules.iterator();
		System.out.println("Rules:\n");
		while (it.hasNext()){
			IRule r = it.next();
			System.out.println(r.toString() + "\n");
		}
		Map<IPredicate,IRelation> facts = p.getFacts();
		Iterator<IPredicate> itP = facts.keySet().iterator();
		Iterator<ITuple> itF = null; 
		System.out.println("Facts with predicate: ");
		IPredicate pr = null;
		while (itP.hasNext()){
			pr = itP.next();
			System.out.print(pr.toString() + "\n");
			itF = facts.get(pr).iterator();
			while (itF.hasNext()){
				System.out.println(itF.next());
			}	
		}
		System.out.println("\nQueries:");
		
		Iterator qIt = p.queryIterator();
		while (qIt.hasNext())
			System.out.println(((IQuery)qIt.next()).toString());
		
		Rule2Relation r2r = new Rule2Relation();
		Map<IPredicate, ITree> ruleRels = r2r.evalRule(p.getRules());
		Map<IPredicate, ITree> queryRels = r2r.evalQueries(p.queryIterator());
		Iterator kIt = ruleRels.keySet().iterator();
		while (kIt.hasNext()){
			pr = (IPredicate)kIt.next();
			System.out.println(pr.toString() + "->" + ruleRels.get(pr).toString());
		}
		kIt = queryRels.keySet().iterator();
		while (kIt.hasNext()){
			pr = (IPredicate)kIt.next();
			System.out.println(pr.toString() + "->" + queryRels.get(pr).toString());
		}
		
		InMemoryProcedure method = new InMemoryProcedure(ruleRels.get(pr), p);
		NaiveEvaluation evaluator = new NaiveEvaluation(method, p, ruleRels, queryRels);
		//SeminaiveEvaluation evaluator = new SeminaiveEvaluation(method, p, ruleRels, queryRels);
		boolean done = evaluator.evaluate();
		if(done){
			IResultSet rs = evaluator.getResultSet();
			System.out.println("\nResultSet.size: " + rs.getResultNumber());
			Iterator<IPredicate> pi = rs.getResults().keySet().iterator();
			while(pi.hasNext()){
				pr = pi.next();
				qIt = p.queryIterator();
				while (qIt.hasNext()){
					IQuery q = (IQuery)qIt.next();
					if(pr.equals(q.getQueryLiteral(0).getPredicate())){
						System.out.println("Results for predicate " + pr + " : ");
						IRelation tr = rs.getResults().get(pr);
						Iterator ti = tr.iterator();
						while(ti.hasNext()){
							System.out.println(ti.next().toString());
						} 
					}
				}
			}
		}
    }
}
