/*
 * MINS (Mins Is Not Silri) A Prolog Egine based on the Silri  
 * 
 * Copyright (C) 1999-2005  Juergen Angele and Stefan Decker
 *                          University of Innsbruck, Austria  
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.deri.iris.api.operations.tuple;

import org.deri.iris.api.basics.ITuple;

/**
 * Interface or class description
 *
 * Interface of a concatenation operation used to promote modularity 
 * of the inference engine.
 * 
 * @author Darko Anicic
 * @date  11.04.2006 @time  15:45:10
 *
 * @version $Revision: 1.4 $ $Date: 2006-07-27 12:42:44 $
 */
public interface IConcatenation {

	/**
	 * Creates a new tuple concatenating two tuples. 
	 * 
	 * @param arg0
	 * 				- the first tuple to be concatenated.
	 * @param arg1
	 * 				- the second tuple to be concatenated.
	 * @return
	 * 				- concatenated tuple.
	 */
	public ITuple concatenate(final ITuple arg0, final ITuple arg1);
	
	/**
	 * @param arg0
	 * @param arg1
	 * @param projectIndexes
	 * 						define indexes which the projection operation
	 * 						will be applied on.  If not specified join 
	 * 						tuples will be simple merged.
	 * @return
	 */
	public ITuple concatenate(final ITuple arg0, final ITuple arg1, 
			final int[] projectIndexes);
}
