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

import java.util.Collection;
import java.util.List;

import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;

/**
 * <p>
 * Represents an add operation. In at the evaluation time there must be only one
 * variable be left for computation, otherwise an exception will be thrown.
 * </p>
 * <p>
 * $Id: AddBuiltin.java,v 1.4 2007-05-03 11:42:07 darko_anicic Exp $
 * </p>
 * 
 * @author Richard Pöttler, richard dot poettler at deri dot org
 * @version $Revision: 1.4 $
 */
public class AddBuiltin extends AbstractBuiltin {

	/** The predicate defining this builtin. */
	private static final IPredicate PREDICATE = BASIC.createBuiltinPredicate(
			"ADD", 3);

	/**
	 * Constructs a builtin.
	 * 
	 * @param t0
	 *            the first term
	 * @param t1
	 *            the second term
	 * @param t2
	 *            the result of the add operation
	 * @throws NullPointerException
	 *             if one of the terms is {@code null}
	 */
	AddBuiltin(final ITerm t0, final ITerm t1, final ITerm t2) {
		super(PREDICATE, 3, t0, t1, t2);
	}

	public ITuple evaluate(final ITuple tup, final IVariable... vars) {
		// e.g., add(3, 4, ?X)
		if(this.getTerm(0).isGround() && this.getTerm(1).isGround() && ! this.getTerm(2).isGround()) {
			if(vars.length == 1){
				if(this.getTerm(2).equals(vars[0])){
					return BASIC.createTuple(BuiltinHelper.add(this.getTerm(0), this.getTerm(1)));
				}else{
					return null;
				}
			}else{
				throw new IllegalArgumentException("Expected length of input variable's array is 1!");
			}
		}else 
		// e.g., add(3, ?X, 4)	
		if(this.getTerm(0).isGround() && ! this.getTerm(1).isGround() && this.getTerm(2).isGround()) {
			if(vars.length == 1){
				if(this.getTerm(1).equals(vars[0])){
					return BASIC.createTuple(BuiltinHelper.subtract(this.getTerm(2), this.getTerm(0)));
				}else{
					return null;
				}
			}else{
				throw new IllegalArgumentException("Expected length of input variable's array is 1!");
			}
		}else 
		// e.g., add(?X, 3, 4)	
		if(! this.getTerm(0).isGround() && this.getTerm(1).isGround() && this.getTerm(2).isGround()) {
			if(vars.length == 0){
				return BASIC.createTuple(BuiltinHelper.subtract(this.getTerm(2), this.getTerm(1)));
			}
			if(vars.length == 1){
				if(this.getTerm(0).equals(vars[0])){
					return BASIC.createTuple(BuiltinHelper.subtract(this.getTerm(2), this.getTerm(1)));
				}else{
					return null;
				}
			}else{
				throw new IllegalArgumentException("Expected length of input variable's array is 1!");
			}
		}else 
		// e.g., add(?X, ?Y, 5)
		if(! this.getTerm(0).isGround() && ! this.getTerm(1).isGround() && this.getTerm(2).isGround()) {
			// for vars.length == 1, we calculate ?Y (for a given ?X=4) as 5 - ?X => ?Y=1
			if(vars.length == 1){
				if(this.getTerm(0).equals(vars[0])){
					return BASIC.createTuple(tup.getTerm(0), BuiltinHelper.subtract(this.getTerm(2),tup.getTerm(0)));
				}else if(this.getTerm(1).equals(vars[0])){
					return BASIC.createTuple(BuiltinHelper.subtract(this.getTerm(2),tup.getTerm(1)),tup.getTerm(1));
				}else {
					return null;
				}
			}
			// for vars.length == 2, we check whether ?X+5=?Y. <4,9> would pass and <5,9> wouldn't!
			if(vars.length == 2){
				if(this.getTerm(0).equals(vars[0]) && this.getTerm(1).equals(vars[1])){
					if(BuiltinHelper.add(tup.getTerm(0), tup.getTerm(1)).equals(this.getTerm(2))){
						return tup;
					}else {
						return null;
					}
				}
			}
			throw new IllegalArgumentException("Expected length of input variable's array is eiter 1 or 2!");
		}else 
		// e.g., add(?X, 5, ?Y)
		if(! this.getTerm(0).isGround() && this.getTerm(1).isGround() && ! this.getTerm(2).isGround()) {
			// for vars.length == 1, we calculate ?Y (for a given ?X=4) as a sum => ?Y=9
			if(vars.length == 1){
				if(this.getTerm(0).equals(vars[0])){
					return BASIC.createTuple(tup.getTerm(0), BuiltinHelper.add(tup.getTerm(0), this.getTerm(1)));
				}else if(this.getTerm(2).equals(vars[0])){
					return BASIC.createTuple(BuiltinHelper.subtract(tup.getTerm(0), this.getTerm(1)),tup.getTerm(0));
				}else {
					return null;
				}
			}
			// for vars.length == 2, we check whether ?X+5=?Y. <4,9> would pass and <5,9> wouldn't!
			if(vars.length == 2){
				if(this.getTerm(0).equals(vars[0]) && this.getTerm(2).equals(vars[1])){
					if(BuiltinHelper.add(tup.getTerm(0), this.getTerm(1)).equals(tup.getTerm(1))){
						return tup;
					}else {
						return null;
					}
				}
			}
			throw new IllegalArgumentException("Expected length of input variable's array is eiter 1 or 2!");
		}else 
		// e.g., add(5, ?X, ?Y)
		if(this.getTerm(0).isGround() && ! this.getTerm(1).isGround() && ! this.getTerm(2).isGround()) {
			// for vars.length == 1, we calculate ?Y (for a given ?X=4) as a sum => ?Y=9
			if(vars.length == 1){
				if(this.getTerm(1).equals(vars[0])){
					return BASIC.createTuple(this.getTerm(0), BuiltinHelper.add(tup.getTerm(0), this.getTerm(0)));
				}else if(this.getTerm(2).equals(vars[0])){
					return BASIC.createTuple(BuiltinHelper.subtract(tup.getTerm(0), this.getTerm(0)),tup.getTerm(0));
				}else {
					return null;
				}
			}
			// for vars.length == 2, we check whether 5+?X=?Y. <4,9> would pass and <5,9> wouldn't!
			if(vars.length == 2){
				if(this.getTerm(1).equals(vars[0]) && this.getTerm(2).equals(vars[1])){
					if(BuiltinHelper.add(tup.getTerm(0), this.getTerm(0)).equals(tup.getTerm(1))){
						return tup;
					}else {
						return null;
					}
				}
			}
			throw new IllegalArgumentException("Expected length of input variable's array is eiter 1 or 2!");
		}else 
		// e.g., add(?X, ?Y, ?Z)
		if(! this.getTerm(0).isGround() && ! this.getTerm(1).isGround() && ! this.getTerm(2).isGround()) {
			if(vars.length != 2)
				throw new IllegalArgumentException("Expected length of input variable's array is 2!");
			// e.g., ?X=3, ?Y=4, ?Z=?
			if(this.getTerm(0).equals(vars[0]) && this.getTerm(1).equals(vars[1])){
				return BASIC.createTuple(tup.getTerm(0), tup.getTerm(1), BuiltinHelper.add(tup.getTerm(0), tup.getTerm(1)));
			}else
			// e.g., ?X=3, ?Y=?, ?Z=4
			if(this.getTerm(0).equals(vars[0]) && this.getTerm(2).equals(vars[1])){
				return BASIC.createTuple(tup.getTerm(0), BuiltinHelper.subtract(tup.getTerm(2), tup.getTerm(1)), tup.getTerm(1));
			}else
			// e.g., ?X=?, ?Y=3, ?Z=4
			if(this.getTerm(1).equals(vars[0]) && this.getTerm(2).equals(vars[1])){
				return BASIC.createTuple(BuiltinHelper.add(tup.getTerm(0), tup.getTerm(1)), tup.getTerm(0), tup.getTerm(1));
			}
		}else {
			throw new IllegalArgumentException("The collection is not be evaluable.");
		}
		return null;
	}

//	 TODO Remove this once you correct List<ITuple> evaluate(Collection<ITuple> t) in the IBuiltInAtom
	public List<ITuple> evaluate(Collection<ITuple> t) {
		return null;
	}
}
