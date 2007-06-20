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

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.PROGRAM;
import static org.deri.iris.factory.Factory.RELATION;
import static org.deri.iris.factory.Factory.TERM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.deri.iris.api.IProgram;
import org.deri.iris.api.basics.IBody;
import org.deri.iris.api.basics.IHead;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.evaluation.common.IAdornedProgram;
import org.deri.iris.api.storage.IMixedDatatypeRelation;
import org.deri.iris.evaluation.common.AdornedProgram;

/**
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date 15.02.2007 15:226:03
 */
public class QSQTemplateTest {
	static IAdornedProgram program = null;
	static QSQTemplate qsqTemplate = null;
	static IProgram pr = null;
	static QSQEvaluator evaluator = null;
	
	public static void main(final String[] args) {
		setEDB();
		test();
	}
	
	private static void test(){
		qsqTemplate = new QSQTemplate(program);
		qsqTemplate.getQSQTemplate();
		
		System.out.println();
		System.out.println("*** QSQTemplate: ******");
		System.out.println(qsqTemplate);
		
		evaluator = new QSQEvaluator(pr);
		evaluator.evaluate();
	}
	
	private static void setEDB(){
		System.out.println("*** TEST 1 ******");
		
		Set<IRule> rules = new HashSet<IRule>();
		Set<IQuery> queries = new HashSet<IQuery>();
		
		// first rule: 
		// rsg(X, Y) <- flat(X, Y);
		// computing head: first rule
		ILiteral lh = BASIC.createLiteral(true, 
				BASIC.createPredicate("rsg", 2),
				BASIC.createTuple(
						TERM.createVariable("X"), 
						TERM.createVariable("Y")));
		IHead head = BASIC.createHead(lh);
		// computing body: first rule
		ILiteral lb = BASIC.createLiteral(true, 
						BASIC.createPredicate("flat", 2), 
						BASIC.createTuple(
								TERM.createVariable("X"), 
								TERM.createVariable("Y")));
		IBody body = BASIC.createBody(lb);

		rules.add(BASIC.createRule(head, body));

		// second rule: 
		// rsg(X, Y) <- up(X, X1), rsg(Y1, X1), down(Y1, Y);
		// computing head: second rule
		lh = BASIC.createLiteral(true, 
				BASIC.createPredicate("rsg", 2), 
				BASIC.createTuple(
						TERM.createVariable("X"), 
						TERM.createVariable("Y")));
		head = BASIC.createHead(lh);
		
		// computing body: first rule
		List<ILiteral> bodyLiterals = new ArrayList<ILiteral>();
	
		bodyLiterals.add(BASIC.createLiteral(true, 
				BASIC.createPredicate("up",2), 
				BASIC.createTuple(
						TERM.createVariable("X"), 
						TERM.createVariable("X1"))));
		
		bodyLiterals.add(BASIC.createLiteral(true, 
							BASIC.createPredicate("rsg",2), 
							BASIC.createTuple(
									TERM.createVariable("Y1"), 
									TERM.createVariable("X1"))));
		
		bodyLiterals.add(BASIC.createLiteral(true, 
				BASIC.createPredicate("down", 2), 
				BASIC.createTuple(
						TERM.createVariable("Y1"), 
						TERM.createVariable("Y"))));
		
		body = BASIC.createBody(bodyLiterals);

		rules.add(BASIC.createRule(head, body));

		// query:
		// query(Y) <- rsg(a, Y);
		IQuery query = BASIC.createQuery(
							BASIC.createLiteral(true, 
									BASIC.createPredicate("rsg", 2), 
									BASIC.createTuple(
											TERM.createString("f"), 
											//TERM.createVariable("X"),
											TERM.createVariable("Y"))));
											//TERM.createString("b"))));
		queries.add(query);
		
		System.out.println("*** input: ******");
		for (IRule r : rules) {
			System.out.println(r);
		}
		System.out.println(query);
		System.out.println();

		System.out.println("*** output: ******");
		program = new AdornedProgram(rules, query);
		System.out.println(program);
		
		Map<IPredicate, IMixedDatatypeRelation> m = setFacts();
		System.out.println("*** facts: ******");
		Iterator<IPredicate> i = m.keySet().iterator();
		IPredicate p = null;
		IMixedDatatypeRelation rel = null;
		Iterator it = null;
		while(i.hasNext()){
			p = i.next();
			rel = (IMixedDatatypeRelation)m.get(p);
			it = rel.iterator();
			System.out.println("For predicate " + p.toString() + ": ");
			while(it.hasNext()){
				System.out.println(((ITuple)it.next()).toString());
			}
		}
		
		pr = PROGRAM.createProgram(setFacts(), rules, queries);
	}
	
	private static ITuple createFact(String ts0, String ts1){
		return BASIC.createTuple(
				TERM.createString(ts0), TERM.createString(ts1));
	}
	
	private static Map<IPredicate, IMixedDatatypeRelation> setFacts(){
		Map<IPredicate, IMixedDatatypeRelation> facts = new HashMap<IPredicate, IMixedDatatypeRelation>();
		IMixedDatatypeRelation rel = null;
		IPredicate p = null;
		
		
		rel = RELATION.getMixedRelation(2);
		
		rel.add(createFact("a", "e"));
		rel.add(createFact("a", "f"));
		rel.add(createFact("f", "m"));
		rel.add(createFact("g", "n"));
		rel.add(createFact("h", "n"));
		rel.add(createFact("i", "o"));
		rel.add(createFact("j", "o"));
		
		p = BASIC.createPredicate("up", 2);
		facts.put(p, rel);
		
		
		rel = RELATION.getMixedRelation(2);
		
		rel.add(createFact("g", "f"));
		rel.add(createFact("m", "n"));
		rel.add(createFact("m", "o"));
		rel.add(createFact("p", "m"));
		
		p = BASIC.createPredicate("flat", 2);
		facts.put(p, rel);
		
		
		rel = RELATION.getMixedRelation(2);
		
		rel.add(createFact("l", "f"));
		rel.add(createFact("m", "f"));
		rel.add(createFact("g", "b"));
		rel.add(createFact("h", "c"));
		rel.add(createFact("i", "d"));
		rel.add(createFact("p", "k"));
		
		p = BASIC.createPredicate("down", 2);
		facts.put(p, rel);
		
		return facts;
	}
	
	/*private static Map<IPredicate, IMixedDatatypeRelation> setFacts(){
		Map<IPredicate, IMixedDatatypeRelation> facts = new HashMap<IPredicate, IMixedDatatypeRelation>();
		IMixedDatatypeRelation rel = null;
		IPredicate p = null;
		
		
		rel = new Relation(2);
		
		rel.add(createFact("a", "e"));
		rel.add(createFact("a", "f"));
		rel.add(createFact("f", "m"));
		rel.add(createFact("g", "n"));
		rel.add(createFact("h", "n"));
		rel.add(createFact("i", "o"));
		rel.add(createFact("j", "o"));
		/////////////////////////////
		
		p = BASIC.createPredicate("up", 2);
		facts.put(p, rel);
		
		
		rel = new Relation(2);
		
		rel.add(createFact("g", "f"));
		rel.add(createFact("m", "n"));
		rel.add(createFact("m", "o"));
		rel.add(createFact("p", "m"));
		/////////////////////////////
		rel.add(createFact("a", "m"));
		
		p = BASIC.createPredicate("flat", 2);
		facts.put(p, rel);
		
		
		rel = new Relation(2);
		
		rel.add(createFact("l", "f"));
		rel.add(createFact("m", "f"));
		rel.add(createFact("g", "b"));
		rel.add(createFact("h", "c"));
		rel.add(createFact("i", "d"));
		rel.add(createFact("p", "k"));
		/////////////////////////////
		rel.add(createFact("a", "f"));
		
		rel.add(createFact("f", "ee"));
		
		p = BASIC.createPredicate("down", 2);
		facts.put(p, rel);
		
		return facts;
	}*/
}
