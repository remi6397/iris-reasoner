/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions.
 * 
 * Copyright (C) 2008 Semantic Technology Institute (STI) Innsbruck, 
 * University of Innsbruck, Technikerstrasse 21a, 6020 Innsbruck, Austria.
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
package org.deri.iris.utils;

import eu.soa4all.graph.Edge;

/**
 * <p>
 * Wrapper class for {@link Edge}. Overrides equals() and hashCode() for easy comparisons.
 * </p>
 * 
 * 
 * @author Christoph Fuchs (christoph dot fuchs at sti2 dot at)
 */
public class ComparableEdge<V, W> {

	private Edge<V, W> edge;

	public ComparableEdge(Edge<V, W> edge) {
		this.edge = edge;
	}

	public boolean equals(Object obj) {
		if (obj instanceof ComparableEdge) {
			@SuppressWarnings("unchecked")
			ComparableEdge<V, W> other = (ComparableEdge<V, W>) obj;

			return equalsCheckingNull(other.getDestination(),
					this.getDestination())
					&& equalsCheckingNull(other.getOrigin(), this.getOrigin())
					&& equalsCheckingNull(other.getWeight(), this.getWeight());
		}
		return false;
	}

	private W getWeight() {
		return edge.getWeight();
	}

	private V getOrigin() {
		return edge.getOrigin();
	}

	private V getDestination() {
		return edge.getDestination();
	}

	private boolean equalsCheckingNull(Object value1, Object value2) {
		if (value1 != null && value2 != null) {
			// Neither value is null, it is save to check for equality
			return value1.equals(value2);
		} else {
			// One of the values is null, check if both are null
			return (value1 == null && value2 == null);
		}
	}

	@Override
	public int hashCode() {
		return getOrigin().hashCode() + getDestination().hashCode()
				+ (getWeight() == null ? 0 : getWeight().hashCode());
	}

	@Override
	public String toString() {
		return getOrigin() + " =" + getWeight() + "=> " + getDestination();
	}
}
