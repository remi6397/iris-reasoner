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
package org.deri.iris.api.factory;

import org.deri.iris.api.builtins.IBuiltInAtom;
import org.deri.iris.api.terms.ITerm;

/**
 * <p>
 * An interface that can be used to create set of built-ins supported 
 * by this engine.
 * </p>
 * <p>
 * $Id: IBuiltInsFactory.java,v 1.4 2007-01-22 12:45:45 darko Exp $
 * </p>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @author richi
 * @date 17.03.2006 11:55:35
 * @version $Revision: 1.4 $
 */
public interface IBuiltInsFactory {

	/**
	 * Creates an equal builtin.
	 * 
	 * @param t0
	 *            the first term
	 * @param t1
	 *            the second term
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 */
	public abstract IBuiltInAtom createEqual(final ITerm t0, final ITerm t1);

	/**
	 * Creates an unequal builtin.
	 * 
	 * @param t0
	 *            the first term
	 * @param t1
	 *            the second term
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 */
	public abstract IBuiltInAtom createUnequal(final ITerm t0, final ITerm t1);

	/**
	 * Creates a less builtin.
	 * 
	 * @param t0
	 *            the first term
	 * @param t1
	 *            the second term
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 */
	public abstract IBuiltInAtom createLess(final ITerm t0, final ITerm t1);

	/**
	 * Creates an less-equal builtin.
	 * 
	 * @param t0
	 *            the first term
	 * @param t1
	 *            the second term
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 */
	public abstract IBuiltInAtom createLessEqual(final ITerm t0, final ITerm t1);

	/**
	 * Creates a greater builtin.
	 * 
	 * @param t0
	 *            the first term
	 * @param t1
	 *            the second term
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 */
	public abstract IBuiltInAtom createGreater(final ITerm t0, final ITerm t1);

	/**
	 * Creates a greater-equal builtin.
	 * 
	 * @param t0
	 *            the first term
	 * @param t1
	 *            the second term
	 * @return the builtin
	 * @throws NullPointerException
	 *             if any of the terms is <code>null</code>
	 */
	public abstract IBuiltInAtom createGreaterEqual(final ITerm t0,
			final ITerm t1);
}
