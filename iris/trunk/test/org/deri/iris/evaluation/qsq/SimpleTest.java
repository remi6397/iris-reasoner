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
import org.deri.iris.api.evaluation.common.IAdornedProgram;
import org.deri.iris.evaluation.common.AdornedProgram;

/**
 * @author Darko Anicic, DERI Innsbruck
 * @date   02.08.2006 14:37:59
 */
public class SimpleTest {
	static IAdornedProgram program = null;
	static QSQTemplate qsqTemplate = null;
	
	
	public static void main(final String[] args) {
		setAdornedProgram();
		test();
	}
	
	private static void test(){
		qsqTemplate = new QSQTemplate(program);
		qsqTemplate.getQSQTemplate();
		System.out.println(qsqTemplate);
	}
	
	private static void setAdornedProgram(){
		System.out.println("*** TEST 2 ******");
		
		Set<IRule> rules = new HashSet<IRule>();

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

		IQuery query = BASIC.createQuery(
							BASIC.createLiteral(true, 
									BASIC.createPredicate("rsg", 2), 
									BASIC.createTuple(
											TERM.createConstant(
													TERM.createString("a")), 
													TERM.createVariable("Y"))));

		System.out.println("*** input: ******");
		for (IRule r : rules) {
			System.out.println(r);
		}
		System.out.println(query);
		System.out.println();

		System.out.println("*** output: ******");
		program = new AdornedProgram(rules, query);
		System.out.println(program);
	}
}
