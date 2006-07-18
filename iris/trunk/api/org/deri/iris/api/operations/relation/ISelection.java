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
package org.deri.iris.api.operations.relation;

import org.deri.iris.api.storage.IRelation;

/**
 * Interface or class description
 *
 * The Selection operation is meant to be used for selecting a portion of
 * a relation (tree). Basically the functionality of this operation is to
 * select all tuples, from a relation, that are equal regarding the 
 * condition defined by a certain pattern (tuple).
 * 
 * Interface of a selection relation used to promote modularity of inference
 * engines.
 *
 * Note: Implementation of the selection operation assumes an implementation
 * of this interface and an implementation of the IRelationFactory interface
 * (createSelectionOperator method/s).
 * 
 * @author Darko Anicic
 * @date  11.04.2006 @time  15:18:20
 *
 * @version $Revision: 1.5 $ $Date: 2006-07-18 09:30:23 $
 */
public interface ISelection {
	
	public IRelation select();
}
