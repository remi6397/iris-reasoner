package org.deri.iris.evaluation.common;

import static org.deri.iris.MiscHelper.createLiteral;
import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.TERM;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.deri.iris.api.basics.IBody;
import org.deri.iris.api.basics.IHead;
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
		IHead head = BASIC.createHead(createLiteral("r0", "X", "Y"));
		IBody body = BASIC.createBody(createLiteral("r11", "X", "Y"));
		IRule r0 = BASIC.createRule(head, body);
		rs.add(r0);
		
		
		// r11(X, Y) :- r21(X), r22(Y), r23(X,Y)
		head = BASIC.createHead(createLiteral("r11", "X", "Y"));
		List<ILiteral> bodyLiterals = new ArrayList<ILiteral>();
		bodyLiterals.add(createLiteral("r21", "X"));
		bodyLiterals.add(createLiteral("r22", "Y"));
		bodyLiterals.add(createLiteral("r23", "X", "Y"));
		body = BASIC.createBody(bodyLiterals);
		IRule r11 = BASIC.createRule(head, body);
		rs.add(r11);
		
		// r23(X, Y) :- r31(X, Y)
		head = BASIC.createHead(createLiteral("r23", "X", "Y"));
		body = BASIC.createBody(createLiteral("r31", "X", "Y"));
		IRule r23 = BASIC.createRule(head, body);
		rs.add(r23);
		
		// r31(X, Y) :- r11(X, Y), r22(Y) // here comes a recursion (no a direct one!)
		head = BASIC.createHead(createLiteral("r31", "X", "Y"));
		bodyLiterals = new ArrayList<ILiteral>();
		bodyLiterals.add(createLiteral("r11", "X", "Y"));
		bodyLiterals.add(createLiteral("r22", "Y"));
		body = BASIC.createBody(bodyLiterals);
		IRule r31 = BASIC.createRule(head, body);
		rs.add(r31);
		
		// s0(X, Y) :- s11(X), s12(X,Y,Z)
		head = BASIC.createHead(createLiteral("s0", "X", "Y"));
		bodyLiterals = new ArrayList<ILiteral>();
		bodyLiterals.add(createLiteral("s11", "X"));
		bodyLiterals.add(createLiteral("s12", "Y", "Y", "Z"));
		body = BASIC.createBody(bodyLiterals);
		IRule s0 = BASIC.createRule(head, body);
		rs.add(s0);
		
		//	s11(X) :- s21(X), r11(X,Y), s22(Q,R)
		head = BASIC.createHead(createLiteral("s11", "X"));
		bodyLiterals = new ArrayList<ILiteral>();
		bodyLiterals.add(createLiteral("s21", "X"));
		bodyLiterals.add(createLiteral("r11", "X", "Y"));
		bodyLiterals.add(createLiteral("s21", "Q", "R"));
		body = BASIC.createBody(bodyLiterals);
		IRule s11 = BASIC.createRule(head, body);
		rs.add(s11);
	
		// s12(X) :- s121(X,X), s122(Q,R)
		head = BASIC.createHead(createLiteral("s12", "X"));
		bodyLiterals = new ArrayList<ILiteral>();
		bodyLiterals.add(createLiteral("s121", "X", "X"));
		bodyLiterals.add(createLiteral("s122", "Q", "R"));
		body = BASIC.createBody(bodyLiterals);
		IRule s12 = BASIC.createRule(head, body);
		rs.add(s12);
		
		// s21(X) :- s21(X)
		head = BASIC.createHead(createLiteral("s21", "X"));
		bodyLiterals = new ArrayList<ILiteral>();
		bodyLiterals.add(createLiteral("s21", "X"));
		body = BASIC.createBody(bodyLiterals);
		IRule s21 = BASIC.createRule(head, body);
		rs.add(s21);
		
		// s22(X, Z) :-  s0(Z,Z), s21(X), s31(Z, Z, X)
		head = BASIC.createHead(createLiteral("s22", "X", "Z"));
		bodyLiterals = new ArrayList<ILiteral>();
		bodyLiterals.add(createLiteral("s0", "Z", "Z"));
		bodyLiterals.add(createLiteral("s21", "X"));
		bodyLiterals.add(createLiteral("s31", "Z", "Z", "X"));
		body = BASIC.createBody(bodyLiterals);
		IRule s22 = BASIC.createRule(head, body);
		rs.add(s22);
		
		// s31(X, Y, R) :-  s0(Z,Z), s21(X), s31(Z, Z, X)
		head = BASIC.createHead(createLiteral("s31", "X", "Y", "R"));
		bodyLiterals = new ArrayList<ILiteral>();
		bodyLiterals.add(createLiteral("s0", "Z", "Z"));
		bodyLiterals.add(createLiteral("s21", "X"));
		bodyLiterals.add(createLiteral("s31", "Z", "Z", "X"));
		body = BASIC.createBody(bodyLiterals);
		IRule s31 = BASIC.createRule(head, body);
		rs.add(s31);
		
		
		// t0(X) :-  t11(Z,Z)
		head = BASIC.createHead(createLiteral("t0", "X"));
		bodyLiterals = new ArrayList<ILiteral>();
		bodyLiterals.add(createLiteral("t11", "Z", "Z"));
		body = BASIC.createBody(bodyLiterals);
		IRule t0 = BASIC.createRule(head, body);
		rs.add(t0);
		
		//	 t11(X,Z) :-  t21a(X), t22a(X), t23a(X), t24a(Z) 
		head = BASIC.createHead(createLiteral("t11", "X", "Z"));
		bodyLiterals = new ArrayList<ILiteral>();
		bodyLiterals.add(createLiteral("t21a", "X"));
		bodyLiterals.add(createLiteral("t22a", "X"));
		bodyLiterals.add(createLiteral("t23a", "X"));
		bodyLiterals.add(createLiteral("t24a", "Z"));
		body = BASIC.createBody(bodyLiterals);
		IRule t11a = BASIC.createRule(head, body);
		rs.add(t11a);
		
		//  t11(X,Z) :-  t21b(X), t22b(X), t23b(X), t24b(Z) 
		head = BASIC.createHead(createLiteral("t11", "X", "Z"));
		bodyLiterals = new ArrayList<ILiteral>();
		bodyLiterals.add(createLiteral("t21b", "X"));
		bodyLiterals.add(createLiteral("t22b", "X"));
		bodyLiterals.add(createLiteral("t23b", "X"));
		bodyLiterals.add(createLiteral("t24b", "Z"));
		body = BASIC.createBody(bodyLiterals);
		IRule t11b = BASIC.createRule(head, body);
		rs.add(t11b);
		
		//	t21b(X) :-  t21a(X)
		head = BASIC.createHead(createLiteral("t21b", "X"));
		bodyLiterals = new ArrayList<ILiteral>();
		bodyLiterals.add(createLiteral("t21a", "X"));
		body = BASIC.createBody(bodyLiterals);
		IRule t21b = BASIC.createRule(head, body);
		rs.add(t21b);
		
		//	t21a(X) :-  t21b(X)
		head = BASIC.createHead(createLiteral("t21a", "X"));
		bodyLiterals = new ArrayList<ILiteral>();
		bodyLiterals.add(createLiteral("t21b", "X"));
		body = BASIC.createBody(bodyLiterals);
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
	 * Test method for 'org.deri.iris.evaluation.common.EvaluationUtilities.extractRelevantRulesForEvaluation(Set<IRule>, IQuery)'
	 */
	public void testExtractRelevantRulesForEvaluationQ1() {

		Set<IRule> filteredRuleSet = 
			EvaluationUtilities.extractRelevantRulesForEvaluation(rs, query1);
		assertEquals(resultQuery1,filteredRuleSet);
	
	}

	/*
	 * Test method for 'org.deri.iris.evaluation.common.EvaluationUtilities.extractRelevantRulesForEvaluation(Set<IRule>, IQuery)'
	 */
	public void testExtractRelevantRulesForEvaluationQ2() {

		Set<IRule> filteredRuleSet = 
			EvaluationUtilities.extractRelevantRulesForEvaluation(rs, query2);
	
		assertEquals(resultQuery2,filteredRuleSet);
		 
		
	
	}
	
	/*
	 * Test method for 'org.deri.iris.evaluation.common.EvaluationUtilities.extractRelevantRulesForEvaluation(Set<IRule>, IQuery)'
	 */
	public void testExtractRelevantRulesForEvaluationQ3() {

		Set<IRule> filteredRuleSet = 
			EvaluationUtilities.extractRelevantRulesForEvaluation(rs, query3);
		assertEquals(resultQuery3,filteredRuleSet);
	
	}
	
	/*
	 * Test method for 'org.deri.iris.evaluation.common.EvaluationUtilities.extractRelevantRulesForEvaluation(Set<IRule>, IQuery)'
	 */
	public void testExtractRelevantRulesForEvaluationQ4() {

		Set<IRule> filteredRuleSet = 
			EvaluationUtilities.extractRelevantRulesForEvaluation(rs, query4);
		assertEquals(resultQuery4, filteredRuleSet);
	
	}
	
	/*
	 * Test method for 'org.deri.iris.evaluation.common.EvaluationUtilities.extractRelevantRulesForEvaluation(Set<IRule>, IQuery)'
	 */
	public void testExtractRelevantRulesForEvaluationQ5() {

		Set<IRule> filteredRuleSet = 
			EvaluationUtilities.extractRelevantRulesForEvaluation(rs, query5);
		assertEquals(resultQuery5, filteredRuleSet);
	
	}
	
	
}
