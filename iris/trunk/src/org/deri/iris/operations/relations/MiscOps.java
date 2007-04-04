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

import java.util.ArrayList;
import java.util.List;

import org.deri.iris.api.terms.IVariable;

/**
 * <p>
 * This class offers some miscellaneous operations related to operation
 * on relations.
 * </p>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date   24.05.2006 09:37:43
 */
public class MiscOps {

	private MiscOps() {
		// prevent subclassing
	}
	
	public static int[] getJoinIndexes(List<IVariable> l0, List<IVariable> l1){
		int[] indexes = JoinSimple.getInitIndexes(Math.max(l0.size(), l1.size()));
		List<Integer> vi = new ArrayList<Integer>();
		for (int j=0; j<l0.size(); j++) {
			for (int k=0; k<l1.size(); k++) {
				if (l0.get(j).equals(l1.get(k))) {
					if(! vi.contains(k)){
						indexes[j] = k;
						vi.add(k);
						break;
					}
				} 
			}
		}
		return indexes;
	}
	
	public static int[] getProjectionIndexes(List<IVariable> fromVars, List<IVariable> toVars) {
		int[] i = getInitProjectionIndexes(fromVars.size());
		IVariable v = null;
		for (int j = 0; j < toVars.size(); j++){
			v = toVars.get(j);
			i[fromVars.indexOf(v)] = j;
		}
		return i;
	}
	
	/**
	 * <p>Creates a default projection indexes.</p>
	 *  
	 * @param arity	Arity of the default projection indexes.
	 * @return		The default projection indexes.
	 */
	public static int[] getInitProjectionIndexes(int arity) {
		int[] i = new int[arity];
		for (int j = 0; j < arity; j++){
			i[j] = -1;
		}
		return i;
	}
}
