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

import org.deri.iris.api.terms.concrete.IIri;
import org.deri.iris.api.terms.concrete.ISqName;

/**
 * Represents an sqname. </br></br>$Id$
 * 
 * @author richi
 * @version $Revision$
 * @date $Date$
 * 
 */
public class SqName implements ISqName, Cloneable {

	/** Namespace for this sqname */
	private IIri namespace;

	/** name for this sqname */
	private String name;

	/**
	 * Constructs a sqname. The format of the string must be
	 * <code>&lt;namespace&gt;#&lt;name&gt;</code>.
	 * 
	 * @param str
	 *            the string to parse
	 * @throws NullPointerException
	 *             if arg is null
	 * @throws IllegalArgumentException
	 *             if the string got an invalid format
	 */
	public SqName(final String str) {
		setValue(str);
	}

	/**
	 * Contructs a sqname.
	 * 
	 * @param namespace
	 *            for the sqname
	 * @param name
	 *            for the sqname
	 * @throws NullPointerException
	 *             if namespace or name is null
	 * @throws IllegalArgumentException
	 *             if name is an empty stirng
	 */
	public SqName(final String namespace, final String name) {
		this(new Iri(namespace), name);
	}

	/**
	 * Contructs a sqname.
	 * 
	 * @param namespace
	 *            for the sqname
	 * @param name
	 *            for the sqname
	 * @throws NullPointerException
	 *             if namespace or name is null
	 * @throws IllegalArgumentException
	 *             if name is an empty stirng
	 */
	public SqName(final IIri namespace, final String name) {
		setNamespace(namespace);
		setName(name);
	}

	/**
	 * Sets the namespace.
	 * 
	 * @param iri
	 *            the namespace for this sqname
	 * @throws NullPointerException
	 *             if the iri is null
	 */
	protected void setNamespace(final IIri iri) {
		if (iri == null) {
			throw new NullPointerException("The iri must not be null");
		}
		this.namespace = iri;
	}

	/**
	 * Sets the name.
	 * 
	 * @param str
	 *            the name for this sqname
	 * @throws NullPointerException
	 *             if the str is null
	 * @throws IllegalArgumentException
	 *             if the name is an empty string
	 */
	protected void setName(final String str) {
		if (str == null) {
			throw new NullPointerException("The srt must not be null");
		}
		if (str.trim().length() <= 0) {
			throw new IllegalArgumentException(
					"The name must not be null and must be longer than 0");
		}
		this.name = str.trim();
	}

	public IIri getNamespace() {
		return this.namespace;
	}

	public String getName() {
		return this.name;
	}

	public String toString() {
		return getClass().getName() + "[namespace=" + getNamespace().getValue()
				+ ",name=" + getName() + "]";
	}

	public boolean equals(final Object obj) {
		if (!(obj instanceof SqName)) {
			return false;
		}
		SqName sname = (SqName) obj;
		return sname.name.equals(name) && sname.namespace.equals(namespace);
	}

	public int hashCode() {
		int result = namespace.hashCode();
		result = result * 37 + name.hashCode();
		return result;
	}

	public Object clone() {
		try {
			SqName sq = (SqName) super.clone();
			sq.namespace = (Iri) ((Iri) namespace).clone();
			return sq;
		} catch (CloneNotSupportedException e) {
			assert true : "Will never happen";
		}
		return null;
	}

	/**
	 * Sets the value for this sqname. The format of the string must be
	 * <code>&lt;namespace&gt;#&lt;name&gt;</code>.
	 * 
	 * @param arg
	 *            the string to parse
	 * @throws NullPointerException
	 *             if arg is null
	 * @throws IllegalArgumentException
	 *             if the string got an invalid format
	 */
	public void setValue(final String arg) {
		if (arg == null) {
			throw new NullPointerException("arg must not be null");
		}
		final String[] frags = arg.split("#");
		if (frags.length < 2) {
			throw new IllegalArgumentException(
					"There must be at least one '#' in the string");
		}
		setNamespace(new Iri(frags[0]));
		setName(frags[1]);
	}

	public String getValue() {
		return getNamespace().getValue() + "#" + getName();
	}

	public int compareTo(ISqName o) {
		if (o == null) {
			throw new NullPointerException("Can not compare with null");
		}

		int iResult = getNamespace().compareTo(o.getNamespace());
		if (iResult != 0) {
			return iResult;
		}
		return getName().compareTo(o.getName());
	}

	public boolean isGround() {
		return true;
	}

	public ISqName getMinValue() {
		return new SqName("#0");
	}
}
