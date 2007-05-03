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

import java.util.Collection;
import java.util.List;

import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.INumericTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.factory.Factory;

/**
 * <p>
 * Built-in to compare two terms and determine which one is bigger.
 * </p>
 * <p>
 * REMARK: Please note that the current implementation works only with
 * IntegerTerm data type.
 * </p>
 * <p>
 * $Id: LessBuiltin.java,v 1.4 2007-05-03 11:45:00 darko_anicic Exp $
 * </p>
 * 
 * @author Richard Pöttler, richard dot poettler at deri dot org
 * @author Darko Anicic, DERI Innsbruck
 * 
 * @version $Revision: 1.4 $
 */
public class LessBuiltin extends AbstractBuiltin {

	/** The predicate defining this builtin. */
	private static final IPredicate PREDICATE = Factory.BASIC.createBuiltinPredicate(
			"LESS", 2);

	/**
	 * Constructs a builtin.
	 * 
	 * @param t0
	 *            the first term
	 * @param t1
	 *            the second term
	 * @throws NullPointerException
	 *             if one of the terms is null
	 */
	LessBuiltin(final ITerm t0, final ITerm t1) {
		super(PREDICATE, t0, t1);
	}

	/**
	 * This is an empty method stub to keep the src directory compileable.
	 * @return at the moment it always returns <code>null</code>
	 */
	public List<ITuple> evaluate(final Collection<ITuple> t) {
		// TODO: not implemented yet
		return null;
	}
	
	public ITuple evaluate(ITuple tup, IVariable... vars) {
		// e.g., less(3, 4)
		if(this.getTerm(0).isGround() && this.getTerm(1).isGround()) {
			if(evaluate(this.getTerm(0), this.getTerm(1))){
				return tup;
			}else{
				return null;
			}
		}else
		// e.g., less(?X, 4)
		if(! this.getTerm(0).isGround() && this.getTerm(1).isGround()) {
			if(vars.length != 1)
				throw new IllegalArgumentException("Expected length of input variable's array is 1!");
			if(evaluate(tup.getTerm(0), this.getTerm(1))){
				return tup;
			}else{
				return null;
			}
		}else
		// e.g., less(4,?X)
		if(this.getTerm(0).isGround() && ! this.getTerm(1).isGround()) {
			if(vars.length != 1)
				throw new IllegalArgumentException("Expected length of input variable's array is 1!");
			if(evaluate(this.getTerm(0), tup.getTerm(0))){
				return tup;
			}else{
				return null;
			}
		}else
		// e.g., less(?X,?Y)
		if(! this.getTerm(0).isGround() && ! this.getTerm(1).isGround()) {
			if(vars.length != 2)
				throw new IllegalArgumentException("Expected length of input variable's array is 2!");
			if(evaluate(tup.getTerm(0), tup.getTerm(1))){
				return tup;
			}else{
				return null;
			}
		}
		return null;
	}

	/**
	 * Runns the evaluation. If the two terms are <code>INumberTerm</code>s
	 * their values will be converted to doubles, otherwise they will be
	 * compared.
	 * 
	 * @param t0	A term to be compared with t1.
	 * @param t1	A term to be compared with t0.
	 * @return <code>true</code> if the two terms are comparable and the
	 *         second one is bigger than the first one, otherwise
	 *         <code>false</code>
	 */
	public boolean evaluate(ITerm t0, ITerm t1) {
		if (isEvaluable()) {
			if ((t0 instanceof INumericTerm)
					&& (t1 instanceof INumericTerm)) {
				return BuiltinHelper.numbersCompare((INumericTerm) t0,
						(INumericTerm) t1) < 0;
			}
			return t0.compareTo(t1) < 0;
		}
		return false;
	}
}
