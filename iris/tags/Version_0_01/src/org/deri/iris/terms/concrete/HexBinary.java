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

import java.util.regex.Pattern;

import org.deri.iris.api.terms.concrete.IHexBinary;

/**
 * Interface or class description
 * 
 * Created on 04.04.2006
 * Committed by $Author$
 * $Source$,
 * 
 * @author Richard Pöttler
 * 
 * @version $Revision$ $Date$
 */
public class HexBinary implements IHexBinary, Cloneable {

	public static final Pattern PATTERN = Pattern.compile("([\\dA-F]{2})+");

	private String content = "";

	HexBinary(final String content) {
		setValue(content);
	}

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			assert true : "Can not happen";
		}
		return null;
	}

	public int compareTo(IHexBinary o) {
		if (o == null) {
			throw new NullPointerException("Can not compare with null");
		}
		return getValue().compareTo(o.getValue());
	}

	public boolean equals(final Object obj) {
		if (!(obj instanceof HexBinary)) {
			return false;
		}
		HexBinary hb = (HexBinary)obj;
		return content.equals(hb.content);
	}

	public String getValue() {
		return content;
	}
	
	public int hashCode() {
		return content.hashCode();
	}

	public void setValue(final String arg) {
		final String sStr = arg.toUpperCase();
		if (PATTERN.matcher(sStr).matches()) {
			this.content = sStr;
		} else {
			throw new IllegalArgumentException(
					"Couldn't parse "
							+ sStr
							+ " to a valid HexBinary. The String must have the pattern "
							+ PATTERN.pattern());
		}
	}

	public String toString() {
		return getClass().getName() + "[content=" + getValue() + "]";
	}

	public boolean isGround() {
		return true;
	}

	public IHexBinary getMinValue() {
		return new HexBinary("00");
	}
}
