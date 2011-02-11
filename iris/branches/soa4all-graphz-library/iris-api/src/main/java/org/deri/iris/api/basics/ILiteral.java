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

/**
 * <p>
 * A literal (a subgoal) is either an atomic formula or a negated atomic
 * formula:
 * </p>
 * <p>
 * p(A1,...An) or not p(A1,...An)
 * </p>
 * <p>
 * This interface is used to promote modularity of the inference engine.
 * </p>
 * <p>
 * $Id: ILiteral.java,v 1.6 2007-10-30 10:35:40 poettler_ric Exp $
 * </p>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date 14.11.2005 17:20:27
 */
public interface ILiteral extends Comparable<ILiteral> {
	/**
	 * <p>
	 * Checks whether the literal is a positive atomic formula or a negated
	 * atomic formula.
	 * </p>
	 * 
	 * @return True if the literal is a positive atomic formula; false
	 *         otherwise.
	 */
	public boolean isPositive();

	/**
	 * <p>
	 * Returns the atom of this literal.
	 * <p/>
	 * 
	 * @return The atom.
	 */
	public IAtom getAtom();
}
