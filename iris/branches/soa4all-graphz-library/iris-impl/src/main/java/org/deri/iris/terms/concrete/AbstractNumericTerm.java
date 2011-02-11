/*
 * Copyright 2011, Semantic Technology Institute (STI) Innsbruck, 
 * University of Innsbruck, Technikerstrasse 21a, 6020 Innsbruck, Austria.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.deri.iris.terms.concrete;

import org.deri.iris.api.terms.INumericTerm;
import org.deri.iris.api.terms.ITerm;

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
