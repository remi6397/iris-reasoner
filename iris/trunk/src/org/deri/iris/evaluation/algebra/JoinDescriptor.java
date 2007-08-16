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
package org.deri.iris.evaluation.algebra;

import org.deri.iris.api.evaluation.algebra.IJoinDescriptor;
import org.deri.iris.operations.relations.JoinCondition;

/**
* @author Darko Anicic, DERI Innsbruck
* @date 13.12.2006 17:47:34
*/
public class JoinDescriptor extends Component implements IJoinDescriptor{

	private JoinCondition condition = null;
	
	JoinDescriptor(JoinCondition c) {
		super(ComponentType.JOIN);
		if (c == null) {
			throw new IllegalArgumentException("Constructor " +
				"parameter must not be specified (not null values");
		}
		this.condition = c;
	}
	
	public JoinCondition getCondition() {
		return this.condition;
	}
	
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append(ComponentType.JOIN);
		buffer.append("<" + this.condition + ">");
		
		boolean first = true;
		buffer.append('[');
		for (int i = 0; i < this.getVariables().size(); i++) {
			if ( first )
				first = false;
			else
				buffer.append(", ");
			buffer.append(this.getVariables().get(i).toString());
		}
		buffer.append(']');
		
		buffer.append('{');
		//buffer.delete(buffer.length() - 1, buffer.length());
		first = true;
		for(int i = 0; i < this.getChildren().size(); i++){
			if ( first )
				first = false;
			else
				buffer.append(", ");
			buffer.append('\n');
			buffer.append(this.getChildren().get(i));
		}
		buffer.append('}');

		return buffer.toString();
	}
}
