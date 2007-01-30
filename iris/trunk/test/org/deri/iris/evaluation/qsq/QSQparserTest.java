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

import org.deri.iris.api.IProgram;
import org.deri.iris.api.evaluation.common.IAdornedProgram;
import org.deri.iris.compiler.Parser;
import org.deri.iris.compiler.ParserImpl;
import org.deri.iris.evaluation.common.AdornedProgram;
import org.deri.iris.factory.Factory;

/**
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date   16.01.2006 17:51:07
 */

public class QSQparserTest {
	public static void main(String[] args) throws Exception {
		QSQparserTest i = new QSQparserTest();
      
		//i.evluateAndPrint(i.test1());
		i.evluateAndPrint(i.test2());
		//i.evluateAndPrint(i.test3());
		//i.evluateAndPrint(i.test4());
    }

    public String test1(){
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
    		"?- rsg('a', ?Y).";
    		//"?- rsg(?X, 'b').";
		    //"?- rsg(?X, ?Y).";
   
    	return program;
    }
    
    public String test2() throws Exception{
		String program = 
		    "edge('a', 'b')." +
		    "path('b', 'c')." +
		    "edge('c', 'd')." +
		    
		    "path(?X, ?Y) :- edge(?X, ?Y)." +	
		    "path(?X, ?Y) :- path(?X, ?Z), path(?Z, ?Y)." +
		    "?- path('b', ?Y).";
		
		return program;
    }
    
    public String test3() throws Exception{
    	String program = 
		    "edge('a', 'b')." +
		    "path('b', 'c')." +
		    "edge('c', 'd')." +
		    
		    "path(?X, ?Y) :- edge(?X, ?Y)." +
		    "edge(?X, ?Y) :- path(?X, ?Y)." +	
		    "path(?X, ?Y) :- edge(?X, ?Z), path(?Z, ?Y)." +
		    "?- path(?X, 'd').";
		
		return program;
    }
    
    public String test4() throws Exception{
    	String program = 
		    "in('galway', 'ireland')." +
		    "in('dublin', 'ireland')." +
		    "in('innsbruck', 'austria')." +
		    "in('ireland', 'europe')." +
		    "in('austria', 'europe')." +
		    
		    "in(?X, ?Z) :- in(?X, ?Y), in(?Y, ?Z)." +
		    "?- in('galway', ?Z).";
		    //"?- in(?X, 'europe').";
		
		return program;
    }
    
    private void evluateAndPrint(String program) throws Exception{
    	Parser pa = new ParserImpl();
    	IProgram p = Factory.PROGRAM.createProgram();
    	pa.compileKB(program, p);
    	
    	IAdornedProgram adPrg = 
    		new AdornedProgram(p.getRules(), p.getQueries().iterator().next());
    	QSQTemplate qsqTemplate = new QSQTemplate(adPrg);
    	
		qsqTemplate.getQSQTemplate();
		System.out.println(qsqTemplate);
		
    	QSQEvaluator evaluator = new QSQEvaluator(p);
		evaluator.evaluate();
    }
}