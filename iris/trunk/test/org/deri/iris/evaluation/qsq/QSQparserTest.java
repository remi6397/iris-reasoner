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
package org.deri.iris.evaluation.qsq;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.deri.iris.api.IProgram;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.evaluation.common.IAdornedProgram;
import org.deri.iris.api.storage.IMixedDatatypeRelation;
import org.deri.iris.compiler.Parser;
import org.deri.iris.evaluation.common.AdornedProgram;
import org.deri.iris.factory.Factory;

/**
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date   16.01.2006 17:51:07
 */

public class QSQparserTest extends TestCase {
	
	private Map<IPredicate,IMixedDatatypeRelation> m = null;
	
	/** 0 as an argument means: "run all tests" */
	private static final int TEST_ALL = 30;
 
	/** Set a number of a test to be run */
	private int TEST_NO = 3;
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(QSQparserTest.class);
	}
	public static junit.framework.Test suite() {
        return new junit.framework.TestSuite(QSQparserTest.class);
    }

	public void test1() throws Exception{
		if(TEST_NO == 1 || TEST_ALL == 0){
    	String program = 
    		"flat('g', 'f')." +
    		"flat('m', 'n')." +
    		"flat('m', 'o')." +
    		"flat('n', 'k')." +
    		
    		"rsg(?X, ?Z) :- flat(?X, ?Y), flat(?Y, ?Z)." +
    		"?- rsg('m', 'o').";
    		
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
    		"rsg('m','o').";
    	return result;
    }
    
    public void test2() throws Exception{
		if(TEST_NO == 2 || TEST_ALL == 0){
    	String program = 
    		"flat('g', 'f')." +
    		"flat('m', 'n')." +
    		"flat('m', 'o')." +
    		"flat('n', 'k')." +
    		
    		"rsg(?X, ?Z) :- flat(?X, ?Y), flat(?Y, ?Z)." +
    		"?- rsg('m', ?Z).";
    		
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
    		"rsg('k').";
    	return result;
    }
    
    public void test3() throws Exception{
		if(TEST_NO == 3 || TEST_ALL == 0){
    	String program = 
    		"edge('a', 'b')." +
		    //"path('b', 'c')." +
		    "edge('b', 'd')." +
		    
		    //"path(?X, ?Y) :- edge(?X, ?Y)." +	
		    //"path(?X, ?Y) :- path(?X, ?Z), path(?Z, ?Y)." +
		    
		    "path(?X, ?Y) :- edge(?X, ?Z), edge(?Z, ?Y)." +
		    "?- path(?X, ?Y).";
    		//"?- path('a', ?Y).";
    	   
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
    		"path('a','b')." +
		    "path('a','c')." +
		    "path('a','d')." +
		    "path('b','c')." +
		    "path('b','d')." +
		    "path('c','d').";
    	return result;
    }
    
    public void test4() throws Exception{
		if(TEST_NO == 4 || TEST_ALL == 0){
		String program = 
		    "edge('a', 'b')." +
		    "edge('c', 'd')." +
		    "path('b', 'c')." +
		    
		    "path(?X, ?Y) :- edge(?X, ?Y)." +	
		    "path(?X, ?Y) :- path(?X, ?Z), path(?Z, ?Y)." +
		    "?- path('a', ?Y).";
		
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
    		"path('c')." +
    		"path('b')." +
    		"path('d').";
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
    
    public void test6() throws Exception{
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
    		"rsg('g')." +
		    "rsg('h')." +
		    "rsg('i')." +
		    "rsg('j').";
		    
    	return result;
    }
    
    /*
    public void test5() throws Exception{
		if(TEST_NO == 5 || TEST_ALL == 0){
			String program = 
		    "edge('a', 'b')." +
		    "path('b', 'c')." +
		    "edge('c', 'd')." +
		    
		    "edg('w', 'd')." +
		    
		    "edge(?X, ?Y) :- edg(?X, ?Y)." +
		    "path(?X, ?Y) :- edge(?X, ?Y)." +
		    "edge(?X, ?Y) :- path(?X, ?Y)." +	
		    "path(?X, ?Y) :- edge(?X, ?Z), path(?Z, ?Y)." +
		    "?- path(?X, 'd').";
		
			m = evluateProgram(program);
	    	System.out.println("test 4");
	    	printResults(m);
	    	System.out.println();
	    	
	    	testProgram(
					m,
					resultTest4());
		}
    }
    public String resultTest5(){
    	String result = 
    		"in('europe')." +
		    "in('ireland').";
		    
		return result;
	}
    */
    
    public Map<IPredicate, IMixedDatatypeRelation> evluateProgram(String program) throws Exception{
		IProgram p = Factory.PROGRAM.createProgram();
		p.resetProgram();
		Parser.parse(program, p);
		
		IAdornedProgram adPrg = 
    		new AdornedProgram(p.getRules(), p.getQueries().iterator().next());
    	QSQTemplate qsqTemplate = new QSQTemplate(adPrg);
		qsqTemplate.getQSQTemplate();
		
		QSQEvaluator evaluator = new QSQEvaluator(p);
		evaluator.evaluate();
		
		return evaluator.getResultSet().getResults();
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
		Iterator i = r.iterator();
		while(i.hasNext()){
			System.out.println(i.next());
		}
	}
}
