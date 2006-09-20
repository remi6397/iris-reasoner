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
package org.deri.iris.api.builtins;

import org.deri.iris.api.basics.IAtom;

/**
 * <p>
 * Defines a Builtin.
 * </p>
 * <p>
 * $Id: IBuiltInAtom.java,v 1.2 2006-09-20 08:10:11 richardpoettler Exp $
 * </p>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @author richi
 * @date 17.03.2006 14:18:27
 * @version $Revision: 1.2 $
 */
public abstract interface IBuiltInAtom extends IAtom {

	/**
	 * <p>
	 * Runns the evaluation.
	 * </p>
	 * <p>
	 * If the builting isn't evaluable, <code>false</code> will be returned.
	 * </p>
	 * <p>
	 * If the result of the builtin/evaluation is a boolean (e.g. a equals, or
	 * less-than builtin) the corresponding value should be returned. If the
	 * result is more complex, it shoud be stored in an additional term in the
	 * builtin (e.g. add(x,y,z) should store the sum of x and y in z).
	 * </p>
	 * 
	 * @return <code>true</code> if the evaluation completed successfully and
	 *         the result (for e.g. a builtin which result is a boolean (e.g.
	 *         equals)) is <code>true</code>, otherwise <code>false</code>
	 */
	public boolean evaluate();

	/**
	 * Checks whether the builtin is evaluable.
	 * 
	 * @return <code>true</code> if the evaluation could be run correctly,
	 *         otherwise <code>false</code>
	 */
	public boolean isEvaluable();
}
