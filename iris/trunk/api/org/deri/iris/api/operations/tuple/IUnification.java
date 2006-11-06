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
package org.deri.iris.api.operations.tuple;

import org.deri.iris.operations.tuple.Unification.UnificationResult;

/**
 *
 * Interface of a unification operation used to promote modularity 
 * of the inference engine.
 * 
 * Given two terms containing some variables, find, if it exists, 
 * the simplest substitution (i.e., an assignment of some term to every 
 * variable) which makes the two terms equal. The resulting substitution 
 * is called the most general unifier and is unique up to variable renaming.
 * 
 * An implementation of this interface is supposed to find the most general
 * unifier for two atoms or two tupples.
 * 
 * @author Darko Anicic
 * @date  11.04.2006 @time  15:54:09
 *
 * @version $Revision: 1.9 $ $Date: 2006-11-06 10:49:27 $
 */
public interface IUnification {

	public UnificationResult unify();
}
