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

// TODO: check whether URI.equals() is like URL.equals() (afaik URL needs network access)

import java.net.URI;
import java.net.URISyntaxException;

import org.deri.iris.api.terms.concrete.IIri;

public class Iri implements IIri, Cloneable {

	private URI uri = null;

	public Iri(final String str) {
		setValue(str);
	}

	public Iri(final URI uri) {
		setValue(uri);
	}

	public Object clone() {
		try {
			Iri i = (Iri) super.clone();
			i.uri = new URI(uri.toString());
			return i;
		} catch (CloneNotSupportedException e) {
			assert true : "Can not happen";
		} catch (URISyntaxException e) {
			//TODO: it's nasty to swallow exceptions!
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

	public void setValue(final String arg) {
		try {
			setValue(new URI(arg));
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException("Wasn't able to parse: "
					+ arg.trim());
		}
	}

	protected void setValue(final URI uri) {
		this.uri = uri;
	}

	public String toString() {
		return getClass().getName() + "[uri=" + getValue() + "]";
	}

	public boolean isGround() {
		return true;
	}
}
