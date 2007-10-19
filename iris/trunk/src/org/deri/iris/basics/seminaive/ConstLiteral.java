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
package org.deri.iris.basics.seminaive;

import static org.deri.iris.factory.Factory.BASIC;

import org.deri.iris.api.basics.IAtom;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.basics.BasicFactory;
import org.deri.iris.basics.Literal;

/**
 * <p>
 * Represents a constance. It is used during the rule rectification. For instance a rule:
 * </p>
 * <p>
 * p(?X, a) :- r(?X, ?Y)
 * </p>
 * <p>
 * will be translated to:
 * </p>
 * <p>
 * p(?X, ?Z) :- r(?X, ?Y), ?Z=a
 * </p>
 * <p>
 * where ?Z=a is a constance represented by an instance of this class and:
 * </p>
 * <p>
 * groundTerm = a
 * var = ?Z
 * </p>
 * @author Darko Anicic, DERI Innsbruck
 * @date 25.04.2007
 * 
 */
public class ConstLiteral implements ILiteral{
	
	private boolean positive = true;
	
	private IAtom atom = null;
	
	public ConstLiteral(final boolean positive, final ITerm groundTerm, final IVariable var){
		if (! groundTerm.isGround() || var == null) {
			throw new IllegalArgumentException("Constructor "
					+ "parameters are not specified correctly!");
		}
		this.positive = positive;
		this.atom = BasicFactory.getInstance().createAtom(
				BASIC.createPredicate("CONST", 2), 
				BASIC.createTuple(groundTerm, var));

	}

	public IAtom getAtom() {
		return this.atom;
	}

	public boolean isPositive() {
		return this.positive;
	}

	public void setPositive(boolean arg) {
		this.positive = arg;
	}

	public IPredicate getPredicate() {
		return this.atom.getPredicate();
	}

	public ITuple getTuple() {
		return this.atom.getTuple();
	}

	public boolean isBuiltin() {
		return false;
	}

	public boolean isGround() {
		return true;
	}
	
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("{");
		buffer.append(this.getAtom().getTuple().get(0).toString());
		buffer.append("}");
		buffer.append("(");
		buffer.append(this.getAtom().getTuple().get(1).toString());
		buffer.append(")");
		return buffer.toString();
	}

	public int compareTo(IAtom o) {
		throw new UnsupportedOperationException(
			"This method is not supported by this literal.");
	}
	
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Literal)) {
			return false;
		}
		Literal l = (Literal) o;
		if (!(l.getAtom() instanceof ConstLiteral)) {
			return false;
		}
		return atom.getTuple().equals(l.getAtom().getTuple()) && (positive == l.isPositive());
	}
}
