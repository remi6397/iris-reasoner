/*
 * Copyright 2011, Semantic Technology Institute (STI) Innsbruck, 
 * University of Innsbruck, Technikerstrasse 21a, 6020 Innsbruck, Austria.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.deri.iris.api.basics;

import java.util.List;

/**
 * <p>
 * Represents a rule in the program. 
 * A rule has a form:
 * </p>
 * <p>
 * q :- p1, p2,...,pn 
 * </p>
 * <p>
 * where q is a positive literal (the head), 
 * and p1, p2,...,pn is a conjunction of several literals (the body). 
 * Only safe rules are supported. A rule is safe if its every variable
 * occurs in one of its positive, non built-in, atoms of the body.</p>
 * <p>
 * $Id: IRule.java,v 1.9 2007-10-30 08:28:27 poettler_ric Exp $
 * </p>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @version $Revision: 1.9 $
 */
public interface IRule
{
	/**
	 * Get the rule head-
	 * @return The rule head.
	 */
	public List<ILiteral> getHead();
	
	/**
	 * Get the rule body.
	 * @return The rule body.
	 */
	public List<ILiteral> getBody();

	/**
	 * <p>
	 * A rule is rectified if its head has the same form as 
	 * heads of the other rules from the program, 
	 * e.g. p(X1,...,Xk) for variables X1,...,Xk.
	 * </p>
	 * <p> 
	 * For a given pair of rules:
	 * </p>
	 * <ul>
	 * <li> p(a, X, Y) :- r(X, Y)</li>
	 * <li> p(X, Y, X) :- r(Y, X)</li>
	 * </ul>
	 * <p>
	 * after the rectification we get the following rules:
	 * </p>
	 * <ul>
	 * <li> p(U, V, W) :- r(V, W), U='a'.</li>
	 * <li> p(U, V, W) :- r(V, U), W=U.</li>
	 * </ul>
	 * <p>
	 * where both rules have heads of the same form.
	 * </p>
	 * 
	 * @return	True if the rule is rectified; otherwise false.
	 */
	public boolean isRectified();
}
