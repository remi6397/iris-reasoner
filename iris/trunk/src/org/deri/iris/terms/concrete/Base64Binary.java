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

import org.deri.iris.api.terms.concrete.IBase64Binary;

/**
 * <p>
 * Simple implementation of the IBase64Binary.
 * </p>
 * <p>
 * $Id$
 * </p>
 * @author Richard Pöttler, richard dot poettler at deri dot org
 * @version $Revision$
 */
public class Base64Binary implements IBase64Binary, Cloneable {

	public static final Pattern PATTERN = Pattern.compile("([a-zA-Z0-9/+]{4})*"
			+ "(([a-zA-Z0-9/+]{2}[AEIMQUYcgkosw048]=)|"
			+ "([a-zA-Z0-9/+]{1}[AQgw]==))?");
	
	private String content = "";

	Base64Binary() {
	}

	Base64Binary(final String content) {
		this();
		setValue(content);
	}

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			assert false : "Object is always cloneable";
		}
		return null;
	}

	public int compareTo(IBase64Binary o) {
		if(o == null) {
			throw new NullPointerException("Can not compare with null");
		}
		return content.compareTo(o.getValue());
	}
	
	public boolean equals(final Object obj) {
		if (!(obj instanceof Base64Binary)) {
			return false;
		}
		Base64Binary b64 = (Base64Binary) obj;
		return content.equals(b64.content);
	}

	public String getValue() {
		return content;
	}

	public int hashCode() {
		return content.hashCode();
	}

	public void setValue(final String content) {
		if (PATTERN.matcher(content).matches()) {
			this.content = content;
		} else {
			throw new IllegalArgumentException("Couldn't parse " + content
					+ " to a valid Base64Binary");
		}
	}

	public String toString() {
		return getClass().getName() + "[content=" + getValue() + "]";
	}

	public boolean isGround() {
		return true;
	}

	public IBase64Binary getMinValue() {
		return new Base64Binary("");
	}
}
