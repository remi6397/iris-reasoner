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

//TODO: maybe overwrite the evaluate method (maybe to return always true)

import javax.naming.OperationNotSupportedException;

import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.factory.Factory;

/**
 * <p>
 * Builtin to compute the difference of two terms.
 * </p>
 * <p>
 * $Id: MultiplyBuiltin.java,v 1.2 2006-11-14 17:21:15 adi Exp $
 * </p>
 * 
 * @author richi
 * @version $Revision: 1.2 $
 * @date $Date: 2006-11-14 17:21:15 $
 */
public class MultiplyBuiltin extends AbstractBuiltin {

	/** The predicate defining this builtin. */
	private static final IPredicate PREDICATE = Factory.BASIC.createBuiltinPredicate(
			"MULTIPLY", 3);

	/**
	 * Constructs a builtin.
	 * 
	 * @param t0
	 *            the first term
	 * @param t1
	 *            the second term
	 * @throws NullPointerException
	 *             if one of the terms is {@code null}
	 */
	MultiplyBuiltin(final ITerm t0, final ITerm t1) {
		super(PREDICATE, 2, t0, t1);
	}

	/**
	 * <p>
	 * Runns the evaluation. This method used the
	 * {@code ITerm.multiply(ITerm)} method for evaluation and stores the
	 * result at the index 2.
	 * </p>
	 * 
	 * @return {@code true} if the evalueation succeeded
	 * @throws OperationNotSupportedException
	 *             if the first term doesn't support the {@code multiply(ITerm)}
	 *             method at all
	 * @throws IllegalArgumentException
	 *             if the first term doesn't support the {@code multiply(ITerm)}
	 *             method with the type of the second term
	 * @see ITerm#multiply(ITerm)
	 */
	public boolean evaluate() {
		setTerm(2, getTerm(0).multiply(getTerm(1)));
		return true;
	}
}
