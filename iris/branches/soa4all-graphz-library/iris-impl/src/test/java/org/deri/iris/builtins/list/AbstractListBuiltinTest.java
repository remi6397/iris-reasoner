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
package org.deri.iris.builtins.list;

import junit.framework.TestCase;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.IConcreteTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.concrete.IList;
import org.deri.iris.factory.Factory;
import org.deri.iris.terms.concrete.IntTerm;

public abstract class AbstractListBuiltinTest extends TestCase {

	protected static final ITerm X = Factory.TERM.createVariable("X");

	protected static final ITerm Y = Factory.TERM.createVariable("Y");

	protected static final ITerm Z = Factory.TERM.createVariable("Z");

	protected static final IConcreteTerm ZERO = new IntTerm(0);
	
	protected static final IConcreteTerm ONE = new IntTerm(1);

	protected static final IConcreteTerm TWO = new IntTerm(2);

	protected static final IConcreteTerm THREE = new IntTerm(3);

	protected static final IConcreteTerm FOUR = new IntTerm(4);

	protected static final ITuple EMPTY_TUPLE = Factory.BASIC.createTuple();

	protected static final IList EMPTY_LIST = new org.deri.iris.terms.concrete.List();
	
	protected ITuple args = null;
	
	protected ITuple actual = null;

}
