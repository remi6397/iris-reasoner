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

import java.net.URI;
import java.net.URISyntaxException;

import org.deri.iris.api.terms.concrete.IIri;

/**
 * Represents an iri. </br></br>$Id$
 * 
 * @author richi
 * @version $Revision$
 * @date $Date$
 */
public class Iri implements IIri, Cloneable {

	/** the uri represented by this object */
	private URI uri;

	/**
	 * Constructs a new iri.
	 * 
	 * @param arg
	 *            the string of the uri
	 * @throws NullPointerException
	 *             if the string is null
	 * @throws IllegalArgumentException
	 *             if the string couldn't be parsed to an URI
	 */
	Iri(final String str) {
		setValue(str);
	}

	/**
	 * Constructs a new iri.
	 * 
	 * @param uri
	 *            the new uri
	 * @throws NullPointerException
	 *             if the uri is null
	 */
	Iri(final URI uri) {
		setValue(uri);
	}

	public Object clone() {
		try {
			Iri i = (Iri) super.clone();
			i.uri = new URI(uri.toString());
			return i;
		} catch (CloneNotSupportedException e) {
			assert false : "Object is always cloneable";
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(e);
		}
		return null;
	}

	public int compareTo(IIri o) {
		if (o == null) {
			throw new NullPointerException("Can not compare with null");
		}
		return uri.compareTo(o.getURI());
	}

	public boolean equals(final Object obj) {
		if (!(obj instanceof Iri)) {
			return false;
		}
		Iri i = (Iri) obj;
		return i.uri.equals(uri);
	}

	public URI getURI() {
		return uri;
	}

	public String getValue() {
		return getURI().toString();
	}

	public int hashCode() {
		return uri.hashCode();
	}

	/**
	 * Sets the uri.
	 * 
	 * @param arg
	 *            the string of the uri
	 * @throws NullPointerException
	 *             if the string is null
	 * @throws IllegalArgumentException
	 *             if the string couldn't be parsed to an URI
	 */
	public void setValue(final String arg) {
		if (arg == null) {
			throw new NullPointerException("arg must not be null");
		}
		try {
			setValue(new URI(arg));
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException("Wasn't able to parse: "
					+ arg.trim());
		}
	}

	/**
	 * Sets the uri.
	 * 
	 * @param uri
	 *            the new uri
	 * @throws NullPointerException
	 *             if the uri is null
	 */
	public void setValue(final URI uri) {
		if (uri == null) {
			throw new NullPointerException("The value must not be null");
		}
		this.uri = uri;
	}

	public String toString() {
		return uri.toString();
	}

	public boolean isGround() {
		return true;
	}

	public IIri getMinValue() {
		return new Iri("");
	}
}
