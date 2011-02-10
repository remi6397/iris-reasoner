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

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;


import eu.soa4all.graph.Edge;

/**
 * <p>
 * Utility class to compare sets of {@link Edge}s. The graphz library does not
 * allow for comparable edges (because multi-graphs are allowed). This utility
 * class tackles this problem by providing static methods which wrap the
 * Edge implementation in a ComparableEdge, which overrides hashCode() and
 * equals().
 * </p>
 * 
 * 
 * @author Christoph Fuchs (christoph dot fuchs at sti2 dot at)
 */
public class EdgeSetEquality {

	/**
	 * Takes two Sets (containing {@link Edge}s) and compares them for equality
	 * by wrapping each Edge instance in a {@link ComparableEdge}.
	 * 
	 * @see Assert#assertEquals(String, Object, Object)
	 */
	public static <V, W> void assertEdgeSetEquality(String message,
			Set<Edge<V, W>> edges1,
			Set<Edge<V, W>> edges2) {
		
		HashSet<ComparableEdge<V, W>> tmpSet1 = new HashSet<ComparableEdge<V, W>>();
		HashSet<ComparableEdge<V, W>> tmpSet2 = new HashSet<ComparableEdge<V, W>>();

		for (Edge<V, W> edge : edges1) {
			ComparableEdge<V, W> comparableEdge = new ComparableEdge<V, W>(edge);
			tmpSet1.add(comparableEdge);
		}
		
		for (Edge<V, W> edge : edges2) {
			ComparableEdge<V, W> comparableEdge = new ComparableEdge<V, W>(edge);
			tmpSet2.add(comparableEdge);
		}
		
		Assert.assertEquals(message, tmpSet1, tmpSet2);
	}
	
}
