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
