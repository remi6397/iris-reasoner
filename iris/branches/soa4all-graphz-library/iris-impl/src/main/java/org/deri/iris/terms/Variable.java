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
package org.deri.iris.terms;

import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;

/**
 * <p>
 * Simple implementation of the IVariable.
 * </p>
 * <p>
 * $Id$
 * </p>
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision$
 */
public class Variable implements IVariable {

	private String name = "";

	Variable(final String name) {
		this.name = name;
	}

	public boolean isGround() {
		return false;
	}

	public int compareTo(ITerm o) {
		if (o == null)
			return 1;
			
		Variable v = (Variable) o;
		
		return name.compareTo( v.getValue() );
	}

	public int hashCode() {
		return name.hashCode();
	}

	public boolean equals(final Object o) {
		if (!(o instanceof Variable)) {
			return false;
		}
		Variable v = (Variable) o;
		return name.equals(v.name);
	}

	/**
	 * Returns a String representation of this object. The subject of the string
	 * format is to change. An example return value might be
	 * &quot;?date&quot;
	 * 
	 * @return the String representation
	 */
	public String toString() {
		return "?" + name;
	}

	public String getValue() {
		return name;
	}
}
