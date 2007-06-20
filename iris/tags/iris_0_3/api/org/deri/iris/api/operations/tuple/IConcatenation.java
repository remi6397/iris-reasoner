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
package org.deri.iris.api.operations.tuple;

import org.deri.iris.api.basics.ITuple;

/**
 * <p>
 * An interface for a concatenation operation. The operation creates
 * a new tuple concatenating two tuples. 
 * </p>
 * 
 * @author Darko Anicic
 * @date  11.04.2006 @time  15:45:10
 *
 * @version $Revision: 1.5 $ $Date: 2007-01-22 12:46:18 $
 */
public interface IConcatenation {

	/**
	 * Concatenates two tuples.
	 * 
	 * @param arg0
	 * 				The first tuple to be concatenated.
	 * @param arg1
	 * 				The second tuple to be concatenated.
	 * @return
	 * 				The concatenated tuple.
	 */
	public ITuple concatenate(final ITuple arg0, final ITuple arg1);
	
	/**
	 * @param arg0	The first tuple to be concatenated.
	 * @param arg1	The second tuple to be concatenated.
	 * @param pInds
	 * 				Defines indexes which the projection operation
	 * 				will be applied on. If not specified, the join 
	 * 				tuples will be simple merged.
	 * @return		
	 * 				The concatenated tuple.
	 */
	public ITuple concatenate(final ITuple arg0, final ITuple arg1, 
			final int[] pInds);
}
