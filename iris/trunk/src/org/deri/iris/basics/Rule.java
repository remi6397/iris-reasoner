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
package org.deri.iris.basics;

import static org.deri.iris.factory.Factory.GRAPH;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.deri.iris.api.basics.IBody;
import org.deri.iris.api.basics.IHead;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;

/**
 * @author richi
 *
 */
public class Rule implements IRule {
	
	private IHead head = null;
	private IBody body = null;
	
	Rule(final IHead head, final IBody body) {
		if (body == null) {
			throw new IllegalArgumentException("The body must not be null");
		}
		this.head = head;
		this.body = body;
	}

	/**
	 * checks the safeness of the datalog rule:
	 * @see ,,logic as a datamodel'' chapter 3 page 104
	 * 
	 * @return true if the rule is safe
	 */
	
	public boolean isSafe() {
		Map<IVariable, Boolean> varsLimited = new HashMap<IVariable, Boolean>();

		for (IVariable var : this.head.getHeadVariables()) {
			varsLimited.put(var, Boolean.FALSE);
		}
		System.out.println(this + "\n\n" + varsLimited);

		// get all literals of the body
		List<ILiteral> tmpLiterals = new ArrayList<ILiteral>(), literals = this.body.getBodyLiterals();
		// remove all ordinary(non-builtin) not-negated predicates and mark their variables as limited
		Iterator it = literals.iterator();
		while (it.hasNext()) {
			ILiteral lit = (ILiteral) it.next();
			System.out.println("focus: "  + lit + " is builtin: " + lit.getPredicate().isBuiltIn());
			if (!lit.getPredicate().isBuiltIn() && lit.isPositive() && !lit.isGround())
				for(ITerm litTerm : lit.getTuple().getTerms()) {
					if (!litTerm.isGround()) {
						varsLimited.put((IVariable)litTerm, Boolean.TRUE);
					}
				}
			else
				tmpLiterals.add(lit);
		}

		System.out.println("lit list meta: "  + tmpLiterals);
						
		// handle negated literals and builtin predicates
		it = literals.iterator();
		while (it.hasNext()) {
			ILiteral lit = (ILiteral) it.next();
			System.out.println("lit: "  + lit + " - " + lit.getPredicate().getPredicateSymbol());
			if(lit.getPredicate().isBuiltIn()) {
				if(lit.getPredicate().getPredicateSymbol() != "EQUAL")
					return false;

				System.out.println("in"); // todo: handle equality-builtin
				
			}
		}

		// todo: handle negated literals
		
		// final check if all variables are limited
		for (IVariable var : varsLimited.keySet())
		{
			if (!varsLimited.get(var))
				return false;
		}
		return true;
	}

	public boolean isRectified() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isCycled() {
		return GRAPH.createPredicateGraph().detectCycles();
	}

	public boolean isFact() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isBuiltIn() {
		// TODO Auto-generated method stub
		return false;
	}

	public int getHeadLenght() {
		return head.getHeadLenght();
	}

	public ILiteral getHeadLiteral(int arg) {
		return head.getHeadLiteral(arg);
	}

	public List<ILiteral> getHeadLiterals() {
		return head.getHeadLiterals();
	}

	public List<IVariable> getHeadVariables() {
		return head.getHeadVariables();
	}

	public int getBodyLenght() {
		return body.getBodyLenght();
	}

	public ILiteral getBodyLiteral(int arg) {
		return body.getBodyLiteral(arg);
	}

	public List<ILiteral> getBodyLiterals() {
		return body.getBodyLiterals();
	}

	public List<IVariable> getBodyVariables() {
		return body.getBodyVariables();
	}
	
	public int hashCode() {
		int result = 37;
		result = result * 17 + body.hashCode();
		result = result * 17 + head.hashCode();
		return result;
	}
	
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Rule)) {
			return false;
		}
		Rule r = (Rule) o;
		return body.equals(r.body) && head.equals(r.head);
	}
	
	public String toString() {
		return head + " :- " + body;
	}
}
