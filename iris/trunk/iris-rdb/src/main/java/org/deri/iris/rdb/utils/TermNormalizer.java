package org.deri.iris.rdb.utils;

import java.math.BigDecimal;

import org.deri.iris.api.terms.IConcreteTerm;
import org.deri.iris.api.terms.INumericTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.concrete.IDateTerm;
import org.deri.iris.api.terms.concrete.IDateTime;
import org.deri.iris.api.terms.concrete.IDuration;
import org.deri.iris.builtins.datatype.ToDateTimeBuiltin;
import org.deri.iris.builtins.datatype.ToDurationBuiltin;

public class TermNormalizer {

	public String createString(ITerm term) {
		if (term == null || !(term instanceof IConcreteTerm)) {
			return null;
		}

		String result = null;

		if (term instanceof IDateTime) {
			// We only have 2 implementations of IDateTime: DateTime and
			// DateTimeStamp which both use the same toCanonicalString method.
			IDateTime dateTime = (IDateTime) term;
			result = dateTime.toCanonicalString();
		}

		if (term instanceof IDateTerm) {
			// Cast a date to a date time and return the canonical
			// representation of date time.
			IDateTerm date = (IDateTerm) term;
			IDateTime dateTime = ToDateTimeBuiltin.toDateTime(date);
			result = dateTime.toCanonicalString();
		}

		if (term instanceof IDuration) {
			// Cast the duration to duration in order to have a "full" duration
			// for a year month and day time duration.
			IDuration duration = ToDurationBuiltin.toDuration(term);

			if (duration != null) {
				result = duration.toCanonicalString();
			}
		}

		if (term instanceof INumericTerm) {
			// All numeric terms are represented as a decimal number.
			INumericTerm numeric = (INumericTerm) term;
			BigDecimal value = numeric.getValue();
			return value.toString();
		}

		if (result == null) {
			result = ((IConcreteTerm) term).toCanonicalString();
		}

		if (result != null) {
			return result.trim();
		}

		return null;
	}

}
