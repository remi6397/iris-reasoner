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
package org.deri.iris;

import junit.framework.Assert;

import org.deri.iris.api.terms.ITerm;

/**
 * <p>
 * Static test collection for terms.
 * </p>
 * <p>
 * $Id: TermTests.java,v 1.1 2007-07-17 10:12:55 poettler_ric Exp $
 * </p>
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision: 1.1 $
 */
public final class TermTests {

	private TermTests() {
		// prevent subclassing
	}

	public static void runTestGetMinValue(ITerm small) {
		Assert.assertTrue("The smallest value of "
				+ small.getClass().getSimpleName() + " mmst be smaller than "
				+ small, small.compareTo(null) > 0);
	}

}
