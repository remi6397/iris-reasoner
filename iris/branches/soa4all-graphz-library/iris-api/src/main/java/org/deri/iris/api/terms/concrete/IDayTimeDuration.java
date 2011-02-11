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
package org.deri.iris.api.terms.concrete;

/*
 * W3C specification: http://www.w3.org/TR/xpath-functions/#dt-dayTimeDuration
 */

/**
 * <p>
 * An interface for representing the xs:dayTimeDuration data-type.
 * xs:dayTimeDuration is derived from xs:duration by restricting its lexical
 * representation to contain only the days, hours, minutes and seconds
 * components.
 * </p>
 * <p>
 * Remark: IRIS supports data types according to the standard specification for
 * primitive XML Schema data types.
 * </p>
 */
public interface IDayTimeDuration extends IDuration {

	/**
	 * Returns a canonical representation of dayTimeDuration as defined in
	 * http://www.w3.org/TR/xpath-functions/#canonical-dayTimeDuration.
	 * 
	 * @return A canonical representation of this dayTimeDuration instance.
	 */
	public IDayTimeDuration toCanonical();

}
