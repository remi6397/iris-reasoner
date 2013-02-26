package at.sti2.streamingiris.terms.concrete;

import at.sti2.streamingiris.api.terms.INumericTerm;
import at.sti2.streamingiris.api.terms.ITerm;

/**
 * @author Adrian Marte
 */
public abstract class AbstractNumericTerm implements INumericTerm {

	public boolean isGround() {
		return true;
	}

	@Override
	public boolean equals(Object thatObject) {
		if (thatObject == null || !(thatObject instanceof INumericTerm)) {
			return false;
		}

		INumericTerm thatNumericTerm = (INumericTerm) thatObject;

		if ((isNotANumber() && thatNumericTerm.isNotANumber())
				|| (isPositiveInfinity() && thatNumericTerm
						.isPositiveInfinity())
				|| (isNegativeInfinity() && thatNumericTerm
						.isNegativeInfinity())) {
			return true;
		}

		return compareTo(thatNumericTerm) == 0;
	}

	public int compareTo(ITerm thatObject) {
		if (thatObject == null || !(thatObject instanceof INumericTerm)) {
			return 1;
		}

		INumericTerm thatNumeric = (INumericTerm) thatObject;

		// NaN = NaN
		if (isNotANumber() && thatNumeric.isNotANumber()) {
			return 0;
		}
		// NaN > non-NaN values
		else if (isNotANumber()) {
			return 1;
		}
		// non-NaN values < NaN
		else if (thatNumeric.isNotANumber()) {
			return -1;
		}

		// -INF = -INF
		if (isNegativeInfinity() && thatNumeric.isNegativeInfinity()) {
			return 0;
		}
		// +INF = +INF
		else if (isPositiveInfinity() && thatNumeric.isPositiveInfinity()) {
			return 0;
		}
		// non-+INF < +INF
		else if (thatNumeric.isPositiveInfinity()) {
			return -1;
		}
		// non--INF > -INF
		else if (thatNumeric.isNegativeInfinity()) {
			return 1;
		}
		// -INF < non--INF
		else if (isNegativeInfinity()) {
			return -1;
		}
		// +INF > non-+INF
		else if (isPositiveInfinity()) {
			return 1;
		}

		INumericTerm thatNumericTerm = (INumericTerm) thatObject;
		return getValue().compareTo(thatNumericTerm.getValue());
	}

	@Override
	public int hashCode() {
		return getValue().hashCode();
	}

	@Override
	public String toString() {
		return toCanonicalString();
	}

	public String toCanonicalString() {
		return getValue().toPlainString();
	}

}
