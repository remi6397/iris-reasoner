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

package example;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.deri.iris.api.IProgram;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.compiler.Parser;
import org.deri.iris.compiler.ParserImpl;
import org.deri.iris.evaluation.seminaive.Rule2Relation;
import org.deri.iris.factory.Factory;
/**
 * @author Francisco Garcia
 */
public class UseParser {
	public static void main(String[] args) throws Exception {
        UseParser i = new UseParser();
        i.test();
    }

    public void test() throws Exception{
    	Parser pa = new ParserImpl();
    	IProgram p = Factory.PROGRAM.createProgram();

    	
   	String program = 
   		/* "p(?X,?Y) :- r(?Z, ?Y) and ?X='a'. " + 
    		"p(?X,?Y) :- s(?X, ?Z) and r(?Z, ?Y). " + 
    		"q(?X,?Y) :- p(?X, 'b') and ?X=?Y. " +
    		"q(?X,?Y) :- p(?X, ?Z) and s(?Z, ?Y). ";
    		program +=
    	*/	
   		
   			/*"s(?X,?Y) :- p(?X,?Z) and p(?Y,?Z) and ?X != ?Y. " + 
    		"c(?X,?Y) :- p(?X,?Xp) and p(?Y,?Yp) and s(?Xp,?Yp). " + 
    		"c(?X,?Y) :- p(?X,?Xp) and p(?Y,?Yp) and c(?Xp,?Yp). " +
    		"r(?X,?Y) :- s(?X,?Y). " +
    		"r(?X,?Y) :- r(?X,?Z) and p(?Y,?Z). " +
    		"r(?X,?Y) :- r(?Z,?Y) and p(?X,?Z). ";*/
	
		//  TEST 1
			"p(?X,?Y) :- r(?Z, ?Y) and ?X='a'. " ;
   			
   			//"p(?X,?Y) :- r(?X, ?Y) and ?X!='a'. " ;
	
		   	/*"p(?X,?Y) :- r(?Z, ?Y) and ?X='a'. " + 
			"p(?X,?Y) :- s(?X, ?Z) and r(?Z, ?Y). " ;*/
			
   			//"p(?X,?Y,?Z) :- r(?Z,?Y,?X) and ?X='a' and ?Y='b'. " ;
   			//"p(?X,?Y,?Z) :- ?X='a' and r(?Z,?Y,?X) and ?Y='b'. " ;
   		
   			//"p(?X,?Y,?Z) :- r(?Z,?Y,?X) and s(?X,?Y) and ?X='a' and ?Y='b'. " ;
   			//"p(?X,?Y,?Z) :- r(?Z,?Y,?X) and s(?X,?Y) and ?X!='a' and ?Y='b'. " ;
   			
   			// Doesn't work
   			//"p(?X,?Y,?Z) :- r(?Z,?Y,?X) and s(?X,?Y) and ?X=?Z and ?Y=?Z. " ;
   			
   			// Doesn't work (needs indexes in for of a matrix)
			//"p(?X,?Y,?Z) :- r(?Z,?Y,?X) and ?X=?Z and ?Y!=?Z. " ;
	
   		
   			//"p(?X,?Y,?Z) :- r(?Z,?Y,?X) and ?X!=?Z and ?Y=?Z. " ;
   	
		//  TEST 2
		   	//"s(?X,?Y) :- p(?X,?Z) and r(?Y,?Z).";
		//  TEST 3
   			//"p(?X,?Y) :- r(?X,?Y) and ?X!='a'.";
   			//"p(?X,?Y) :- r(?Z,?Y) and ?X!='a'."; // unsafe rule!
		   	//"p(?X,?Y) :- r(?X,?Y) and ?X!='a' and s(?X,?Y)."; 
   			//"p(?X,?Y) :- r(?X,?Y) and s(?X,?Y) and ?X!='a'.";
   		//  TEST 4	
   			//"q(?X,?Y) :- p(?X,'b') and ?X=?Y."; // this case is needed
		   	//"q(?X,?Y) :- p(?X,'b') and ?X=?Y and r(?X,'b')."; // It works now!
   			//"q(?X,?Y) :- p(?X,?Y) and ?X!=?Y."; // not sure whether we need this case. 
		//  TEST 5	
		   	//"q(?X,?Y) :- p(?X,'b') and ?X=?Y. q(?X,?Y) :- p(?X,?Z) and s(?Z,?Y).";
		//  TEST 6	
		   	//"p(?X, ?Y, ?Z) :- r(?Y, ?Z) and ?X='a'. p(?X, ?Y, ?Z) :- r(?Y, ?X) and ?Z=?X .";
		//  TEST 7	
		   	//"s(?X,?Y) :- p(?X,?Y). s(?X,?Y) :- r(?X,?Y). s(?X,?Y) :- q(?X,?Y).";

	    
    	pa.compileKB(program, p);
		// * Results
		System.out.println("These are the results for the input: \n" + program + "\n\n");
		Set<IRule> rules = p.getRules();
		Iterator<IRule> it = rules.iterator();
		System.out.println("Rules:\n");
		while (it.hasNext()){
			IRule r = it.next();
			System.out.println(r.toString() + "\n");

		}
		System.out.println("Queries:\n");
		
		Iterator qIt = p.queryIterator();
		while (qIt.hasNext())
			System.out.println(((IQuery)qIt.next()).toString());
		

		
	//	IQuery q =p.queryIterator();
	//	System.out.println(q.toString() + "\n");
		Rule2Relation r2r = new Rule2Relation();
		Map result = r2r.evalRule(p.getRules());
		Iterator kIt = result.keySet().iterator();
		System.out.println(result.size());
		while (kIt.hasNext()){
			IPredicate head = 
				(IPredicate)kIt.next();
			System.out.println(head.toString() + "->" + result.get(head).toString());
//			result.get(head);
		}

		
    }
}
