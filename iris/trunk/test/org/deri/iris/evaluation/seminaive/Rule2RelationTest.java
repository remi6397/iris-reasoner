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
import static org.deri.iris.factory.Factory.SEMINAIVE_MODEL;

import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import java.util.HashMap;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IHead;
import org.deri.iris.api.basics.IBody;

import org.deri.iris.api.evaluation.seminaive.model.ITree;
import org.deri.iris.api.evaluation.seminaive.model.IProjection;
import org.deri.iris.api.evaluation.seminaive.model.INaturalJoin;
import org.deri.iris.api.evaluation.seminaive.model.IRule;

/**
 * @author Joachim Adi Schuetz, DERI Innsbruck
 * @author Darko Anicic, DERI Innsbruck
 * @date $Date: 2006-11-10 10:27:24 $
 * @version $Id: Rule2RelationTest.java,v 1.1 2006-11-10 10:27:24 adi Exp $
 */
public class Rule2RelationTest extends TestCase {

	Rule2Relation r2r;
	List<ILiteral> literals;
	Collection<org.deri.iris.api.basics.IRule> rules;
	
	public static Test suite() {
		return new TestSuite(Rule2RelationTest.class, Rule2RelationTest.class.getSimpleName());
	}

	/**
	 * setup for Rule2Relation tests
	 */
	public void setUp() {
		r2r = new Rule2Relation();
		literals = new ArrayList<ILiteral>();
		rules = new ArrayList<org.deri.iris.api.basics.IRule>();		
	}

	/**
	 * run rule2relation test
	 *
	 */
	protected void runRule2Relation(final Collection<org.deri.iris.api.basics.IRule> rul, final Map<ITree, ITree> rel) {

		Map<ITree, ITree> res = this.r2r.eval(rul);
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
		HashMap<ITree, ITree> result = new HashMap<ITree, ITree>();
		// head
		ITree tree = SEMINAIVE_MODEL.createTree("s");
		tree.addVariable("X");
		tree.addVariable("Y");
		// body
		int[] index = new int[] {0, -1, 1};
		IProjection proj = SEMINAIVE_MODEL.createProjection(index);
		proj.addVariable("X");
		proj.addVariable("Y");
		INaturalJoin join = SEMINAIVE_MODEL.createNaturalJoin();
		join.addVariable("X");
		join.addVariable("Z");
		join.addVariable("Y");
		IRule rule = SEMINAIVE_MODEL.createRule("p", 2);
		rule.addVariable("X");
		rule.addVariable("Z");
		join.addComponent(rule);
		rule = SEMINAIVE_MODEL.createRule("r", 2);
		rule.addVariable("Y");
		rule.addVariable("Z");
		join.addComponent(rule);
		proj.addComponent(join);
		ITree tree2 = proj;
		tree2.addComponent(proj);
		
		result.put(tree, tree2);
				
		runRule2Relation(rules, result);
	}
	protected static void assertResults(final Map<ITree, ITree> a, final Map<ITree, ITree> b) {
		Assert.assertEquals("The length of relation and the list of"
				+ " expected tuples must be equal", a.size(), b.size());
		Set<ITree> keyseta = a.keySet();
		Set<ITree> keysetb = b.keySet();
		Iterator it = keyseta.iterator();
		Iterator it2 = keysetb.iterator();
		while(it.hasNext() && it2.hasNext()) {
			ITree keya = (ITree)it.next();
			ITree keyb = (ITree)it2.next();
			assertTrue("The keys must be equal.", keya.equals(keyb));
			ITree vala = a.get(keya);
			ITree valb = b.get(keyb);
			
			System.out.println("---> " + vala + "\n---> " + valb);
			// does not work at the moment
			assertTrue("The vals must be equal.", vala.equals(valb));
		}
	}
}
