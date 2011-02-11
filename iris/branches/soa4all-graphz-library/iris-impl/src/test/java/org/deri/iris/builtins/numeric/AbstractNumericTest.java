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
package org.deri.iris.builtins.numeric;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.factory.Factory;

import junit.framework.TestCase;

/**
 */
public abstract class AbstractNumericTest extends TestCase {

	protected static final ITerm X = Factory.TERM.createVariable("X");

	protected static final ITerm Y = Factory.TERM.createVariable("Y");

	protected static final ITerm Z = Factory.TERM.createVariable("Z");

	protected static final ITuple EMPTY_TUPLE = Factory.BASIC.createTuple();

	protected ITuple args = null;

	protected ITuple expected = null;

	protected ITuple actual = null;

	public AbstractNumericTest(String name) {
		super(name);
	}

}
