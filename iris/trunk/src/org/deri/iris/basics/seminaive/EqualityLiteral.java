package org.deri.iris.basics.seminaive;

import org.deri.iris.api.basics.IAtom;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.basics.BasicFactory;
import org.deri.iris.basics.Literal;

// TODO: Remmove this class and its appearance from the program
public class EqualityLiteral implements ILiteral<ILiteral>{
	public static final String EQUALS = "=";
	public static final String NOT_EQUALS = "!=";
	public static final String LESS_THAN = "<";
	public static final String GREATER_THAN = ">";
	public static final String LESS_OR_EQUAL = "<=";
	public static final String GREATER_OR_EQUAL = ">=";

	private boolean positive = true;
	private IAtom atom = null;

	public EqualityLiteral(final String predicate,final ITuple tuple) {
		IPredicate p = BasicFactory.getInstance().createPredicate(predicate, tuple.getArity());
		this.atom = BasicFactory.getInstance().createAtom(p,tuple);
	}
	
	public EqualityLiteral(final boolean positive, final String predicate,final ITuple tuple) {
		this(predicate, tuple);
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
		
		boolean b1 = atom.equals(l.getAtom());
		boolean b2 = this.isPositive() == l.isPositive();
		
		return b1 && b2;
	}

	public String toString() {
		return (positive ? "" : "-") + atom;
	}

}
