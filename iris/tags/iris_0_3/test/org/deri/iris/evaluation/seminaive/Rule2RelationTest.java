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

import static org.deri.iris.factory.Factory.ALGEBRA;
import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.BUILTIN;
import static org.deri.iris.factory.Factory.TERM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deri.iris.api.basics.IBody;
import org.deri.iris.api.basics.IHead;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.evaluation.algebra.IComponent;
import org.deri.iris.api.evaluation.algebra.IConstantDescriptor;
import org.deri.iris.api.evaluation.algebra.IJoinDescriptor;
import org.deri.iris.api.evaluation.algebra.IProjectionDescriptor;
import org.deri.iris.api.evaluation.algebra.IRelationDescriptor;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.evaluation.algebra.Rule2Relation;
import org.deri.iris.operations.relations.JoinCondition;

/**
 * @author Joachim Adi Schuetz, DERI Innsbruck
 * @author Darko Anicic, DERI Innsbruck
 * @date $Date: 2007-01-25 15:05:35 $
 * @version $Id: Rule2RelationTest.java,v 1.8 2007-01-25 15:05:35 darko Exp $
 */
public class Rule2RelationTest extends TestCase {

	Rule2Relation r2r;
	List<ILiteral> literals;
	Set<IRule> rules;
	
	public static Test suite() {
		return new TestSuite(Rule2RelationTest.class, Rule2RelationTest.class.getSimpleName());
	}

	/**
	 * setup for Rule2Relation tests
	 */
	public void setUp() {
		r2r = new Rule2Relation();
		literals = new ArrayList<ILiteral>();
		rules = new HashSet<IRule>();		
	}

	/**
	 * run rule2relation test
	 *
	 */
	protected void runRule2Relation(final Set<IRule> rul, final Map<IPredicate, IComponent> rel) {

		Map<IPredicate, IComponent> res = this.r2r.translateRules(rul);
		System.out.println("in: " + rul + "\nrel: "+ rel + "\nres: " + res + "\n");
		assertResults(rel, res);
	}
	
	/**
	 * s(X, Y) :- p(Y, Z), r(Y, Z)
	 *
	 */
	public void testRule2Relation() {
		// input
		ILiteral literal = BASIC.createLiteral(true, BASIC.createPredicate(
				"s", 2));
		literal.getTuple().setTerm(0, TERM.createVariable("X"));
		literal.getTuple().setTerm(1, TERM.createVariable("Y"));
		literals.add(literal);
		IHead head = BASIC.createHead(literals);

		literals.clear();
		
		literal = BASIC.createLiteral(true, BASIC.createPredicate("p", 2));
		literal.getTuple().setTerm(0, TERM.createVariable("X"));
		literal.getTuple().setTerm(1, TERM.createVariable("Z"));
		literals.add(literal);

		literal = BASIC.createLiteral(true, BASIC.createPredicate("r", 2));
		literal.getTuple().setTerm(0, TERM.createVariable("Y"));
		literal.getTuple().setTerm(1, TERM.createVariable("Z"));
		literals.add(literal);

		IBody body = BASIC.createBody(literals);
		
		rules.add(BASIC.createRule(head, body));

		// result
		HashMap<IPredicate, IComponent> result = new HashMap<IPredicate, IComponent>();
		// head
		IRelationDescriptor tree = ALGEBRA.createRelationDescriptor(true, BASIC.createBuiltinPredicate("s", 2));
		List<IVariable> vars = new ArrayList<IVariable>();
		vars.add(TERM.createVariable("X"));
		vars.add(TERM.createVariable("Y"));
		tree.addVariables(vars);
		
		// body
		int[] index = new int[] {0, -1, 1};
		IProjectionDescriptor proj = ALGEBRA.createProjectionDescriptor(index);
		proj.addVariables(vars);
		IJoinDescriptor join = ALGEBRA.createJoinDescriptor(JoinCondition.EQUALS);
		IRelationDescriptor rule = ALGEBRA.createRelationDescriptor(
				true, BASIC.createPredicate("p", 2));
		vars = new ArrayList<IVariable>();
		vars.add(TERM.createVariable("X"));
		vars.add(TERM.createVariable("Z"));
		rule.addVariables(vars);
		join.addChild(rule);
		rule = ALGEBRA.createRelationDescriptor(
				true, BASIC.createPredicate("r", 2));
		vars = new ArrayList<IVariable>();
		vars.add(TERM.createVariable("Y"));
		vars.add(TERM.createVariable("Z"));
		rule.addVariables(vars);
		join.addChild(rule);
		proj.addChild(join);
		IComponent tree2 = proj;
		tree2.addChild(proj);
		
		result.put(tree.getPredicate(), tree2);
				
		runRule2Relation(rules, result);
	}
	/**
	 * p(?X,?Y) :- r(?Z, ?Y) and ?X='a'
	 *
	 */
	public void testRule2Relation_1a() {
		// input
		ILiteral literal = BASIC.createLiteral(true, BASIC.createPredicate(
				"p", 2));
		literal.getTuple().setTerm(0, TERM.createVariable("X"));
		literal.getTuple().setTerm(1, TERM.createVariable("Y"));
		literals.add(literal);
		IHead head = BASIC.createHead(literals);

		literals.clear();
		
		literal = BASIC.createLiteral(true, BASIC.createPredicate("r", 2));
		literal.getTuple().setTerm(0, TERM.createVariable("Z"));
		literal.getTuple().setTerm(1, TERM.createVariable("Y"));
		literals.add(literal);

		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("X"), TERM.createString("a")));
		literals.add(literal);

		IBody body = BASIC.createBody(literals);
		
		rules.add(BASIC.createRule(head, body));

		// result
		HashMap<IPredicate, IComponent> result = new HashMap<IPredicate, IComponent>();
		// head
		IRelationDescriptor tree = ALGEBRA.createRelationDescriptor(
				true, BASIC.createPredicate("p", 2));
		List<IVariable> vars = new ArrayList<IVariable>();
		vars.add(TERM.createVariable("X"));
		vars.add(TERM.createVariable("Y"));
		tree.addVariables(vars);
		
		// body
		int[] index = new int[] {0, -1, 1};
		IProjectionDescriptor proj = ALGEBRA.createProjectionDescriptor(index);
		proj.addVariables(vars);
		IJoinDescriptor join = ALGEBRA.createJoinDescriptor(JoinCondition.EQUALS);
		IRelationDescriptor rule = ALGEBRA.createRelationDescriptor(true, BASIC.createBuiltinPredicate("r", 2));
		vars = new ArrayList<IVariable>();
		vars.add(TERM.createVariable("Z"));
		vars.add(TERM.createVariable("Y"));
		rule.addVariables(vars);
		join.addChild(rule);
		IConstantDescriptor con = ALGEBRA.createConstantDescriptor(TERM.createString("a"), TERM.createVariable("X"));
		join.addChild(con);
		proj.addChild(join);
		IComponent tree2 = proj;
		tree2.addChild(proj);
		
		result.put(tree.getPredicate(), tree2);
				
		runRule2Relation(rules, result);
	}
	/**
	 * p(?X,?Y) :- r(?X, ?Y) and ?X!='a'
	 *
	 */
	/*public void testRule2Relation_1b() {
		// input
		ILiteral literal = BASIC.createLiteral(true, BASIC.createPredicate(
				"p", 2));
		literal.getTuple().setTerm(0, TERM.createVariable("X"));
		literal.getTuple().setTerm(1, TERM.createVariable("Y"));
		literals.add(literal);
		IHead head = BASIC.createHead(literals);

		literals.clear();
		
		literal = BASIC.createLiteral(true, BASIC.createPredicate("r", 2));
		literal.getTuple().setTerm(0, TERM.createVariable("Z"));
		literal.getTuple().setTerm(1, TERM.createVariable("Y"));
		literals.add(literal);

		literal = BASIC.createLiteral(true, BUILTIN.createEqual(TERM.createVariable("X"), TERM.createString("a")));
		literals.add(literal);

		IBody body = BASIC.createBody(literals);
		
		rules.add(BASIC.createRule(head, body));

		// result
		HashMap<IPredicate, IComponent> result = new HashMap<IPredicate, IComponent>();
		// head
		IComponent tree = SEMINAIVE_MODEL.createTree("p");
		tree.addVariables("X");
		tree.addVariables("Y");
		// body
		int[] index = new int[] {0, -1, 1};
		IProjection proj = SEMINAIVE_MODEL.createProjection(index);
		proj.addVariables("X");
		proj.addVariables("Y");
		IJoin join = SEMINAIVE_MODEL.createJoin(new int[]{-1,-1}, JoinCondition.EQUALS);
		IRule rule = SEMINAIVE_MODEL.createRule("r", 2);
		rule.addVariables("Z");
		rule.addVariables("Y");
		join.addComponent(rule);
		rule = SEMINAIVE_MODEL.createUnaryRule("a");
		rule.addVariables("X");
		join.addComponent(rule);
		proj.addComponent(join);
		IComponent tree2 = proj;
		tree2.addComponent(proj);
		
		result.put(BASIC.createPredicate(tree.getName(), tree.getArity()), tree2);
				
		runRule2Relation(rules, result);
	}*/
	
	protected static void assertResults(final Map<IPredicate, IComponent> a, 
			final Map<IPredicate, IComponent> b) {
		
		Assert.assertEquals("The length of relation and the list of"
				+ " expected tuples must be equal", a.size(), b.size());
		Set<IPredicate> keyseta = a.keySet();
		Set<IPredicate> keysetb = b.keySet();
		Iterator it = keyseta.iterator();
		Iterator it2 = keysetb.iterator();
		while(it.hasNext() && it2.hasNext()) {
			IPredicate keya = (IPredicate)it.next();
			IPredicate keyb = (IPredicate)it2.next();
			assertTrue("The keys must be equal.", keya.equals(keyb));
		}
	}
}
