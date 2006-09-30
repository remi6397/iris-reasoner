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
package org.deri.iris.api.factory;

import org.deri.iris.api.basics.IAtom;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.operations.tuple.IConcatenation;
import org.deri.iris.api.operations.tuple.IMatching;
import org.deri.iris.api.operations.tuple.IUnification;


/**
 * @author Darko Anicic, DERI Innsbruck
 * @date   30.09.2006 17:20:39
 */
public interface ITupleOperationsFactory {

	/**
	 * @param arg0 tuple to be Concatenated
	 * @param arg1 tuple to be Concatenated
	 * @return     concatenated tuple
	 */
	public IConcatenation createConcatenationOperator(ITuple arg0, ITuple arg1);
	
	/**
	 * @param arg0 tuple to be Concatenated
	 * @param arg1 tuple to be Concatenated
	 * @param pi   projection indexes
	 * @return     concatenated tuple
	 */
	public IConcatenation createConcatenationOperator(
			ITuple arg0, ITuple arg1, int[] pi);

	public IMatching createMatchingOperator(ITuple arg0, ITuple arg1);
	
	public IUnification createUnificationOperator(final ITuple arg0, final ITuple arg1);
	
	public IUnification createUnificationOperator(final IAtom arg0, final IAtom arg1);
	
}
