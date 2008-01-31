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
package org.deri.iris.evaluation_old.algebra;

import org.deri.iris.api.evaluation_old.algebra.IUnionDescriptor;

/**
* @author Darko Anicic, DERI Innsbruck
* @date 13.12.2006 17:47:34
*/
public class UnionDescriptor extends Component implements IUnionDescriptor{

	public UnionDescriptor() {
		super(ComponentType.UNION);
	}
	
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append(ComponentType.UNION);
		buffer.append(" (");
		for (int i = 0; i < this.getVariables().size(); i++) {
			if(this.getVariables().get(i) != null){
				buffer.append(this.getVariables().get(i).toString());
			}
			buffer.append(", ");
		}
		buffer.delete(buffer.length() - 2, buffer.length());
		buffer.append("}\n(");
		if(this.getChildren().size() > 0){
			for(int i=0; i<this.getChildren().size(); i++){
				buffer.append(this.getChildren().get(i).toString());
				buffer.append(",\n");
			}
			buffer.delete(buffer.length() - 2, buffer.length());
		}else
			buffer.append("NO CHILD FOUND!");
		buffer.append(")");
		return buffer.toString();
	}
}
