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
