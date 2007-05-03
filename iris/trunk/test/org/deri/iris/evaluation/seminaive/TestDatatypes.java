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

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.TERM;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.deri.iris.Executor;
import org.deri.iris.api.IExecutor;
import org.deri.iris.api.IProgram;
import org.deri.iris.api.basics.IBody;
import org.deri.iris.api.basics.IHead;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.evaluation.algebra.IExpressionEvaluator;
import org.deri.iris.api.storage.IRelation;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.evaluation.algebra.ExpressionEvaluator;
import org.deri.iris.factory.Factory;
import org.deri.iris.parser.ProgramTest;

/**
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date   06.03.2007 10:00:07
 */
public class TestDatatypes {
	public static void main(final String[] arg) {
		
		// constructing rules
		
		// solarPlanet(X) :- planet(?X), distanceFromSun(?X,?Y)
		Set<IRule> rules = new HashSet<IRule>(2);
		IHead h = BASIC.createHead(
				BASIC.createLiteral(true, BASIC.createAtom(
						BASIC.createPredicate("solarPlanet", 1), 
						BASIC.createTuple(TERM.createVariable("X")))));
		
		IBody b = Factory.BASIC.createBody(
				BASIC.createLiteral(true, BASIC.createAtom(
						BASIC.createPredicate("planet", 1), 
						BASIC.createTuple(TERM.createVariable("X")))),
				BASIC.createLiteral(true, BASIC.createAtom(
						BASIC.createPredicate("distanceFromSun", 2), 
						BASIC.createTuple(TERM.createVariable("X"),TERM.createVariable("Y")))));

		IRule r = Factory.BASIC.createRule(h, b);
		rules.add(r);

		// nonSolarPlanet(X) :- planet(?X), not solarPlanet(?X)
		h = BASIC.createHead(
				BASIC.createLiteral(true, BASIC.createAtom(
						BASIC.createPredicate("nonSolarPlanet", 1), 
						BASIC.createTuple(TERM.createVariable("X")))));
		
		b = Factory.BASIC.createBody(
				BASIC.createLiteral(true, BASIC.createAtom(
						BASIC.createPredicate("planet", 1), 
						BASIC.createTuple(TERM.createVariable("X")))),
				BASIC.createLiteral(false, BASIC.createAtom(
						BASIC.createPredicate("solarPlanet", 1), 
						BASIC.createTuple(TERM.createVariable("X")))));

		r = Factory.BASIC.createRule(h, b);
		rules.add(r);

		// constructing facts
		Map<IPredicate, IRelation> facts = new HashMap<IPredicate, IRelation>(); 
		/*
		 * Solar planets: Mercury, Venus, Earth, Mars, Jupiter, Saturn, Uranus, 
		 * Neptune and Pluto;
		 * Non Solar planets: 51Pegasi_planet
		 */
		IPredicate p = Factory.BASIC.createPredicate("planet", 1);
		IRelation rel = Factory.RELATION.getRelation(1);
		
		rel.add(Factory.BASIC.createTuple(
				Factory.TERM.createString("mercury")));
		rel.add(Factory.BASIC.createTuple(
				Factory.TERM.createString("venus")));
		rel.add(Factory.BASIC.createTuple(
				Factory.TERM.createString("earth")));
		rel.add(Factory.BASIC.createTuple(
				Factory.TERM.createString("mars")));
		rel.add(Factory.BASIC.createTuple(
				Factory.TERM.createString("jupiter")));
		rel.add(Factory.BASIC.createTuple(
				Factory.TERM.createString("saturn")));
		rel.add(Factory.BASIC.createTuple(
				Factory.TERM.createString("uranus")));
		rel.add(Factory.BASIC.createTuple(
				Factory.TERM.createString("neptune")));
		rel.add(Factory.BASIC.createTuple(
				Factory.TERM.createString("pluto")));
		rel.add(Factory.BASIC.createTuple(
				Factory.TERM.createString("51pegasi_planet")));
		
		facts.put(p, rel);
		
		// distanceFromSun(?X,?Y)
		
		p = Factory.BASIC.createPredicate("distanceFromSun", 2);
		rel = Factory.RELATION.getRelation(2);
		rel.add(Factory.BASIC.createTuple(
				Factory.TERM.createString("mercury"),
				Factory.CONCRETE.createDecimal(1)));
		rel.add(Factory.BASIC.createTuple(
				Factory.TERM.createString("venus"),
				Factory.CONCRETE.createDecimal(0.72)));
		rel.add(Factory.BASIC.createTuple(
				Factory.TERM.createString("earth"),
				Factory.CONCRETE.createDecimal(1.0)));
		rel.add(Factory.BASIC.createTuple(
				Factory.TERM.createString("mars"),
				Factory.CONCRETE.createDecimal(1.5)));
		rel.add(Factory.BASIC.createTuple(
				Factory.TERM.createString("jupiter"),
				Factory.CONCRETE.createDecimal(5.2)));
		rel.add(Factory.BASIC.createTuple(
				Factory.TERM.createString("saturn"),
				Factory.CONCRETE.createDecimal(9.5)));
		rel.add(Factory.BASIC.createTuple(
				Factory.TERM.createString("uranus"),
				Factory.CONCRETE.createDecimal(19.2)));
		rel.add(Factory.BASIC.createTuple(
				Factory.TERM.createString("neptune"),
				Factory.CONCRETE.createDecimal(30.1)));
		rel.add(Factory.BASIC.createTuple(
				Factory.TERM.createString("pluto"),
				Factory.CONCRETE.createDecimal(39.5)));
		
		/*rel.add(Factory.BASIC.createTuple(
				Factory.CONCRETE.createDecimal(1),
				Factory.CONCRETE.createDecimal(1)));
		rel.add(Factory.BASIC.createTuple(
				Factory.CONCRETE.createDecimal(12),
				Factory.CONCRETE.createDecimal(0.72)));*/
		
		facts.put(p, rel);

		// constructing a query
		// :- solarPlanet(X)
		IQuery q = Factory.BASIC.createQuery(
				BASIC.createLiteral(true, BASIC.createAtom(
					BASIC.createPredicate("nonSolarPlanet", 1), 
					BASIC.createTuple(TERM.createVariable("X")))));
		
		Set<IQuery> queries = new HashSet<IQuery>(1);
		queries.add(q);
		
		final IProgram e = Factory.PROGRAM.createProgram(facts, rules, queries);
		
		System.out.println("--- input ---");
		for (final IRule rule : e.getRules()) {
			System.out.println(rule);
		}
	
		System.out.println("--- facts ---");
		for (final IPredicate pred : e.getPredicates()) {
			System.out.printf("%s -> %s\n", pred.getPredicateSymbol(), e
					.getFacts(pred));
			for (ITuple t : e.getFacts(pred)) {
				System.out.println(pred.getPredicateSymbol() + t);
			}
		}
		
		IExpressionEvaluator method = new ExpressionEvaluator();
		IExecutor exec = new Executor(e, method);
		exec.execute();
		IRelation result = exec.computeSubstitution(q);
		
		System.out.println("--- results for query: " + q.toString());
		ProgramTest.printResults(result);
	}
}
