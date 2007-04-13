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
package org.deri.iris.parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.deri.iris.Executor;
import org.deri.iris.api.IExecutor;
import org.deri.iris.api.IProgram;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.evaluation.algebra.IExpressionEvaluator;
import org.deri.iris.api.storage.IRelation;
import org.deri.iris.compiler.Parser;
import org.deri.iris.evaluation.algebra.ExpressionEvaluator;
import org.deri.iris.factory.Factory;

/**
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date   26.01.2006 09:58:07
 */
public class ProgramTest extends TestCase {

	private Map<IPredicate,IRelation> m = null;
	
	/** 0 as an argument means: "run all tests" */
	private static final int TEST_ALL = 0;
 
	/** Set a number of a test to be run */
	private int TEST_NO = 11;
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(ProgramTest.class);
	}
	
	public void test1() throws Exception{
		if(TEST_NO == 1 || TEST_ALL == 0){
	    	String program = 
			    "k('d', 'e')." +
			    "k('d', 'g')." +
			    "l('d')." +
			    
			    "w(?Y) :- k(?X, ?Y), l(?X)." +
			    "?- w(?X).";
	    	
	    	m = evluateProgram(program);
	    	System.out.println("test 1");
	    	printResults(m);
	    	System.out.println();
	
	    	testProgram(
					m,
					resultTest1());
		}
    }
	public String resultTest1(){
    	String result = 
		    "w('e')." +
		    "w('g').";
		    
    	return result;
    }
	
	public void test2() throws Exception{
		if(TEST_NO == 2 || TEST_ALL == 0){
			String program = 
	 			"s('d')." +
			    "s('b')." +
			    "s('a')." +
			    "s('q')." +
			    
			    "r('d')." +
			    "r('c')." +
			    
			    "p('b')." +
			    "p('e')." +
			    
			    "t('a')." +
			    
			    "q(?X) :- s(?X), not p(?X)." +
			    "p(?X) :- r(?X)." +
			    "r(?X) :- t(?X)." +
			    "?- q(?X).";
	    	
	    	m = evluateProgram(program);
	    	System.out.println("test 2");
	    	printResults(m);
	    	System.out.println();
	
	    	testProgram(
					m,
					resultTest2());
		}
    }
	
	public String resultTest2(){
    	String result = 
		    "q('q').";
		    
    	return result;
    }
	
	public void test3() throws Exception{
		if(TEST_NO == 3 || TEST_ALL == 0){
			String program = 
	   		
	 			"s('d')." +
			    "s('b')." +
			    "s('a')." +
			    
			    "r('d')." +
			    
			    "p('b')." +
			    
			    "p(?X) :- r(?X)." +
			    // TODO: only Equality built-in works currently!
			    "w(?X) :- s(?X), p(?X), ?X='d'." +
			    "?- w(?X).";
	   	
	    	m = evluateProgram(program);
	    	System.out.println("test 3");
	    	printResults(m);
	    	System.out.println();
	
	    	testProgram(
					m,
					resultTest3());
		}
    }
	
	public String resultTest3(){
    	String result = 
		    "w('d').";
		    
    	return result;
    }
	
	public void test4() throws Exception{
		if(TEST_NO == 4 || TEST_ALL == 0){
			String program = 
			    "edge('a', 'b')." +
			    "path('b', 'c')." +
			    "edge('c', 'd')." +
			    
			    "path(?X, ?Y) :- edge(?X, ?Y)." +	
			    "path(?X, ?Y) :- path(?X, ?Z), path(?Z, ?Y)." +
			    "?- path(?X, ?Y).";
	  	
			m = evluateProgram(program);
	    	System.out.println("test 4");
	    	printResults(m);
	    	System.out.println();
	
	    	
	    	testProgram(
					m,
					resultTest4());
		}
	}
	 
	public String resultTest4(){
    	String result = 
		    "path('a','b')." +
		    "path('a','c')." +
		    "path('a','d')." +
		    "path('b','c')." +
		    "path('b','d')." +
		    "path('c','d').";
			    
    	return result;
    }
	 
	public void test5() throws Exception{
		if(TEST_NO == 5 || TEST_ALL == 0){
	    	String program = 
			    "in('galway', 'ireland')." +
			    "in('dublin', 'ireland')." +
			    "in('innsbruck', 'austria')." +
			    "in('ireland', 'europe')." +
			    "in('austria', 'europe')." +
			    
			    "in(?X, ?Z) :- in(?X, ?Y), in(?Y, ?Z)." +
			    "?- in('galway', ?Z).";
			
	    	m = evluateProgram(program);
	    	System.out.println("test 5");
	    	printResults(m);
	    	System.out.println();
	   
	    	testProgram(
					m,
					resultTest5());
		}
    }
	
	public String resultTest5(){
    	String result = 
    		"in('galway','europe')." +
		    "in('galway', 'ireland').";
		    
		return result;
	}
	
	public void test6()throws Exception{
		if(TEST_NO == 6 || TEST_ALL == 0){
	    	String program = 
	    		"down('g', 'b')." +
	    		"down('h', 'c')." +
	    		"down('i', 'd')." +
	    		"down('l', 'f')." +
	    		"down('m', 'f')." +
	    		"down('p', 'k')." +
	    		
	    		"flat('g', 'f')." +
	    		"flat('m', 'n')." +
	    		"flat('m', 'o')." +
	    		"flat('p', 'm')." +
	    		
	    		"up('a', 'e')." +
	    		"up('a', 'f')." +
	    		"up('f', 'm')." +
	    		"up('g', 'n')." +
	    		"up('h', 'n')." +
	    		"up('i', 'o')." +
	    		"up('j', 'o')." +
	    		
			    "rsg(?X, ?Y) :- up(?X, ?W), rsg(?Q, ?W), down(?Q, ?Y)." +
			    "rsg(?X, ?Y) :- flat(?X, ?Y)." +
			    "?- rsg(?X, ?Y).";
			   
	    	m = evluateProgram(program);
	    	System.out.println("test 6");
	    	printResults(m);
	    	System.out.println();
	
	    	testProgram(
					m,
					resultTest6());
		}
    }
	
	public String resultTest6(){
    	String result = 
		    "rsg('a','b')." +
		    "rsg('a','c')." +
		    "rsg('a','d')." +
		    "rsg('f','k')." +
		    "rsg('g','f')." +
		    "rsg('h','f')." +
		    "rsg('i','f')." +
		    "rsg('j','f')." +
		    "rsg('m','n')." +
		    "rsg('m','o')." +
		    "rsg('p','m').";
		    
		return result;
	}
	
	public void test7()throws Exception{
		if(TEST_NO == 7 || TEST_ALL == 0){
			String program = 
			    "edge('a', 'b')." +
			    "path('b', 'c')." +
			    "edge('c', 'd')." +
			    
			    "path(?X, ?Y) :- edge(?X, ?Y)." +
			    "edge(?X, ?Y) :- path(?X, ?Y)." +	
			    "path(?X, ?Y) :- edge(?X, ?Z), path(?Z, ?Y)." +
			    "?- path(?X, ?Y).";
	   
			m = evluateProgram(program);
	    	System.out.println("test 7");
	    	printResults(m);
	    	System.out.println();
	
	    	testProgram(
					m,
					resultTest7());
		}
    }
	
	public String resultTest7(){
    	String result = 
		    "path('a','b')." +
		    "path('a','c')." +
		    "path('a','d')." +
		    "path('b','c')." +
		    "path('b','d')." +
		    "path('c','d').";
		    
		return result;
	}
	
	public void test8()throws Exception{
		if(TEST_NO == 8 || TEST_ALL == 0){
	    	String program = 
			    "r('a', 'a')." +
			    "r('b', 'c')." +
			    "r('c', 'd')." +
			    
			    "p(?X, 'a') :- r(?X, ?Y)." +
			    "?- p(?X, ?Y).";
	  	
	    	m = evluateProgram(program);
	    	System.out.println("test 8");
	    	printResults(m);
	    	System.out.println();
	    	
	    	testProgram(
					m,
					resultTest8());
		}
    }
	
	public String resultTest8(){
    	String result = 
		    "p('a','a')." +
		    "p('b','a')." +
		    "p('c','a').";
		    
		return result;
	}
	
	public void test9()throws Exception{
		if(TEST_NO == 9 || TEST_ALL == 0){
	    	String program = 
	 			"u('d')." +
			    "u('b')." +
			    "u('a')." +
			    "u('q')." +
			    
			    "s('d')." +
			    "s('c')." +
			    
			    "p('b')." +
			    "p('e')." +
			    
			    "q('a')." +
			    
			    "p(?X) :- q(?X), not r(?X)." +
			    "r(?X) :- s(?X), not t(?X)." +
			    "t(?X) :- u(?X)." +
			    "?- p(?X).";
	  	
	    	m = evluateProgram(program);
	    	System.out.println("test 9");
	    	printResults(m);
	    	System.out.println();
	
	    	testProgram(
					m,
					resultTest9());
		}
    }
	
	public String resultTest9(){
    	String result = 
		    "p('a')." +
		    "p('b')." +
		    "p('e').";
		    
		return result;
	}
	
	public void test10()throws Exception{
		if(TEST_NO == 10 || TEST_ALL == 0){
	    	String program = 
			    "r('a', 'a')." +
			    "r('b', 'a')." +
			    
			    "s('a', 'a')." +
			    "s('b', 'b')." +
			    
			    "p(?X) :- not s(?X, 'a'), r(?X, ?Y)." +
			    "?- p(?X).";
	  	
	    	m = evluateProgram(program);
	    	System.out.println("test 10");
	    	printResults(m);
	    	System.out.println();
	    	
	    	testProgram(
					m,
					resultTest10());
		}
	}
	
	public String resultTest10(){
    	String result = 
		    "p('b').";
		    
		return result;
	}
	
	public void test11()throws Exception{
		if(TEST_NO == 11 || TEST_ALL == 0){
	    	String program = 
			    "s('a', 'a')." +
			    "s('a', 'b')." +
			    "s('a', 'c')." +
			    "s('a', 'd')." +
			    "s('a', 'e')." +
			    "s('a', 'f')." +
			    "s('a', 'g')." +
			    "s('a', 'h')." +
			    "s('a', 'i')." +
			    
			    "s('b', 'a')." +
			    "s('b', 'b')." +
			    "s('b', 'c')." +
			    "s('b', 'd')." +
			    "s('b', 'e')." +
			    "s('b', 'f')." +
			    "s('b', 'g')." +
			    "s('b', 'h')." +
			    "s('b', 'i')." +
			    
			    "s('c', 'a')." +
			    "s('c', 'b')." +
			    "s('c', 'c')." +
			    "s('c', 'd')." +
			    "s('c', 'e')." +
			    "s('c', 'f')." +
			    "s('c', 'g')." +
			    "s('c', 'h')." +
			    "s('c', 'i')." +
			    
			    "s('f', 'f')." +
			    "s('f', 'g')." +
			    "s('f', 'h')." +
			    "s('f', 'i')." +
			    
			    "s('g', 'f')." +
			    "s('g', 'g')." +
			    "s('g', 'h')." +
			    "s('g', 'i')." +
			    
			    "s('h', 'f')." +
			    "s('h', 'g')." +
			    "s('h', 'h')." +
			    "s('h', 'i')." +
			    
			    "p(?X) :- s(?X, ?Y), s(?Y, ?X)." +
			    "?- p(?X).";
	  	
	    	m = evluateProgram(program);
	    	System.out.println("test 11");
	    	printResults(m);
	    	System.out.println();
	    	
	    	testProgram(
					m,
					resultTest11());
		}
	}
	
	public String resultTest11(){
    	String result = 
		    "p('a')." +
		    "p('h')." +
		    "p('f')." +
		    "p('c')." +
		    "p('b')." +
		    "p('g').";
		    
		return result;
	}
	
	public Map<IPredicate, IRelation> evluateProgram(String program) 
		throws Exception{
    	
    	IProgram p = Factory.PROGRAM.createProgram();
    	p.resetProgram();
    	Parser.parse(program, p);
   		IExpressionEvaluator method = new ExpressionEvaluator();
		IExecutor exec = new Executor(p, method);
		exec.execute();
		return exec.computeSubstitutions();
    }
	
	public void testProgram(Map<IPredicate, IRelation> res1, String res0)
		throws Exception{
    	
		Map<IPredicate, IRelation> f = new HashMap<IPredicate, IRelation>();
		Set<IRule> r = new HashSet<IRule>();
		Set<IQuery> q = new HashSet<IQuery>();
		
    	IProgram p = Factory.PROGRAM.createProgram(f, r, q);
    	Parser.parse(res0, p);
    	
   		IRelation r0 = null;
		IRelation r1 = null;
		for(IPredicate pr : p.getPredicates()){
			r0 = p.getFacts().get(pr);
			r1 = res1.get(pr);
			
			assertEquals("The length of relation and the list of"
					+ " expected tuples must be equal", r0.size(), r1.size());
			assertTrue("The relation must contain all expected tuples", 
					r0.containsAll(r1));
			assertTrue("...and vise vers!", 
					r1.containsAll(r0));
    	}
    }
	
	public static void printResults(Map<IPredicate, IRelation> m){
    	for(IPredicate pr : m.keySet()){
			System.out.println(pr.toString());
			for(ITuple t : m.get(pr)){
				System.out.println(t.toString());
			}
    	}
    }
	
	public static void printResults(IRelation r){
    	Iterator i = r.iterator();
		while(i.hasNext()){
			System.out.println(i.next());
		}
    }
	
	public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(ProgramTest.class);
    }
}
