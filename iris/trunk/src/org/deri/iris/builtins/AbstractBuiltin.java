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
import org.deri.iris.api.terms.INumericTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.factory.Factory;

/**
 * <p>
 * Serves as skeleton implementation for builtins.
 * </p>
 * <p>
 * To successfully subclass this class simply implement the evaluate method. If
 * needed the isEvaluable method should be overwritten, too. See the method
 * documentation for it's default behaviour.
 * </p>
 * <p>
 * $Id: AbstractBuiltin.java,v 1.3 2006-09-28 11:30:11 richardpoettler Exp $
 * </p>
 * 
 * @author richi
 * @version $Revision: 1.3 $
 * @date $Date: 2006-09-28 11:30:11 $
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
		this(p, t.length, t);
	}

	/**
	 * <p>
	 * Constructs the builtin. More precisely it constructs the inner atom. The
	 * number of terms submitted to this constructor must match the arity of the
	 * predicate.
	 * </p>
	 * <p>
	 * The necessary field is used to define the minimal amount of initially
	 * submitted terms to successfully evaluate the builtin. An example usage
	 * could be a term which computes a result and stores it in an additional
	 * field, which doesn't need to be submitted while the construction of the
	 * term (e.g. add(x, y, z) computes x + y and stores the result in z, but
	 * only x and y need to be submitted for the construction).
	 * </p>
	 * 
	 * @param p
	 *            the special predicate for this builtin
	 * @param necessary
	 *            the amount of necessary terms for this builtin
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
	protected AbstractBuiltin(final IPredicate p, final int necessary,
			final ITerm... t) {
		if ((p == null) || (t == null)) {
			throw new NullPointerException(
					"The predicate and the terms must not be null");
		}
		if (Arrays.asList(t).contains(null)) {
			throw new NullPointerException("The terms must not contain null");
		}
		if (necessary > p.getArity()) {
			throw new IllegalArgumentException("The amount of necessary ("
					+ necessary + ") terms must not be bigger "
					+ "than the arity of the predicate (" + p.getArity() + ")");
		}
		if ((necessary >= 0) && (t.length < necessary)) {
			throw new IllegalArgumentException("There where " + necessary
					+ " terms required, but only " + t.length + " terms given");
		} else if ((necessary < 0) && (t.length != p.getArity())) {
			throw new IllegalArgumentException(
					"The length of the terms and the arity of the perdicate "
							+ "must match. Was " + p.getArity() + " but was "
							+ t.length);
		}
		final ITuple tup = Factory.BASIC.createTuple(p.getArity());
		tup.setTerms(Arrays.asList(t));
		this.a = Factory.BASIC.createAtom(p, tup);
	}

	public abstract boolean evaluate();

	/**
	 * <p>
	 * Determines whether the builtin is evaluable.
	 * </p>
	 * <p>
	 * First this method determines whether the first and the second term is
	 * ground. If both were not ground, true will be returned. If they are
	 * ground, <code>true</code> will be returned, if they are both
	 * <code>INumericTerm</code>s or their classes match.
	 * </p>
	 * 
	 * @return <code>true</code> if it's evaluable, <code>false</code>, if
	 *         not
	 */
	public boolean isEvaluable() {
		if (getTerm(0).isGround() && getTerm(1).isGround()) {
			if ((getTerm(0) instanceof INumericTerm)
					&& (getTerm(1) instanceof INumericTerm)) {
				return true;
			} else if (getTerm(0).getClass().equals(getTerm(1).getClass())) {
				return true;
			}
			return false;
		}
		return true;
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

	public boolean isInCycle() {
		return a.isInCycle();
	}

	public int compareTo(Object o) {
		return a.compareTo(o);
	}

	/**
	 * Returns the term at the given position.
	 * 
	 * @param i
	 *            the index of the term (starts at 0)
	 * @return the term
	 * @throws IndexOutOfBoundsException
	 *             if i is to small, or too big (bigger or equals to the arity
	 *             of the atom)
	 * @see #setTerm(int, ITerm)
	 */
	protected ITerm getTerm(final int i) {
		if ((i < 0) || (i >= getTermsLength())) {
			throw new IndexOutOfBoundsException(
					"The index must be between 0 and " + getTermsLength()
							+ ", but was " + i);
		}
		return a.getTuple().getTerm(i);
	}

	/**
	 * Sets the term at the given position.
	 * 
	 * @param i
	 *            the position of the term (starts at 0)
	 * @param t
	 *            the term
	 * @throws IndexOutOfBoundsException
	 *             if i is to small, or too big (bigger or equals to the arity
	 *             of the atom)
	 * @see #getTerm(int)
	 */
	protected void setTerm(final int i, final ITerm t) {
		if ((i < 0) || (i >= getTermsLength())) {
			throw new IndexOutOfBoundsException(
					"The index must be between 0 and " + getTermsLength()
							+ ", but was " + i);
		}
		a.getTuple().setTerm(i, t);
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
}
