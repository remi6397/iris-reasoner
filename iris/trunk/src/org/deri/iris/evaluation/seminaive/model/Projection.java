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
package org.deri.iris.evaluation.seminaive.model;

import org.deri.iris.api.evaluation.seminaive.model.IProjection;

/**
 * 
 * @author Paco Garcia, University of Murcia
 * @date 01-sep-2006
 *
 */
public class Projection extends Composite implements IProjection{
	private int[] indexes = null;
	
	Projection(int[] indexes) {
		if (indexes == null) {
			throw new IllegalArgumentException("All constructor " +
					"parameters must not be specified (non null values");
		}
		
		this.indexes = indexes;
	}

	public int[] getIndexes() {
		return indexes;
	}
	
	public String toString() {
		String result = "PROJECTION[";
		for(int i = 0; i < indexes.length; i++)
		{
			result += new Integer(indexes[i]).toString() + " ";			
		}
		result += "] (" + this.getChildren().get(0).toString() + ")";
		
		return result;
	}
}
