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
package org.deri.iris.api.evaluation_old.algebra;

import java.util.Collection;
import java.util.List;

import org.deri.iris.api.terms.IVariable;

/**
 * <p>
 * Represents a general component for describing a relational
 * algebra operation (e.g. join, selection, projection, union etc.). 
 * An interface of a component (descriptor) for a particular 
 * relational algebra operation extends this interface.
 * </p>
 * <p>
 * A relational algebra expression is represented as a tree where each node 
 * represents a particular relational algebra operation. The tree node is 
 * represented as a component. The evaluation of a relational algebra 
 * expression is done by the evaluation of the set of  relational algebra 
 * operations in an order defined by the tree.
 * </p>
 * 
* @author Darko Anicic, DERI Innsbruck
* @date 13.12.2006 17:29:34
*/
public interface IComponent {

	/**
	 * Defines a type of the component.
	 */
	public static enum ComponentType {
		BUILTIN,
		CONSTANT,
		DIFFERENCE,
		JOIN,
		PROJECTION,
		RELATION,
		SELECTION,
		UNION
	};
	
	/**
	 * Returns a type of the component.
	 * 
	 * @return	The component type.
	 */
	public ComponentType getType();
	
	/**
	 * Adds a collection of variables to the component which are needed for 
	 * a particular relational algebra operation.
	 * 
	 * @param vars	Variables to add.
	 */
	public void addVariables(final Collection<IVariable> vars);
	
	/**
	 * Add the child represented as a component to the parent.
	 * 
	 * @param c	The component to be added to the parent.
	 * @return	True if the component is added to the tree, otherwise false.
	 */
	public boolean addChild(final IComponent c);
	
	/**
	 * Add children represented as a list of components to the parent.
	 * 
	 * @param children	The list of components to be added to the parent.
	 * @return	True if the component is added to the tree, otherwise false.
	 */
	public boolean addChildren(final List<IComponent> children);
	
	/**
	 * Returns all children.
	 * 
	 * @return	List of all children.
	 */
	public List<IComponent> getChildren();
	
	/**
	 * Removes a particular component from the tree.
	 * 
	 * @param c	The component to be removed.
	 * @return	True if the component is removed from the tree, otherwise false.
	 */
	public boolean removeChild(final IComponent c);
	
	/**
	 * Returns all variables from the component.
	 * 
	 * @return	List of all variables from the component.
	 */
	public List<IVariable> getVariables();
	
	/**
	 * <p>
	 * Checks whether the component (branch) which represents corresponding literal is positive.
	 * </p>
	 * 
	 * @return True if the component (branch) is a positive; false
	 *         otherwise.
	 */
	public boolean isPositive();
	
	/**
	 * <p>
	 * Set the component (branch), which represents corresponding 
	 * literal, to be positive or negative.
	 * </p>
	 * 
	 * @param positive	If true, the component is a positive; false 
	 *            otherwise.
	 */
	public void setPositive(boolean positive);
}
