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
package org.deri.iris.terms.concrete;

/**
 * <p>
 * Some helper methods for some operations on terms.
 * </p>
 * <p>
 * $Id: TermHelper.java,v 1.3 2007-10-09 20:29:38 bazbishop237 Exp $
 * </p>
 * 
 * @author richi
 * @version $Revision: 1.3 $
 * @date $Date: 2007-10-09 20:29:38 $
 */
class TermHelper {
	private TermHelper() {
		// prevent subclassing
	}

	/**
	 * Returns the double value from a <code>INumericTerm</code> <b>This
	 * method assumes that only numbers are stored in <code>INumericTerm</code>.</b>
	 * 
	 * @param n
	 *            the term
	 * @return the double value
	 * @throws NullPointerException
	 *             if the term is null
	 * @see Number
	 */
	/*static double getDouble(final INumericTerm n) {
		if (n == null) {
			throw new NullPointerException("The term must not be null");
		}
		// TODO: maybe instance check for Number
		return ((Number) n.getValue()).doubleValue();
	}*/

	/**
	 * Returns the float value from a <code>INumericTerm</code> <b>This method
	 * assumes that only numbers are stored in <code>INumericTerm</code>.</b>
	 * 
	 * @param n
	 *            the term
	 * @return the float value
	 * @throws NullPointerException
	 *             if the term is null
	 * @see Number
	 */
	/*static float getFloat(final INumericTerm n) {
		if (n == null) {
			throw new NullPointerException("The term must not be null");
		}
		// TODO: maybe instance check for Number
		return ((Number) n.getValue()).floatValue();
	}*/

	/**
	 * Returns the int value from a <code>INumericTerm</code> <b>This method
	 * assumes that only numbers are stored in <code>INumericTerm</code>.</b>
	 * 
	 * @param n
	 *            the term
	 * @return the int value
	 * @throws NullPointerException
	 *             if the term is null
	 * @see Number
	 */
	/*static int getInt(final INumericTerm n) {
		if (n == null) {
			throw new NullPointerException("The term must not be null");
		}
		return (int) getDouble(n);
	}*/
}
