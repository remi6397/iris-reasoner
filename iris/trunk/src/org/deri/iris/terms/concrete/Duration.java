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

package org.deri.iris.terms.concrete;

import org.deri.iris.api.terms.concrete.IDuration;

/**
 * <p>
 * Simple implementation of the IDuration.
 * </p>
 * <p>
 * $Id$
 * </p>
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision$
 */
public class Duration implements IDuration, Cloneable {

	/** Duration time in milliseconds. */
	private long millis = 0;

	/** Milliseconds per second. */
	private static final long MILLIS_PER_SECOND = 1000;

	/** Milliseconds per minute. */
	private static final long MILLIS_PER_MINUTE = MILLIS_PER_SECOND * 60;

	/** Milliseconds per hour. */
	private static final long MILLIS_PER_HOUR = MILLIS_PER_MINUTE * 60;

	/** Milliseconds per day. */
	private static final long MILLIS_PER_DAY = MILLIS_PER_HOUR * 24;

	/**
	 * Constructs a new duration.
	 * @param day the dayspan
	 * @param hour the hourspan
	 * @param minute the minutespan
	 * @param second the secondspan
	 */
	Duration(final int day, 
			final int hour, final int minute, final int second) {
		this(day * MILLIS_PER_DAY + 
				hour * MILLIS_PER_HOUR + 
				minute * MILLIS_PER_MINUTE + 
				second * MILLIS_PER_SECOND);
	}

	/**
	 * Contructs a new duration out of a given amount of milliseconds. The
	 * milliseconds will be round down to the next second.
	 * @param millis the millisecondspan
	 */
	Duration(final long millis) {
		this.millis = millis - millis % MILLIS_PER_SECOND;
	}

	public boolean equals(final Object obj) {
		if (!(obj instanceof Duration)) {
			return false;
		}
		return millis == ((Duration) obj).getValue().longValue();
	}

	public int getDay() {
		return (int) (millis / MILLIS_PER_DAY);
	}

	public int getHour() {
		return (int) (millis % MILLIS_PER_DAY / MILLIS_PER_HOUR);
	}

	public int getMinute() {
		return (int) (millis % MILLIS_PER_HOUR / MILLIS_PER_MINUTE);
	}

	public int getSecond() {
		return (int) (millis % MILLIS_PER_MINUTE / MILLIS_PER_SECOND);
	}

	public int hashCode() {
		return (int) (millis ^ (millis >>> 32));
	}

	/**
	 * <p>
	 * Returns a short string representation of this object. <b>The format
	 * of the returned string is subject to change.</b>
	 * </p>
	 * <p>
	 * The format is inspired by ISO 8601 (Representations of dates and times). 
	 * In short:
	 * <code>P&lt;days&gt;DT&lt;hours&gt;H&lt;minutes&gt;M&lt;seconds&gt;S</code>
	 * </p>
	 * @return the string representation
	 */
	public String toString() {
		return "P" + getDay() + "DT" + getHour() + "H" + getMinute() + "M" + getSecond() + "S";
	}

	public Object clone() {
		try {
			Duration di = (Duration) super.clone();
			return di;
		} catch (CloneNotSupportedException e) {
			assert false : "Object is always cloneable";
		}
		return null;
	}

	public int compareTo(IDuration o) {
		if (o == null) {
			return 1;
		}
		return Long.valueOf(millis).compareTo(o.getValue());
	}

	public boolean isGround() {
		return true;
	}

	public Long getValue() {
		return Long.valueOf(millis);
	}

	public void setValue(final Long millis) {
		if (millis == null) {
			throw new NullPointerException("The milliseconds must not be null");
		}
		this.millis = millis.longValue();
	}
}
