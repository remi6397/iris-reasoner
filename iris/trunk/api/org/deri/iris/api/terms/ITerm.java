/*
 * MINS (Mins Is Not Silri) A Prolog Egine based on the Silri  
 * 
 * Copyright (C) 1999-2005  Juergen Angele and Stefan Decker
 *                          University of Innsbruck, Austria  
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.deri.iris.api.terms;

/**
 * @author Darko Anicic, DERI Innsbruck
 * @date 14.11.2005 12:09:01
 */

public interface ITerm<Type extends ITerm, Type0> extends Comparable<Type> {

	public boolean isGround();

	public String toString();

	public boolean equals(Object o);

	public int hashCode();

	public Type getMinValue();

	public Type0 getValue();

	public void setValue(Type0 t);

	public Object clone() throws CloneNotSupportedException;

	/**
	 * <p>
	 * Computes the sum of the actual term and the given one. None of the both
	 * participating terms should be changed.
	 * </p>
	 * <p>
	 * <b>This operation throws an Illegal argument exception, if the operation
	 * couldn't be performed with the submitted term it throws a
	 * <code>IllegalArgumentException</code> and a
	 * <code>OperationNotSupportedExeption</code> if the operation isn't
	 * supported by this type of term at all</b>
	 * </p>
	 * 
	 * @param t
	 *            addend, a term that will be added
	 * @return the computed result
	 * @throws NullPointerException
	 *             if the therm is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if the operation couldn't be performed with the type of the
	 *             term
	 * @throws UnsupportedOperationException
	 *             if the operation isn't supported by the term at all
	 */
	public abstract Type add(final ITerm t);

	/**
	 * <p>
	 * Computes the difference of the actual term and the given one. None of the
	 * both paticipating terms should be changed.
	 * </p>
	 * <p>
	 * <b>This operation throws an Illegal argument exception, if the operation
	 * couldn't be performed with the submitted term it throws a
	 * <code>IllegalArgumentException</code> and a
	 * <code>OperationNotSupportedExeption</code> if the operation isn't
	 * supported by this type of term at all</b>
	 * </p>
	 * 
	 * @param t
	 *            the suptrahend of the operation
	 * @return the computed result
	 * @throws NullPointerException
	 *             if the therm is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if the operation couldn't be performed with the type of the
	 *             term
	 * @throws UnsupportedOperationException
	 *             if the operation isn't supported by the term at all
	 */
	public abstract Type subtract(final ITerm t);

	/**
	 * <p>
	 * Computes the product of the actual term and the given one. None of the
	 * both paticipating terms should be changed.
	 * </p>
	 * <p>
	 * <b>This operation throws an Illegal argument exception, if the operation
	 * couldn't be performed with the submitted term it throws a
	 * <code>IllegalArgumentException</code> and a
	 * <code>OperationNotSupportedExeption</code> if the operation isn't
	 * supported by this type of term at all</b>
	 * </p>
	 * 
	 * @param t
	 *            the factor of the operation
	 * @return the computed result
	 * @throws NullPointerException
	 *             if the therm is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if the operation couldn't be performed with the type of the
	 *             term
	 * @throws UnsupportedOperationException
	 *             if the operation isn't supported by the term at all
	 */
	public abstract Type multiply(final ITerm t);

	/**
	 * <p>
	 * Computes the quotient of the actual term and the given one. None of the
	 * both paticipating terms should be changed.
	 * </p>
	 * <p>
	 * <b>This operation throws an Illegal argument exception, if the operation
	 * couldn't be performed with the submitted term it throws a
	 * <code>IllegalArgumentException</code> and a
	 * <code>OperationNotSupportedExeption</code> if the operation isn't
	 * supported by this type of term at all</b>
	 * </p>
	 * 
	 * @param t
	 *            the divisor of the operation
	 * @return the computed result
	 * @throws NullPointerException
	 *             if the therm is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if the operation couldn't be performed with the type of the
	 *             term
	 * @throws UnsupportedOperationException
	 *             if the operation isn't supported by the term at all
	 */
	public abstract Type divide(final ITerm t);
}
