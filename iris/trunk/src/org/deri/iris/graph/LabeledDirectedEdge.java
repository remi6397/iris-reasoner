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
package org.deri.iris.graph;

import org._3pq.jgrapht.edge.DirectedEdge;

// TODO: implement LabeledElement (it doesn't support generics -> do it in jgraphT v0.7)

/**
 * This class represents a simple implementation of a labeled edge.<br/><br/>
 * <b>NOTE: do not use this class outside of this project! We don't know whether
 * to keep this class in the api!</b>
 * 
 * <br/><br/>$Id$
 * 
 * @author richi
 * @version $Revision$
 * 
 */
public class LabeledDirectedEdge<Type> extends DirectedEdge {

	/** label of this edge */
	private Type label = null;

	private static final long serialVersionUID = -3260958362372872661L;

	/**
	 * A simple constructor which sets the source and the taget.
	 * 
	 * @param source
	 *            the source vertex
	 * @param target
	 *            the target vertex
	 */
	public LabeledDirectedEdge(final Object source, final Object target) {
		super(source, target);
	}

	/**
	 * A simple constructor which sets the source, taget and the label.
	 * 
	 * @param source
	 *            the source vertex
	 * @param target
	 *            the target vertex
	 * @param label
	 *            the label to set
	 */
	public LabeledDirectedEdge(final Object source, final Object target,
			final Type label) {
		super(source, target);
		setLabel(label);
	}

	/**
	 * This method sets the label of the edge
	 * 
	 * @param label
	 *            the label to set
	 */
	public void setLabel(Type label) {
		this.label = label;
	}

	/**
	 * Returns the actual label of the edge
	 * 
	 * @return the label
	 */
	public Type getLabel() {
		return label;
	}

	/**
	 * Returns whether there is actually a label set
	 * 
	 * @return true if the label is not null, otherwise false
	 */
	public boolean hasLabel() {
		return label != null;
	}

	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof LabeledDirectedEdge)) {
			return false;
		}
		LabeledDirectedEdge le = (LabeledDirectedEdge) o;

		return le.getSource().equals(getSource())
				&& le.getTarget().equals(getTarget())
				&& label.equals(le.label)
				&& (Double.doubleToLongBits(getWeight()) == Double
						.doubleToLongBits(le.getWeight()));
	}

	/**
	 * Returns a simple string representation of this labeled directed edge.
	 * <br/><b>The subject of the stringrepresentation is to change.</b> <br/>An
	 * example String could be: <code>source->(label)->target<code>.
	 */
	public String toString() {
		return getSource() + " ->( " + label + " )-> " + getTarget();
	}
}
