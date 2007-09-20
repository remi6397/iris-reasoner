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
package org.deri.iris.builtins;

import java.util.Arrays;

import org.deri.iris.api.basics.IAtom;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.builtins.IBuiltInAtom;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.factory.Factory;

/**
 * <p>
 * Serves as skeleton implementation for builtins. If you use this class as
 * superclass, you only have only to implement the evaluate method.
 * <p>
 * $Id: AbstractBuiltin.java,v 1.8 2007-05-10 15:58:01 poettler_ric Exp $
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot org)
 * @version $Revision: 1.8 $
 */
public abstract class AbstractBuiltin implements IBuiltInAtom {

	/** Holds the inner atom. */
	private IAtom a;

	/**
	 * Constructs the builtin. More precisely it constructs the inner atom. The
	 * number of terms submitted to this constructor must match the arity of the
	 * predicate.
	 * 
	 * @param p
	 *            the special predicate for this builtin
	 * @param t
	 *            the terms defining the values and variables for this builtin
	 * @throws NullPointerException
	 *             if the perdicate or the terms is {@code null}
	 * @throws NullPointerException
	 *             if the terms contain {@code null}
	 * @throws IllegalArgumentException
	 *             if the length of the terms and the arity of the perdicate
	 *             doesn't match
	 */
	protected AbstractBuiltin(final IPredicate p, final ITerm... t) {
		if ((p == null) || (t == null)) {
			throw new NullPointerException(
					"The predicate and the terms must not be null");
		}
		if (Arrays.asList(t).contains(null)) {
			throw new NullPointerException("The terms must not contain null");
		}
		if (t.length != p.getArity()) {
			throw new IllegalArgumentException("The amount of terms <" + t.length + 
					"> must match the arity of the predicate <" + p.getArity() + ">");
		}
		this.a = Factory.BASIC.createAtom(p, Factory.BASIC.createTuple(t));
	}

	public IPredicate getPredicate() {
		return a.getPredicate();
	}

	public ITuple getTuple() {
		return a.getTuple();
	}

	public boolean isGround() {
		return a.isGround();
	}

	public int compareTo(Object o) {
		return a.compareTo(o);
	}

	/**
	 * Returns the number of terms stored in this builtin.
	 * 
	 * @return the number of terms
	 */
	protected int getTermsLength() {
		return a.getPredicate().getArity();
	}

	/**
	 * <p>
	 * Returns a short description of the inner atom. <b>The format of the
	 * returned String is undocumented and shubject to change.</b>
	 * </p>
	 * <p>
	 * An example String could be: <code>EQUALS(A, B)</code>
	 * </p>
	 * 
	 * @return the short description
	 */
	public String toString() {
		return a.toString();
	}

	public int hashCode() {
		return a.hashCode();
	}

	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof AbstractBuiltin)) {
			return false;
		}
		AbstractBuiltin ab = (AbstractBuiltin) o;
		return a.equals(ab.a);
	}

	public boolean isBuiltin() {
		return true;
	}
}
