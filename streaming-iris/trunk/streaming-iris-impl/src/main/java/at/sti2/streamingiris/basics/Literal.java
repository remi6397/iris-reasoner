package at.sti2.streamingiris.basics;

import at.sti2.streamingiris.api.basics.IAtom;
import at.sti2.streamingiris.api.basics.ILiteral;

/**
 * <p>
 * Simple literal implementation.
 * </p>
 * <p>
 * $Id$
 * </p>
 * 
 * @author Richard Pöttler, richard dot poettler at deri dot org
 * @author Darko Anicic, DERI Innsbruck
 * @version $Revision$
 */
public class Literal implements ILiteral {

	private final boolean positive;

	private final IAtom atom;

	Literal(final IAtom atom) {
		this(true, atom);
	}

	Literal(final boolean positive, final IAtom atom) {
		this.atom = atom;
		this.positive = positive;
	}

	public boolean isPositive() {
		return positive;
	}

	public IAtom getAtom() {
		return atom;
	}

	public int compareTo(final ILiteral oo) {
		ILiteral o = (ILiteral) oo;
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
		return atom.equals(l.atom) && (positive == l.positive);
	}

	public String toString() {
		return (positive ? "" : "!") + atom;
	}
}
