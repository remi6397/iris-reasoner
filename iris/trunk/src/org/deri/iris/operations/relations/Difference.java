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
package org.deri.iris.operations.relations;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.operations.relation.IDifference;
import org.deri.iris.api.storage.IRelation;

/**
 * @author Joachim Adi Schuetz, DERI Innsbruck
 * @author Darko Anicic
 * @date 06.10.2006
 * 
 */
public class Difference implements IDifference{

	private IRelation<ITuple> rel0 = null;
	private IRelation<ITuple> rel1 = null;	
	
	Difference(IRelation arg0, IRelation arg1) {

		if ((arg0 == null) || (arg1 == null)) {
			throw new IllegalArgumentException(
					"Input parameter must not be null");
		}

		if (((ITuple)arg0.first()).getArity() != ((ITuple)arg1.first()).getArity()) {
			throw new IllegalArgumentException(
					"Arity of input parameter do not match: " + ((ITuple)arg0.first()).getArity() + " != " + ((ITuple)arg1.first()).getArity());
		}
		
		this.rel0 = arg0;
		this.rel1 = arg1;
	}
	
	public IRelation difference() {
	
		IRelation<ITuple> differenceRel = this.rel0;
		differenceRel.removeAll(this.rel1);

 		return differenceRel;
	}

}
