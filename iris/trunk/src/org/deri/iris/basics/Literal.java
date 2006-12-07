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
package org.deri.iris.basics;

import org.deri.iris.api.basics.IAtom;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.ITuple;

/**
 * Simple literal implementation.<br/><br/>$Id$
 * @author richi
 * 
 * Revision 1.1  26.07.2006 09:23:14  Darko Anicic, DERI Innsbruck
 */
public class Literal implements ILiteral<ILiteral> {

	private boolean positive = true;

	private IAtom atom = null;

	Literal(final IAtom atom) {
		this.atom = atom;
	}
	
	Literal(final boolean positive, final IAtom atom) {
		this.atom = atom;
		setPositive(positive);
	}
	
	public boolean isPositive() {
		return positive;
	}

	public void setPositive(boolean arg) {
		positive = arg;
	}

	public IPredicate getPredicate() {
		return atom.getPredicate();
	}

	public ITuple getTuple() {
		return this.atom.getTuple();
	}

	public IAtom getAtom() {
		return atom;
	}
	
	public boolean isGround() {
		return atom.isGround();
	}

	public int compareTo(final ILiteral o) {
		if ((positive != o.isPositive()) && positive) {
			return 1;
		} else if ((positive != o.isPositive()) && !positive) {
			return -1;
		}
		return atom.compareTo(o.getAtom());
	}

	public int hashCode() {
		int result = 17;
		result = result * 37 + atom.hashCode();
		result = result * 37 + (positive ? 1 : 0);
		return result;
	}

	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Literal)) {
			return false;
		}
		Literal l = (Literal) o;
		
		boolean b1 = atom.equals(l.atom);
		boolean b2 = positive == l.positive;
		
		return atom.equals(l.atom) && (positive == l.positive);
	}

	public String toString() {
		return (positive ? "" : "-") + atom;
	}

}
