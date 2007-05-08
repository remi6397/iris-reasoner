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

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.CONCRETE;
import static org.deri.iris.factory.Factory.TERM;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.INumericTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.api.terms.concrete.IDecimalTerm;
import org.deri.iris.api.terms.concrete.IDoubleTerm;
import org.deri.iris.api.terms.concrete.IFloatTerm;
import org.deri.iris.api.terms.concrete.IIntegerTerm;

/**
 * <p>
 * Some helper methods common to some Builtins.
 * </p>
 * <p>
 * $Id: BuiltinHelper.java,v 1.6 2007-05-08 13:57:00 poettler_ric Exp $
 * </p>
 * 
 * @author Richard Pöttler, richard dot poettler at deri dot org
 * @version $Revision: 1.6 $
 */
public class BuiltinHelper {

	private BuiltinHelper() {
		// prevent subclassing
	}

	/**
	 * Compares two numerics by casting them to doubles and then comparing them
	 * for equality. <b>This method assumes that only numbers are stored in
	 * <code>INumericTerm</code>.</b>
	 * 
	 * @param n0
	 *            the first number
	 * @param n1
	 *            the second number
	 * @return whether the numbers are equal
	 * @throws NullPointerException
	 *             if one of the numbers is null
	 * @see Number
	 */
	public static boolean numbersEqual(final INumericTerm n0, final INumericTerm n1) {
		if ((n0 == null) || (n1 == null)) {
			throw new NullPointerException("The numbers must not be null");
		}
		return numbersCompare(n0, n1) == 0;
	}

	/**
	 * Compares two numerics by casting them to doubles and then comparing them.
	 * <b>This method assumes that only numbers are stored in
	 * <code>INumericTerm</code>.</b>
	 * 
	 * @param n0
	 *            the first number
	 * @param n1
	 *            the second number
	 * @return <code>0</code> if they are equal, a value <code>&lt; 0</code>
	 *         if n0 is smaller than n1 and a value <code>&gt; 0</code> if n0
	 *         is bigger than n1
	 * @throws NullPointerException
	 *             if one of the numbers is null
	 * @see Number
	 */
	public static int numbersCompare(final INumericTerm n0, final INumericTerm n1) {
		if ((n0 == null) || (n1 == null)) {
			throw new NullPointerException("The numbers must not be null");
		}
		return Double.compare(getDouble(n0), getDouble(n1));
	}

	/**
	 * Compares two terms to each other. A value <code>&lt;0</code>, <code>0</code> or
	 * <code>&gt;0</code> will be returned, if the first term is smaller,
	 * equal or greater than the second one.
	 * @param t0 the first term
	 * @param t1 the second term
	 * @return an Integer determining which one of the terms is bigger
	 * @throws NullPointerException if one of the terms is <code>null</code>
	 * @throws IllegalArgumentException if the two terms couldn't be
	 * compared
	 */
	public static int compare(final ITerm t0, final ITerm t1) {
		if ((t0 == null) || (t1 == null)) {
			throw new NullPointerException("The terms must not be null");
		}
		if ((t0 instanceof INumericTerm) && (t1 instanceof INumericTerm)) {
			return numbersCompare((INumericTerm) t0, (INumericTerm) t1);
		} else if (t0.getClass().isAssignableFrom(t1.getClass())) {
			return t0.compareTo(t1);
		}
		throw new IllegalArgumentException("Couldn't compare " + t0.getClass().getName() + 
				" and " + t1.getClass().getName());
	}

	/**
	 * Checks whether the values of two terms are the same.
	 * @param t0 the first term
	 * @param t1 the second term
	 * @return <code>true</code> if the values are equal, otherwise
	 * <code>false</code>
	 * @throws NullPointerException if one of the terms is <code>null</code>
	 */
	public static boolean equal(final ITerm t0, final ITerm t1) {
		if ((t0 == null) || (t1 == null)) {
			throw new NullPointerException("The terms must not be null");
		}
		if ((t0 instanceof INumericTerm) && (t1 instanceof INumericTerm)) {
			return numbersEqual((INumericTerm) t0, (INumericTerm) t1);
		}
		return t0.equals(t1);
	}

	/**
	 * Returns the Double value from a <code>INumericTerm</code> <b>This
	 * method assumes that only numbers are stored in <code>INumericTerm</code>.</b>
	 * 
	 * @param n
	 *            the term
	 * @return the double value
	 * @throws NullPointerException
	 *             if the term is null
	 * @see Number
	 */
	private static double getDouble(final INumericTerm n) {
		if (n == null) {
			throw new NullPointerException("The term must not be null");
		}
		// TODO: maybe instance check for Number
		return ((Number) n.getValue()).doubleValue();
	}

	/**
	 * <p>
	 * Produces the sum of two terms. The resulting term will be of the most accurate
	 * type of the submitted ones.
	 * </p>
	 * <p>
	 * At the moment only INumericTerms are supported.
	 * </p>
	 * @param t0 the first summand
	 * @param t1 the second summand
	 * @return the sum
	 * @throws NullPointerException if one of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is not a INumericTerm
	 */
	public static ITerm add(final ITerm t0, final ITerm t1) {
		if((t0 == null) || (t1 == null)) {
			throw new NullPointerException("The terms must not be null");
		}
		if(!(t0 instanceof INumericTerm) || !(t1 instanceof INumericTerm)) {
			throw new IllegalArgumentException("The terms must be INumericTermS, but were " + 
					t0.getClass().getName() + " and " + t1.getClass().getName());
		}
		return toAccuratest(getDouble((INumericTerm) t0) + getDouble((INumericTerm) t1), t0, t1);
	}

	/**
	 * <p>
	 * Produces the difference of two terms. The resulting term will be of the most accurate
	 * type of the submitted ones.
	 * </p>
	 * <p>
	 * At the moment only INumericTerms are supported.
	 * </p>
	 * @param t0 the minuend
	 * @param t1 the subtrahend
	 * @return the differnece
	 * @throws NullPointerException if one of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is not a INumericTerm
	 */
	public static ITerm subtract(final ITerm t0, final ITerm t1) {
		if((t0 == null) || (t1 == null)) {
			throw new NullPointerException("The terms must not be null");
		}
		if(!(t0 instanceof INumericTerm) || !(t1 instanceof INumericTerm)) {
			throw new IllegalArgumentException("The terms must be INumericTermS, but were " + 
					t0.getClass().getName() + " and " + t1.getClass().getName());
		}
		return toAccuratest(getDouble((INumericTerm) t0) - getDouble((INumericTerm) t1), t0, t1);
	}

	/**
	 * <p>
	 * Produces the product of two terms. The resulting term will be of the most accurate
	 * type of the submitted ones.
	 * </p>
	 * <p>
	 * At the moment only INumericTerms are supported.
	 * </p>
	 * @param t0 the first factor
	 * @param t1 the second factor
	 * @return the product
	 * @throws NullPointerException if one of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is not a INumericTerm
	 */
	public static ITerm multiply(final ITerm t0, final ITerm t1) {
		if((t0 == null) || (t1 == null)) {
			throw new NullPointerException("The terms must not be null");
		}
		if(!(t0 instanceof INumericTerm) || !(t1 instanceof INumericTerm)) {
			throw new IllegalArgumentException("The terms must be INumericTermS, but were " + 
					t0.getClass().getName() + " and " + t1.getClass().getName());
		}
		return toAccuratest(getDouble((INumericTerm) t0) * getDouble((INumericTerm) t1), t0, t1);
	}

	/**
	 * <p>
	 * Produces the quotient of two terms. The resulting term will be of the most accurate
	 * type of the submitted ones.
	 * </p>
	 * <p>
	 * At the moment only INumericTerms are supported.
	 * </p>
	 * @param t0 the dividend
	 * @param t1 the divisor
	 * @return the quotient
	 * @throws NullPointerException if one of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is not a INumericTerm
	 */
	public static ITerm divide(final ITerm t0, final ITerm t1) {
		if((t0 == null) || (t1 == null)) {
			throw new NullPointerException("The terms must not be null");
		}
		if(!(t0 instanceof INumericTerm) || !(t1 instanceof INumericTerm)) {
			throw new IllegalArgumentException("The terms must be INumericTermS, but were " + 
					t0.getClass().getName() + " and " + t1.getClass().getName());
		}
		return toAccuratest(getDouble((INumericTerm) t0) / getDouble((INumericTerm) t1), t0, t1);
	}

	/**
	 * Constructs a number term with of the most accuratest submitted term type.
	 * @param n the value for the returned term
	 * @param t0 the first term (only the type of this term will be taken into account)
	 * @param t1 the first term (only the type of this term will be taken into account)
	 * @return a term of the most accuratest type of the submitted ones with the 
	 * 	given value.
	 * @throws NullPointerException if the number is <code>null</code>
	 * @throws NullPointerException if one of the terms is <code>null</code>
	 * @throws IllegalArgumentException if one of the terms is not  a INumericTerm
	 * @throws IllegalArgumentException if the most accuratest term could not be determined.
	 */
	private static ITerm toAccuratest(final Number n, final ITerm t0, final ITerm t1) {
		if(n == null) {
			throw new NullPointerException("The number must not be null");
		}
		if((t0 == null) || (t1 == null)) {
			throw new NullPointerException("The terms must not be null");
		}
		if(!(t0 instanceof INumericTerm) || !(t1 instanceof INumericTerm)) {
			throw new IllegalArgumentException("The terms must be INumericTermS, but were " + 
					t0.getClass().getName() + " and " + t1.getClass().getName());
		}
		if((t0 instanceof IDecimalTerm) || (t1 instanceof IDecimalTerm)) {
			return CONCRETE.createDecimal(n.doubleValue());
		} else if((t0 instanceof IDoubleTerm) || (t1 instanceof IDoubleTerm)) {
			return CONCRETE.createDouble(n.doubleValue());
		} else if((t0 instanceof IFloatTerm) || (t1 instanceof IFloatTerm)) {
			return CONCRETE.createFloat(n.floatValue());
		} else if((t0 instanceof IIntegerTerm) || (t1 instanceof IIntegerTerm)) {
			return CONCRETE.createInteger(n.intValue());
		}
		throw new IllegalArgumentException("Couldn't determine the most accurate term out of " + 
				t0.getClass().getName() + " and " + t1.getClass().getName());
	}

	/**
	 * Determines the indexes of the ground terms in a collection.
	 * @param t the collection where to search for ground terms
	 * @return the indexes of the ground terms (the first term has the index 0)
	 * @throws NullPointerException if the collection is <code>null</code>
	 */
	public static int[] determineGround(final Collection<ITerm> t) {
		if(t == null) {
			throw new NullPointerException("The tuple must not be null");
		}
		final int[] res = new int[t.size()];
		int i = 0;
		int pos = 0;
		for(final ITerm term : t) {
			if(term.isGround()) {
				res[i++] = pos;
			}
			pos++;
		}
		final int[] ret = new int[i];
		System.arraycopy(res, 0, ret, 0, i);
		return ret;
	}

	/**
	 * Returns the terms at a given position in a collection.
	 * @param t the collection from where to retrieve the terms
	 * @param pos the positions of the terms to retrieve
	 * @return the retrieved terms
	 * @throws NullPointerException if the collection is <code>null</code>
	 * @throws NullPointerException if the position array is <code>null</code>
	 */
	public static ITerm[] getIndexes(final Collection<ITerm> t, final int[] pos) {
		if(pos == null) {
			throw new NullPointerException("The position array must not be null");
		}
		if(t == null) {
			throw new NullPointerException("The tuple must not be null");
		}
		final ITerm[] ret = new ITerm[pos.length];
		final ITerm[] in = t.toArray(new ITerm[t.size()]);
		int idx = 0;

		for (final int i : pos) {
			ret[idx++] = in[i];
		}
		return ret;
	}

	/**
	 * Determines the indexes of the unground terms in a collection.
	 * @param t the collection where to search for ground terms
	 * @return the indexes of the unground terms (the first term has the index 0)
	 * @throws NullPointerException if the collection is <code>null</code>
	 */
	public static int[] determineUnground(final Collection<ITerm> t) {
		if(t == null) {
			throw new NullPointerException("The tuple must not be null");
		}
		final int[] res = new int[t.size()];
		int i = 0;
		int pos = 0;
		for(final ITerm term : t) {
			if(!term.isGround()) {
				res[i++] = pos;
			}
			pos++;
		}
		final int[] ret = new int[i];
		System.arraycopy(res, 0, ret, 0, i);
		return ret;
	}

	/**
	 * <p>
	 * Computes the complement of an index array.
	 * </p>
	 * <p>
	 * E.g. we have computed an index array <code>[1, 3, 4]</code> of a collection
	 * with length 5 we would get <code>[0, 2]</code> as complement.
	 * </p>
	 * @param i the array from which to compute the complement
	 * @param l the length of the collection whe took the index from
	 * @return the complement
	 * @throws NullPointerException if the index array was <code>null</code>
	 * @throws IllegalArgumentException if the lenght was nevative
	 * @see #determineGround(Collection<ITerm>)
	 * @see #determineUnground(Collection<ITerm>)
	 */
	public static int[] complement(final int[] i, final int l) {
		if(l < 0) {
			throw new IllegalArgumentException("The length must not be negative");
		}
		int[] res = new int[l - i.length];
		if (i.length == 0) { // dirty hack would like to use the List.removeAll() method...
			for (int j = 0; j < res.length; j++) {
				res[j] = j;
			}
		} else {
			int guess = 0;
			int ipos = 0;
			for(int j = 0; j < res.length; ) {
				if(i[ipos] != guess) {
					res[j++] = guess;
				} else {
					ipos++;
				}
				guess++;
			}
		}
		return res;
	}

	/**
	 * <p>
	 * Concats two term arrays taking an index array into account.
	 * </p>
	 * <p>
	 * E.g. we got an index of <code>[0, 3, 4]</code>, the firs array is <code>['a', 'b', 'c']</code>  
	 * and the second <code>[12, 13, 14]</code> we will get <code>['a', 12, 13, 'b', 'c', 14]</code>.
	 * </p>
	 * @param idx0 the index array
	 * @param t0 the first array (this terms will be put at he positions of the index array)
	 * @param t1 the second array (with this terms the gaps will be filled up)
	 * @throws NullPointerException if the index array is <code>null</code>
	 * @throws NullPointerException if one of the term arrays is <code>null</code>
	 * @throws IllegalArgumentException if the lenght of the index and the first term array don't match
	 * @see #determineGround(Collection<ITerm>)
	 * @see #determineUnground(Collection<ITerm>)
	 */
	public static ITerm[] concat(final int[] idx0, final ITerm[] t0, final ITerm[] t1) {
		if(idx0 == null) {
			throw new NullPointerException("The index array must not be null");
		}
		if((t0 == null) || (t1 == null)) {
			throw new NullPointerException("The term arrays must not be null");
		}
		if(idx0.length != t0.length) {
			throw new IllegalArgumentException("The length of the index array and " + 
					"the first array must be equal, but was " + 
					idx0.length + " and " + t0.length);
		}
		final ITerm[] res = new ITerm[t0.length + t1.length];
		if (idx0.length == 0) {
			System.arraycopy(t1, 0, res, 0, t1.length);
		} else {
			int ipos = 0;
			int pos0 = 0;
			int pos1 = 0;
			for(int i = 0; i < res.length; i++) {
				if(idx0[ipos] == i) {
					res[i] = t0[pos0++];
					ipos++;
				} else {
					res[i] = t1[pos1++];
				}
			}
		}
		return res;
	}

	/**
	 * <p>
	 * Constructs the constants tuple used by the evaluage method of the
	 * builtins. This method takes a builtin tuple, a tuple of constants and
	 * an array of variables as input. A newly created tuple with the
	 * constants of <code>c</code> at the positions of the variables of
	 * <code>v</code> found in <code>b</code>.
	 * </p>
	 * <p>
	 * In other words, if you pass in a builtin <code>ADD(X, Y, 5)</code>
	 * and a tuple <code>&lt;2&gt;</code> and <code>v</code> is <code>[X]</code> 
	 * you will get back a tuple <code>&lt;2, Y, 5&gt;</code>. If <code>v</code> 
	 * is <code>[Y]</code> the returned tuple would be <code>&lt;X, 2, 5&gt;</code>.
	 * </p>
	 * <p>
	 * If performance is very crucial i wouldn't use this method, because it
	 * has to search for the variables every time. So if you have one
	 * builtin and a set of constants for wich you want to evaluate the
	 * builtin you should consider writing the replacement on your own.
	 * </p>
	 * @param b the base tuple in which to search for the variable
	 * possitions
	 * @param c the constan tuple from where to take the constants
	 * @param v the variables to replace
	 * @return the constants tupele used by the evaluate methods of the
	 * builtins
	 * @throws NullPointerException if the base tuple is <code>null</code>
	 * @throws NullPointerException if the constans tuple is <code>null</code>
	 * @throws NullPointerException if the variable array is <code>null</code>
	 * @throws IllegalArgumentException if the lenght of the constants tuple
	 * and the variable array doesn't match
	 * @see org.deri.iris.api.builtins.IBuiltInAtom#evaluate(ITuple)
	 */
	public static ITuple createConstantsTuple(final ITuple b, final ITuple c, IVariable... v) {
		if (b == null) {
			throw new NullPointerException("The builtin tuple must not be null");
		}
		if (c == null) {
			throw new NullPointerException("The consants tuple must not be null");
		}
		if (v == null) {
			throw new NullPointerException("The variable array must not be null");
		}
		if (c.getArity() != v.length) {
			throw new IllegalArgumentException("The lenght of the constants (" + 
					c.getArity() + ") must be equals to the lenght of the variables (" + 
					v.length + ")");
		}

		final Iterator<ITerm> iter = c.getTerms().iterator();
		final ITerm[] ret = b.getTerms().toArray(new ITerm[b.getTerms().size()]);
		final List<ITerm> terms = Arrays.asList(ret);

		for (final IVariable var : v) {
			final int pos = terms.indexOf(v);
			if (pos > -1) {
				ret[pos] = iter.next();
			} else {
				throw new IllegalArgumentException("Couldn't find the variable " + 
						var + " in " + b);
			}
		}
		return BASIC.createTuple(ret);
	}
}
