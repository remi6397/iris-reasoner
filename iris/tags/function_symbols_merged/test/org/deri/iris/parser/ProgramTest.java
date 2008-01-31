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
import org.deri.iris.api.evaluation_old.algebra.IExpressionEvaluator;
import org.deri.iris.api.storage_old.IMixedDatatypeRelation;
import org.deri.iris.compiler.Parser;
import org.deri.iris.evaluation_old.algebra.ExpressionEvaluator;
import org.deri.iris.factory.Factory;

/**
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date   26.01.2006 09:58:07
 */
public class ProgramTest extends TestCase {

	private Map<IPredicate,IMixedDatatypeRelation> m = null;
	
	/** 0 as an argument means: "run all tests" */
	private static final int TEST_ALL = 0;
 
	/** Set a number of a test to be run */
	private int TEST_NO = 6;
	
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
	
	/*public void test3() throws Exception{
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
    }*/
	
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
    		"in('europe')." +
		    "in('ireland').";
		    
		return result;
	}
	
	public void test6()throws Exception{
		if(TEST_NO == 6 || TEST_ALL == 0){
			
		long t0_start = System.currentTimeMillis();
			
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
			    //"?- rsg(?X, ?Y).";
			    "?- rsg(?X, 'f').";
	    	
	    	m = evluateProgram(program);
	    	System.out.println("test 6");
	    	printResults(m);
	    	System.out.println();
	
	    	testProgram(
					m,
					resultTest6());
	    	
	    	long t0_end = System.currentTimeMillis();
	        long t0 = t0_end - t0_start;
	        System.out.println("(" + t0 + "ms)");
		}
    }
	
	public String resultTest6(){
    	String result = 
		    /*"rsg('a','b')." +
		    "rsg('a','c')." +
		    "rsg('a','d')." +
		    "rsg('f','k')." +
		    "rsg('g','f')." +
		    "rsg('h','f')." +
		    "rsg('i','f')." +
		    "rsg('j','f')." +
		    "rsg('m','n')." +
		    "rsg('m','o')." +
		    "rsg('p','m').";*/
		    
    		"rsg('g')." +
		    "rsg('h')." +
		    "rsg('i')." +
		    "rsg('j').";
    	
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
	    	System.out.println("test " + TEST_NO);
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
	
	/*public void test12() throws Exception{
		if(TEST_NO == 12 || TEST_ALL == 0){
			String program = 
	   		
	 			"s('d')." +
			    "s('b')." +
			    "s('a')." +
			    
			    "p('b')." +
			    "p('d')." +
			    
			    
			    // Inequality built-in:
			    "w(?X) :- s(?X), p(?X), ?X != 'd'." +
			    "?- w(?X).";
	   	
	    	m = evluateProgram(program);
	    	System.out.println("test 12");
	    	printResults(m);
	    	System.out.println();
	
	    	testProgram(
					m,
					resultTest12());
		}
    }	
	public String resultTest12(){
    	String result = 
		    "w('b').";
		    
    	return result;
    }*/
	
	/*public void test13() throws Exception{
		if(TEST_NO == 13 || TEST_ALL == 0){
			String program = 
	   		
				"s(1)." +
				"s(2)." +
				"s(3)." +
				
				"p(2)." +
				"p(3)." +
				
			    // Less built-in:
			    "w(?X) :- s(?X), p(?Y), ?X < ?Y." +
			    "?- w(?X).";
	   	
	    	m = evluateProgram(program);
	    	System.out.println("test 13");
	    	printResults(m);
	    	System.out.println();
	
	    	testProgram(
					m,
					resultTest13());
		}
    }
	public String resultTest13(){
    	String result = 
		    "w(1)." +
		    "w(2).";
		    
    	return result;
    }*/
	
	/*public void test14() throws Exception{
		if(TEST_NO == 14 || TEST_ALL == 0){
			String program = 
	   		
				"s(1)." +
				
				"p(2)." +
				"p(3)." +
				
			    // Less add-in:
			    "w(?X,?Z) :- s(?X), p(?Y), ?X + ?Y = ?Z." +
			    "?- w(?X,?Z).";
	   	
	    	m = evluateProgram(program);
	    	System.out.println("test 14");
	    	printResults(m);
	    	System.out.println();
	
	    	testProgram(
					m,
					resultTest14());
		}
    }
	public String resultTest14(){
    	String result = 
		    "w(1, 3)." +
		    "w(1, 4).";
		    
    	return result;
    }*/
	
	public void test15() throws Exception{
		if(TEST_NO == 15 || TEST_ALL == 0){
			String program = 
	   		
				"s(1)." +
				
				"p(2)." +
				"p(3)." +
				
			    "w(?X,?Y) :- s(?X), p(?Y)." +
			    "?- w(?X,?Y).";
	   	
	    	m = evluateProgram(program);
	    	System.out.println("test " + TEST_NO);
	    	printResults(m);
	    	System.out.println();
	
	    	testProgram(
					m,
					resultTest15());
		}
    }
	
	public String resultTest15(){
    	String result = 
		    "w(1, 2)." +
		    "w(1, 3).";
		    
    	return result;
    }
	
	public void test16() throws Exception{
		if(TEST_NO == 16 || TEST_ALL == 0){
			String program = 
	   		
				"s(1)." +
				
				// If we had p(2,9) instead of p(2,2), we would get no result!
				// Is this OK?
				"p(2,2)." +
				"p(3,9)." +
				
				"r(9)." +
				
			    // Unsafe rule: 
				//"w(?X,?Y) :- s(?X), not p(2,?Y)." +
				// Safe rule:
				"w(?X,?Y) :- s(?X), r(?Y), not p(2,?Y)." +
			    "?- w(?X,?Y).";
	   	
	    	m = evluateProgram(program);
	    	System.out.println("test " + TEST_NO);
	    	printResults(m);
	    	System.out.println();
	
	    	testProgram(
					m,
					resultTest16());
		}
    }
	
	public String resultTest16(){
    	String result = 
		    //"w(1, 3).";
    		"w(1, 9).";
    	
    	return result;
    }
	
	public Map<IPredicate, IMixedDatatypeRelation> evluateProgram(String program) 
		throws Exception{
    	
    	IProgram p = Factory.PROGRAM.createProgram();
    	p.resetProgram();
    	Parser.parse(program, p);
   		IExpressionEvaluator method = new ExpressionEvaluator();
		IExecutor exec = new Executor(p, method);
		exec.execute();
		return exec.computeSubstitutions();
    }
	
	public void testProgram(Map<IPredicate, IMixedDatatypeRelation> res1, String res0)
		throws Exception{
    	
		Map<IPredicate, IMixedDatatypeRelation> f = new HashMap<IPredicate, IMixedDatatypeRelation>();
		Set<IRule> r = new HashSet<IRule>();
		Set<IQuery> q = new HashSet<IQuery>();
		
    	IProgram p = Factory.PROGRAM.createProgram(f, r, q);
    	Parser.parse(res0, p);
    	
   		IMixedDatatypeRelation r0 = null;
		IMixedDatatypeRelation r1 = null;
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
	
	public static void printResults(Map<IPredicate, IMixedDatatypeRelation> m){
    	for(IPredicate pr : m.keySet()){
			System.out.println(pr.toString());
			for(ITuple t : m.get(pr)){
				System.out.println(t.toString());
			}
    	}
    }
	
	public static void printResults(IMixedDatatypeRelation r){
    	Iterator<ITuple> i = r.iterator();
		while(i.hasNext()){
			System.out.println(i.next());
		}
    }
	
	public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(ProgramTest.class);
    }
}
