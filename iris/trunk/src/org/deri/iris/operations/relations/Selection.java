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

import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.operations.relation.ISelection;
import org.deri.iris.api.storage.IRelation;
import org.deri.iris.api.terms.IStringTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.operations.tuple.SelectionComparator;
import org.deri.iris.storage.Relation;

/**
 * @author Darko Anicic, DERI Innsbruck
 * @date   31.05.2006 10:38:40
 */
public class Selection implements ISelection{

	private IRelation relation = null;
	private ITuple pattern = null;
	private SelectionComparator comparator = null;
	
	public IRelation select(IRelation relation, ITuple pattern) {
		//this.relation = relation;
		this.pattern = pattern;
		this.comparator = new SelectionComparator(createIndexes(this.pattern));
		this.relation = new Relation(this.comparator);
		SortedSet set = relation.tailSet(pattern);
		this.relation.addAll(relation.tailSet(pattern));
		
		return null;
	}

	private int[]createIndexes(ITuple pattern){
		int[] indexes = new int[pattern.getArity()];
		List<ITerm> terms = pattern.getTerms();
		Iterator i = terms.iterator();
		int j=0;
		while(i.hasNext()){
			Object o = ((IStringTerm)i.next()).getValue();
			if(o != null)
				indexes[j] = j;
			else
				indexes[j] = -1;
			j++;
		}
		return indexes;
	}
}
