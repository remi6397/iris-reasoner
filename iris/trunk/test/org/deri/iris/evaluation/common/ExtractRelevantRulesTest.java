package org.deri.iris.evaluation.common;

import static org.deri.iris.MiscHelper.createLiteral;
import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.TERM;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;

import junit.framework.TestCase;

public class ExtractRelevantRulesTest extends TestCase {

	IQuery query1, query2, query3, query4, query5;
	Set<IRule> rs;
	
	Set<IRule> resultQuery1;
	Set<IRule> resultQuery2;
	Set<IRule> resultQuery3;
	Set<IRule> resultQuery4;
	Set<IRule> resultQuery5;
	
	public static void main(String[] args) {
	}

	public ExtractRelevantRulesTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		// constructing he rules for rules set rs1;
		
		rs = new HashSet<IRule>();
		resultQuery1 = new HashSet<IRule>();
		resultQuery2 = new HashSet<IRule>();
		resultQuery3 = new HashSet<IRule>();
		resultQuery4 = new HashSet<IRule>();
		resultQuery5 = new HashSet<IRule>();
		
		// r0(X, Y) :- r11(X, Y)
		List<ILiteral> head = Arrays.asList(createLiteral("r0", "X", "Y"));
		List<ILiteral> body = Arrays.asList(createLiteral("r11", "X", "Y"));
		IRule r0 = BASIC.createRule(head, body);
		rs.add(r0);
		
		
		// r11(X, Y) :- r21(X), r22(Y), r23(X,Y)
		head = Arrays.asList(createLiteral("r11", "X", "Y"));
		body = new ArrayList<ILiteral>();
		body.add(createLiteral("r21", "X"));
		body.add(createLiteral("r22", "Y"));
		body.add(createLiteral("r23", "X", "Y"));
		IRule r11 = BASIC.createRule(head, body);
		rs.add(r11);
		
		// r23(X, Y) :- r31(X, Y)
		head = Arrays.asList(createLiteral("r23", "X", "Y"));
		body = Arrays.asList(createLiteral("r31", "X", "Y"));
		IRule r23 = BASIC.createRule(head, body);
		rs.add(r23);
		
		// r31(X, Y) :- r11(X, Y), r22(Y) // here comes a recursion (no a direct one!)
		head = Arrays.asList(createLiteral("r31", "X", "Y"));
		body = new ArrayList<ILiteral>();
		body.add(createLiteral("r11", "X", "Y"));
		body.add(createLiteral("r22", "Y"));
		IRule r31 = BASIC.createRule(head, body);
		rs.add(r31);
		
		// s0(X, Y) :- s11(X), s12(X,Y,Z)
		head = Arrays.asList(createLiteral("s0", "X", "Y"));
		body = new ArrayList<ILiteral>();
		body.add(createLiteral("s11", "X"));
		body.add(createLiteral("s12", "X", "Y", "Z"));
		IRule s0 = BASIC.createRule(head, body);
		rs.add(s0);
		
		//	s11(X) :- s21(X), r11(X,Y), s22(Q,R)
		head = Arrays.asList(createLiteral("s11", "X"));
		body = new ArrayList<ILiteral>();
		body.add(createLiteral("s21", "X"));
		body.add(createLiteral("r11", "X", "Y"));
		body.add(createLiteral("s21", "Q", "R"));
		IRule s11 = BASIC.createRule(head, body);
		rs.add(s11);
	
		// s12(X) :- s121(X,X), s122(Q,R)
		head = Arrays.asList(createLiteral("s12", "X"));
		body = new ArrayList<ILiteral>();
		body.add(createLiteral("s121", "X", "X"));
		body.add(createLiteral("s122", "Q", "R"));
		IRule s12 = BASIC.createRule(head, body);
		rs.add(s12);
		
		// s21(X) :- s21(X)
		head = Arrays.asList(createLiteral("s21", "X"));
		body = new ArrayList<ILiteral>();
		body.add(createLiteral("s21", "X"));
		IRule s21 = BASIC.createRule(head, body);
		rs.add(s21);
		
		// s22(X, Z) :-  s0(Z,Z), s21(X), s31(Z, Z, X)
		head = Arrays.asList(createLiteral("s22", "X", "Z"));
		body = new ArrayList<ILiteral>();
		body.add(createLiteral("s0", "Z", "Z"));
		body.add(createLiteral("s21", "X"));
		body.add(createLiteral("s31", "Z", "Z", "X"));
		IRule s22 = BASIC.createRule(head, body);
		rs.add(s22);
		
		// s31(X, Y, R) :-  s0(Z,Z), s21(X), s31(Z, Z, X)
		head = Arrays.asList(createLiteral("s31", "X", "Y", "R"));
		body = new ArrayList<ILiteral>();
		body.add(createLiteral("s0", "Z", "Z"));
		body.add(createLiteral("s21", "X"));
		body.add(createLiteral("s31", "Z", "Z", "X"));
		IRule s31 = BASIC.createRule(head, body);
		rs.add(s31);
		
		
		// t0(X) :-  t11(Z,Z)
		head = Arrays.asList(createLiteral("t0", "X"));
		body = new ArrayList<ILiteral>();
		body.add(createLiteral("t11", "Z", "Z"));
		IRule t0 = BASIC.createRule(head, body);
		rs.add(t0);
		
		//	 t11(X,Z) :-  t21a(X), t22a(X), t23a(X), t24a(Z) 
		head = Arrays.asList(createLiteral("t11", "X", "Z"));
		body = new ArrayList<ILiteral>();
		body.add(createLiteral("t21a", "X"));
		body.add(createLiteral("t22a", "X"));
		body.add(createLiteral("t23a", "X"));
		body.add(createLiteral("t24a", "Z"));
		IRule t11a = BASIC.createRule(head, body);
		rs.add(t11a);
		
		//  t11(X,Z) :-  t21b(X), t22b(X), t23b(X), t24b(Z) 
		head = Arrays.asList(createLiteral("t11", "X", "Z"));
		body = new ArrayList<ILiteral>();
		body.add(createLiteral("t21b", "X"));
		body.add(createLiteral("t22b", "X"));
		body.add(createLiteral("t23b", "X"));
		body.add(createLiteral("t24b", "Z"));
		IRule t11b = BASIC.createRule(head, body);
		rs.add(t11b);
		
		//	t21b(X) :-  t21a(X)
		head = Arrays.asList(createLiteral("t21b", "X"));
		body = new ArrayList<ILiteral>();
		body.add(createLiteral("t21a", "X"));
		IRule t21b = BASIC.createRule(head, body);
		rs.add(t21b);
		
		//	t21a(X) :-  t21b(X)
		head = Arrays.asList(createLiteral("t21a", "X"));
		body = new ArrayList<ILiteral>();
		body.add(createLiteral("t21b", "X"));
		IRule t21a = BASIC.createRule(head, body);
		rs.add(t21a);
		
		
		// constructing some queries

		List<ILiteral> qLiterals;
		
		// r0(john, Y)
		query1 = BASIC.createQuery(BASIC.createLiteral(true, BASIC
				.createPredicate("r0", 2), BASIC.createTuple(TERM
				.createString("john"), TERM.createVariable("Y"))));
		
		resultQuery1.add(r0);
		resultQuery1.add(r11);
		resultQuery1.add(r23);
		resultQuery1.add(r31);
				

		// s22(Z, Z)
		qLiterals = new ArrayList<ILiteral>();
		qLiterals.add(createLiteral("s22", "Z", "Z"));
		query2 = BASIC.createQuery(qLiterals);
		
		resultQuery2.add(s0);
		resultQuery2.add(s11);
		resultQuery2.add(s21);
		resultQuery2.add(s22);
		resultQuery2.add(s31);
		resultQuery2.add(r11);
		resultQuery2.add(r23);
		resultQuery2.add(r31);

		
		
		// r0(X,Y,Z,D)
		qLiterals = new ArrayList<ILiteral>();
		qLiterals.add(createLiteral("r0", "X", "Y", "Z", "D"));
		query3 = BASIC.createQuery(qLiterals);
		
		// resultQuery3 is empty
		
		// s21(X), r23(P, Q), s12(Q)
		qLiterals = new ArrayList<ILiteral>();
		qLiterals.add(createLiteral("s21", "X"));
		qLiterals.add(createLiteral("r23", "P", "Q"));
		qLiterals.add(createLiteral("s12", "Q"));
		query4 = BASIC.createQuery(qLiterals);
		
		resultQuery4.add(s21);
		resultQuery4.add(r23);
		resultQuery4.add(r31);
		resultQuery4.add(r11);
		resultQuery4.add(s12);
		
		// t0(X, Y)
		qLiterals = new ArrayList<ILiteral>();
		qLiterals.add(createLiteral("t0", "X"));
		query5 = BASIC.createQuery(qLiterals);
		
		resultQuery5.add(t0);
		resultQuery5.add(t11a);
		resultQuery5.add(t11b);
		resultQuery5.add(t21a);
		resultQuery5.add(t21b);
		
		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Test method for 'org.deri.iris.evaluation.common.EvaluationUtilities.shrinkRules(Set<IRule>, IQuery)'
	 */
	public void testExtractRelevantRulesForEvaluationQ1() {

		Set<IRule> filteredRuleSet = 
			EvaluationUtilities.shrinkRules(rs, query1);
		assertEquals(resultQuery1,filteredRuleSet);
	
	}

	/*
	 * Test method for 'org.deri.iris.evaluation.common.EvaluationUtilities.shrinkRules(Set<IRule>, IQuery)'
	 */
	public void testExtractRelevantRulesForEvaluationQ2() {

		Set<IRule> filteredRuleSet = 
			EvaluationUtilities.shrinkRules(rs, query2);
	
		assertEquals(resultQuery2,filteredRuleSet);
		 
		
	
	}
	
	/*
	 * Test method for 'org.deri.iris.evaluation.common.EvaluationUtilities.shrinkRules(Set<IRule>, IQuery)'
	 */
	public void testExtractRelevantRulesForEvaluationQ3() {

		Set<IRule> filteredRuleSet = 
			EvaluationUtilities.shrinkRules(rs, query3);
		assertEquals(resultQuery3,filteredRuleSet);
	
	}
	
	/*
	 * Test method for 'org.deri.iris.evaluation.common.EvaluationUtilities.shrinkRules(Set<IRule>, IQuery)'
	 */
	public void testExtractRelevantRulesForEvaluationQ4() {

		Set<IRule> filteredRuleSet = 
			EvaluationUtilities.shrinkRules(rs, query4);
		assertEquals(resultQuery4, filteredRuleSet);
	
	}
	
	/*
	 * Test method for 'org.deri.iris.evaluation.common.EvaluationUtilities.shrinkRules(Set<IRule>, IQuery)'
	 */
	public void testExtractRelevantRulesForEvaluationQ5() {

		Set<IRule> filteredRuleSet = 
			EvaluationUtilities.shrinkRules(rs, query5);
		assertEquals(resultQuery5, filteredRuleSet);
	
	}
	
	
}
