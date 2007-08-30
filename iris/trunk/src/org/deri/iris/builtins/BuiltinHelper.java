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

import static org.deri.iris.factory.Factory.CONCRETE;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.INumericTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.concrete.IDateTerm;
import org.deri.iris.api.terms.concrete.IDateTime;
import org.deri.iris.api.terms.concrete.IDecimalTerm;
import org.deri.iris.api.terms.concrete.IDoubleTerm;
import org.deri.iris.api.terms.concrete.IDuration;
import org.deri.iris.api.terms.concrete.IFloatTerm;
import org.deri.iris.api.terms.concrete.IIntegerTerm;
import org.deri.iris.api.terms.concrete.ITime;

/**
 * <p>
 * Some helper methods common to some Builtins.
 * </p>
 * <p>
 * $Id: BuiltinHelper.java,v 1.12 2007-08-30 16:19:49 poettler_ric Exp $
 * </p>
 * 
 * @author Richard Pöttler, richard dot poettler at deri dot org
 * @version $Revision: 1.12 $
 */
public class BuiltinHelper {

	/** 
	 * Empty tuple (with arity 0) to avoid some timeconsuming creation of
	 * tuples. 
	 */
	public static final ITuple EMPTY_TUPLE = 
		org.deri.iris.factory.Factory.BASIC.createTuple(0);

	/** Milliseconds per hour. */
	private static final int MILLIS_PER_HOUR = 1000 * 60 * 60;

	/** Milliseconds per minute. */
	private static final int MILLIS_PER_MINUTE = 1000 * 60;

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
	 * Extracts the hours out of a timezone.
	 * @param tz the timezone
	 * @return the hours
	 * @throws NullPointerException if the timezone is <code>null</code>
	 */
	private static int getTimeZoneHour(final TimeZone tz) {
		if (tz == null) {
			throw new NullPointerException("The timezone must not be null");
		}
		return tz.getRawOffset() / MILLIS_PER_HOUR;
	}

	/**
	 * Extracts the minutes out of a timezone.
	 * @param tz the timezone
	 * @return the minutes
	 * @throws NullPointerException if the timezone is <code>null</code>
	 */
	private static int getTimeZoneMinute(final TimeZone tz) {
		if (tz == null) {
			throw new NullPointerException("The timezone must not be null");
		}
		return (tz.getRawOffset() % MILLIS_PER_HOUR) / MILLIS_PER_MINUTE;
	}

	/**
	 * Creates a duration out of a given amount of milliseconds.
	 * @param millis the milliseconds for which to create the duration
	 * @return the duration
	 */
	private static IDuration createDuration(final long millis) {
		return CONCRETE.createDuration(millis);
	}

	/**
	 * Creates a time out of a given amount of milliseconds.
	 * @param millis the milliseconds for which to create the duration
	 * @param zone the timezone the resulting time should have. if
	 * <code>null</code> GMT will be set.
	 * @return the time
	 */
	private static ITime createTime(final long millis, final TimeZone zone) {
		final Calendar cal = new GregorianCalendar(zone);
		cal.setTimeInMillis(millis);
		return CONCRETE.createTime(cal.get(Calendar.HOUR_OF_DAY), 
				cal.get(Calendar.MINUTE), 
				cal.get(Calendar.SECOND),
				(zone == null) ? 0 : getTimeZoneHour(zone), 
				(zone == null) ? 0 : getTimeZoneMinute(zone));
	}

	/**
	 * Creates a datetime out of a given amount of milliseconds.
	 * @param millis the milliseconds for which to create the duration
	 * @param zone the timezone the resulting time should have. if
	 * <code>null</code> GMT will be set.
	 * @return the datetime
	 */
	private static IDateTime createDateTime(final long millis, final TimeZone zone) {
		final Calendar cal = new GregorianCalendar(zone);
		cal.setTimeInMillis(millis);
		return CONCRETE.createDateTime(cal.get(Calendar.YEAR), 
				cal.get(Calendar.MONTH), 
				cal.get(Calendar.DAY_OF_MONTH), 
				cal.get(Calendar.HOUR_OF_DAY), 
				cal.get(Calendar.MINUTE), 
				cal.get(Calendar.SECOND),
				(zone == null) ? 0 : getTimeZoneHour(zone), 
				(zone == null) ? 0 : getTimeZoneMinute(zone));
	}

	/**
	 * Creates a date out of a given amount of milliseconds.
	 * @param millis the milliseconds for which to create the duration
	 * @return the date
	 */
	private static IDateTerm createDate(final long millis) {
		final Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
		cal.setTimeInMillis(millis);
		return CONCRETE.createDate(cal.get(Calendar.YEAR), 
				cal.get(Calendar.MONTH), 
				cal.get(Calendar.DAY_OF_MONTH));
	}

	/**
	 * <p>
	 * Produces the sum of two terms. The resulting term will be of the most accurate
	 * type of the submitted ones.
	 * </p>
	 * @param t0 the first summand
	 * @param t1 the second summand
	 * @return the sum
	 * @throws NullPointerException if one of the terms is <code>null</code>
	 * @throws IllegalArgumentException if the operation couldn't be applied
	 * to the terms
	 */
	public static ITerm add(final ITerm t0, final ITerm t1) {
		if((t0 == null) || (t1 == null)) {
			throw new NullPointerException("The terms must not be null");
		}
		if((t0 instanceof INumericTerm) && (t1 instanceof INumericTerm)) { // number + number = number
			return toAccuratest(getDouble((INumericTerm) t0) + getDouble((INumericTerm) t1), t0, t1);
		} else if ((t0 instanceof IDateTime) && (t1 instanceof IDuration)) { // datetime + duration = datetime
			final Calendar cal0 = ((IDateTime) t0).getValue();
			return createDateTime(cal0.getTimeInMillis() + ((IDuration) t1).getValue(), cal0.getTimeZone());
		} else if ((t0 instanceof IDuration) && (t1 instanceof IDateTime)) { // duration + datetime = datetime
			final Calendar cal1 = ((IDateTime) t1).getValue();
			return createDateTime(((IDuration) t0).getValue() + cal1.getTimeInMillis(), cal1.getTimeZone());
		} else if ((t0 instanceof IDateTerm) && (t1 instanceof IDuration)) { // date + duration = date
			return createDate(((IDateTerm) t0).getValue().getTimeInMillis() + ((IDuration) t1).getValue());
		} else if ((t0 instanceof IDuration) && (t1 instanceof IDateTerm)) { // duration + date = date
			return createDate(((IDuration) t0).getValue() + ((IDateTerm) t1).getValue().getTimeInMillis());
		} else if ((t0 instanceof ITime) && (t1 instanceof IDuration)) { // time + duration = time
			final Calendar cal0 = ((ITime) t0).getValue();
			return createTime(cal0.getTimeInMillis() + ((IDuration) t1).getValue(), cal0.getTimeZone());
		} else if ((t0 instanceof IDuration) && (t1 instanceof ITime)) { // duration + time = time
			final Calendar cal1 = ((ITime) t1).getValue();
			return createTime(((IDuration) t0).getValue() + cal1.getTimeInMillis(), cal1.getTimeZone());
		} else if ((t0 instanceof IDuration) && (t1 instanceof IDuration)) { // duration + duration = duration
			return createDuration(((IDuration) t0).getValue() + ((IDuration) t1).getValue());
		}
		throw new IllegalArgumentException("The add operatoin on " + t0.getClass().getName() + 
				" and " + t1.getClass().getName() + " is not possible");
	}

	/**
	 * <p>
	 * Produces the difference of two terms. The resulting term will be of the most accurate
	 * type of the submitted ones.
	 * </p>
	 * @param t0 the minuend
	 * @param t1 the subtrahend
	 * @return the differnece
	 * @throws NullPointerException if one of the terms is <code>null</code>
	 * @throws IllegalArgumentException if the operation couldn't be applied
	 * to the terms
	 */
	public static ITerm subtract(final ITerm t0, final ITerm t1) {
		if((t0 == null) || (t1 == null)) {
			throw new NullPointerException("The terms must not be null");
		}
		if((t0 instanceof INumericTerm) && (t1 instanceof INumericTerm)) { // numer - number = number
			return toAccuratest(getDouble((INumericTerm) t0) - getDouble((INumericTerm) t1), t0, t1);
		} else if ((t0 instanceof IDateTime) && (t1 instanceof IDateTime)) { // datetime - datetime = duration
			return createDuration(((IDateTime) t0).getValue().getTimeInMillis() - ((IDateTime) t1).getValue().getTimeInMillis());
		} else if ((t0 instanceof IDateTime) && (t1 instanceof IDuration)) { // datetime - duration = datetime
			final Calendar cal0 = ((IDateTime) t0).getValue();
			return createDateTime(cal0.getTimeInMillis() - ((IDuration) t1).getValue(), cal0.getTimeZone());
		} else if ((t0 instanceof IDateTerm) && (t1 instanceof IDateTerm)) { // date - date = duration
			return createDuration(((IDateTerm) t0).getValue().getTimeInMillis() - ((IDateTerm) t1).getValue().getTimeInMillis());
		} else if ((t0 instanceof IDateTerm) && (t1 instanceof IDuration)) { // date - duration = date
			return createDate(((IDateTerm) t0).getValue().getTimeInMillis() - ((IDuration) t1).getValue());
		} else if ((t0 instanceof ITime) && (t1 instanceof ITime)) { // time - time = duration
			return createDuration(((ITime) t0).getValue().getTimeInMillis() - ((ITime) t1).getValue().getTimeInMillis());
		} else if ((t0 instanceof ITime) && (t1 instanceof IDuration)) { // time - duration = time
			final Calendar cal0 = ((ITime) t0).getValue();
			return createTime(cal0.getTimeInMillis() - ((IDuration) t1).getValue(), cal0.getTimeZone());
		} else if ((t0 instanceof IDuration) && (t1 instanceof IDuration)) { // duration - duration
			return createDuration(((IDuration) t0).getValue() - ((IDuration) t1).getValue());
		}
		throw new IllegalArgumentException("The subtract operatoin on " + t0.getClass().getName() + 
				" and " + t1.getClass().getName() + "is not possible");
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
	 * @throws IllegalArgumentException if ther are more terms requested,
	 * than given (pos.length &gt; t.size())
	 */
	public static ITerm[] getIndexes(final Collection<ITerm> t, final int[] pos) {
		if(pos == null) {
			throw new NullPointerException("The position array must not be null");
		}
		if(t == null) {
			throw new NullPointerException("The tuple must not be null");
		}
		if (pos.length > t.size()) {
			throw new IllegalArgumentException("There are " + pos.length + " <" + 
					Arrays.asList(pos) + "> terms requested, but only " + t.size() + 
					" <" + t + "> terms given");
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
		// TODO: this method needs a rewrite (get rid of this
		// arraycopy)! WHY DON'T WE USE PERL!?!?!
		int[] ret = new int[l - i.length];
		int[] clone = new int[i.length];
		System.arraycopy(i, 0, clone, 0, i.length);
		Arrays.sort(clone);
		for (int j = 0, pos = 0; pos < ret.length; j++) {
			if (Arrays.binarySearch(clone, j) < 0) {
				ret[pos++] = j;
			}
		}
		return ret;
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
		final java.util.List<ITerm> res = new java.util.LinkedList<ITerm>();
		res.addAll(Arrays.asList(t1));
		for (int i = 0; i < idx0.length; i++) {
			res.add(idx0[i], t0[i]);
		}
		return res.toArray(new ITerm[res.size()]);
	}

	/**
	 * Merges the terms of two tuples. The constants of the first tuple
	 * will have a highter priority than the constants of the second one.
	 * @param t0 the first tuple (with the higher priority)
	 * @param t1 the second tuple
	 * @return the merged terms
	 * @throws NullPointerException if one of the tuples is <code>null</code>
	 * @throws IllegalArgumentException the arities of the tuples doesn't
	 * match.
	 * @since 0.4
	 */
	public static ITerm[] merge(final ITuple t0, final ITuple t1) {
		if ((t0 == null) || (t1 == null)) {
			throw new NullPointerException("The none of the tuples must not be null");
		}
		if (t0.getArity() != t1.getArity()) {
			throw new IllegalArgumentException("The arity of the tuples must match " + 
					t0.getArity() + " <-> " + t1.getArity());
		}
		// calculating the needed term indexes from the submitted tuple
		int[] outstanding = determineUnground(t0.getTerms());
		// retrieving the constants of this builin
		final ITerm[] bCons = getIndexes(t0.getTerms(), 
				complement(outstanding, t0.getArity()));
		// putting the term from this builtin and the submitted tuple together
		return concat(outstanding, getIndexes(t1.getTerms(), outstanding), bCons);
	}
}
