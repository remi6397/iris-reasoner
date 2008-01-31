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

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.evaluation_old.algebra.ISelectionDescriptor;
import org.deri.iris.basics.seminaive.NonEqualityTerm;

/**
* @author Darko Anicic, DERI Innsbruck
* @date 13.12.2006 18:17:34
*/
public class SelectionDescriptor extends Component implements ISelectionDescriptor{

	private ITuple pattern = null;

	private int[] indexes = null;
	
	SelectionDescriptor(final int[] i) {
		super(ComponentType.SELECTION);
		if (i == null) {
			throw new IllegalArgumentException("Constructor " +
				"parameter must be specified (not null values");
		}
		this.indexes = i;
	}

	SelectionDescriptor(final ITuple p) {
		super(ComponentType.SELECTION);
		if (p == null) {
			throw new IllegalArgumentException("Constructor " +
				"parameter must not be specified (not null values");
		}
		this.pattern = p;
	}
	
	SelectionDescriptor(final ITuple p, final int[] i) {
		super(ComponentType.SELECTION);
		if (i == null || p == null) {
			throw new IllegalArgumentException("All constructor " +
				"parameters must be specified (not null values");
		}
		this.pattern = p;
		this.indexes = i;
	}

	public int[] getIndexes() {
		return this.indexes;
	}

	public ITuple getPattern() {
		return this.pattern;
	}
	
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append(ComponentType.SELECTION);
		
		buffer.append(" ((");
		for (int i = 0; i < this.pattern.size(); i++) {
			if(this.pattern.get(i) != null)
				buffer.append(this.pattern.get(i).toString());
			else
				buffer.append("null");
			buffer.append(", ");
		}
		buffer.delete(buffer.length() - 2, buffer.length());
		buffer.append(")<");
		if(this.pattern.get(0) instanceof NonEqualityTerm &&
				this.pattern.get(1) instanceof NonEqualityTerm){
			buffer.append("NON-EQUALS");	
		}else{
			buffer.append("EQUALS");
		}
		buffer.append(">) [");
		if(indexes != null){
			for(int i = 0; i < indexes.length; i++){
				buffer.append(indexes[i]);
				buffer.append(", ");
			}
			buffer.delete(buffer.length() - 2, buffer.length());
		}else{
			buffer.append("no indexes");
		}
		buffer.append("] all vars:(");
		for (int i = 0; i < this.getVariables().size(); i++) {
			if(this.getVariables().get(i) != null){
				buffer.append(this.getVariables().get(i).toString());
			}
			buffer.append(", ");
		}
		buffer.delete(buffer.length() - 2, buffer.length());
		buffer.append(")\n(");
		if(this.getChildren().size() > 0)
			buffer.append(this.getChildren().get(0).toString());
		else
			buffer.append("NO CHILD FOUND!");
		buffer.append(")");
		return buffer.toString();
	}
}
