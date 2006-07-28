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

import org.deri.iris.compiler.*;
import org.deri.iris.api.IEDB;
import org.deri.iris.factory.*;
import java.util.*;
import org.deri.iris.api.basics.*;
/**
 * @author Francisco Garcia
 */
public class UseParser {
	public static void main(String[] args) throws Exception {
        UseParser i = new UseParser();
        i.test();
    }

    public void test() throws Exception{
    	org.deri.iris.compiler.Parser pa = new ParserImpl();
    	IEDB p = Factory.PROGRAM.createEDB();
    	
    	String transmission = "rule(?x, ?y) :- r(?x, func('paco',?l), f(?y, ff(?x))), q(?y, 'deri'). " + 
    	"?- query(?x, 'deri'). " + 
    	"fact('paco'). ";
    	
		
    	pa.compileKB(transmission, p);
		// * Results
		System.out.println("These are the results for the input: \n" + transmission + "\n\n");
		Set<IRule> rules = p.getRules();
		Iterator<IRule> it = rules.iterator();
		System.out.println("Rules:\n");
		while (it.hasNext())
		{
			IRule r = it.next();
			System.out.println(r.toString() + "\n");

		}
		Set<IAtom> facts = p.getFacts();
		Iterator<IAtom> itf = facts.iterator();
		System.out.println("Facts:\n");
		while (itf.hasNext())
		{
			IAtom f = itf.next();
			System.out.println(f.toString() + "\n");

		}
		System.out.println("Queries:\n");
		
		Iterator qIt = p.queryIterator();
		while (qIt.hasNext())
			System.out.println(((IQuery)qIt.next()).toString());
	//	IQuery q =p.queryIterator();
	//	System.out.println(q.toString() + "\n");
    }
}
