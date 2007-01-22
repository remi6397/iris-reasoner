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
package org.deri.iris.api.terms;

/**
 * <p>
 * An interface which defines a term. A term is a name for an object
 * in the universe of discourse. There are three types of terms:
 * 
 * <ul>
 * <li> variables</li>
 * <li> constants</li>
 * <li> constructed terms (functional symbols)</li>
 * </ul>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date 14.11.2005 12:09:01
 */

public interface ITerm<Type extends ITerm, Type0> extends Comparable<Type> {

	/**
	 * Checks whether the term is ground (a term with no variables).
	 * 
	 * @return	<code>true</code> if the term is ground, 
	 * 			otherwise <code>false</code>.
	 */
	public boolean isGround();
	
	/**
	 * Returns a vale of the term.
	 * 
	 * @return	The term value.
	 */
	public Type0 getValue();

	/**
	 * Sets a vale of the term.
	 * 
	 * @param t The new value of the term.
	 */
	public void setValue(Type0 t);

	/**
	 * @deprecated	This method is used in some implementations of 
	 * 				operations on relations. Use implementations of 
	 * 				relational operations which do not use this method.
	 * @return		
	 * 				Minimal value a term of a certain type (e.g. for an 
	 * 				Integer this is: 0x80000000). 
	 */
	public Type getMinValue();

	/**
	 * Creates and returns a copy of this object.
	 * 
	 * @return	A clone of this instance.
	 * @throws CloneNotSupportedException
	 */
	public Object clone() throws CloneNotSupportedException;

	/**
	 * Returns a String object representing this term's value.
	 * 
	 * @return	A string representation of the value of this term.
	 */
	public String toString();

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
