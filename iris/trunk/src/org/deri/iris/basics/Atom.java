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

import java.util.ArrayList;
import java.util.List;

import org.deri.iris.api.basics.IAtom;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.terms.ITerm;

/**
 * @author richi
 * 
 */
public class Atom implements IAtom<IAtom> {

	private List<ITerm> terms;

	private final IPredicate predicate;

	public Atom(final IPredicate predicate) {
		if (predicate == null) {
			throw new IllegalArgumentException("The predicate must not be null");
		}
		this.predicate = predicate;
		terms = new ArrayList<ITerm>(predicate.getArity());
	}
	
	public Atom(final IPredicate predicate, final List<ITerm> terms) {
		this(predicate);
		setTerms(terms);
	}

	public IPredicate getPredicate() {
		return predicate;
	}

	public void setTerms(List terms) {
		if (terms.size() != predicate.getArity()) {
			throw new IllegalArgumentException(
					"The number of submitted terms ("
							+ terms.size()
							+ ") doesn't match the arity of the arity of the predicate ("
							+ predicate.getArity() + ")");
		}
		this.terms = terms;
	}

	public List<ITerm> getTerms() {
		return terms;
	}

	public void setTerm(ITerm term, int arg) {
		if (arg >= predicate.getArity()) {
			throw new IllegalArgumentException("Can't set a more terms (" + arg
					+ ") than the arity of the arity of the predicate ("
					+ predicate.getArity() + ")");
		}
		if (arg >= terms.size()) {
			terms.add(arg, term);
		}
		terms.set(arg, term);
	}

	public ITerm getTerm(int arg) {
		return terms.get(arg);
	}

	public boolean isGround() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isInCycle() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isKnown() {
		// TODO Auto-generated method stub
		return false;
	}

	public int compareTo(IAtom o) {
		int res = 0;
		int minsize = 0;
		if ((res = predicate.compareTo(o.getPredicate())) != 0) {
			return res;
		}

		minsize = Math.min(terms.size(), o.getTerms().size());
		for (int iCounter = 0; iCounter < minsize; iCounter++) {
			if ((res = terms.get(iCounter).compareTo(o.getTerm(iCounter))) != 0) {
				return res;
			}
		}

		return terms.size() - o.getTerms().size();
	}

	public int hashCode() {
		int result = 37;
		result = result * 17 + predicate.hashCode();
		for (ITerm t : terms) {
			if (t != null) {
				result = result * 17 + t.hashCode();
			}
		}
		return result;
	}

	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Atom)) {
			return false;
		}

		Atom a = (Atom) o;
		return (predicate.equals(a.predicate)) && terms.equals(a.terms);
	}

	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append(predicate.getPredicateSymbol()).append("(");
		for (ITerm t : terms) {
			buffer.append(t).append(", ");
		}
		if (terms.size() <= 0) {
			return buffer.append(")").toString();
		}
		return buffer.substring(0, buffer.length() - 2) + ")";
	}
}
