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
package org.deri.iris.rules.compiler;

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.CONCRETE;
import static org.deri.iris.factory.Factory.TERM;

import java.util.ArrayList;
import java.util.List;

import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.ITerm;

/**
 * Helper methods for the tests in this package.
 */
public class Helper {
	public static ITerm createTerm(Object t) {
		if (t instanceof Integer)
			return CONCRETE.createInteger((Integer) t);
		else if (t instanceof String)
			return TERM.createVariable((String) t);
		else if (t instanceof ITerm)
			return (ITerm) t;

		throw new RuntimeException(
				"Unsupported term type in Helper.createTerm()");
	}

	public static List<ITerm> createTerms(Object... termObjects) {
		List<ITerm> terms = new ArrayList<ITerm>();

		for (Object o : termObjects)
			terms.add(createTerm(o));

		return terms;
	}

	public static ITuple createTuple(Object... termObjects) {
		return BASIC.createTuple(createTerms(termObjects));
	}

	public static ITerm createConstant(Object t) {
		if (t instanceof Integer)
			return CONCRETE.createInteger((Integer) t);
		else if (t instanceof String)
			return TERM.createString((String) t);
		else if (t instanceof ITerm)
			return (ITerm) t;

		throw new RuntimeException(
				"Unsupported term type in Helper.createTerm()");
	}

	public static List<ITerm> createConstants(Object... termObjects) {
		List<ITerm> terms = new ArrayList<ITerm>();

		for (Object o : termObjects)
			terms.add(createConstant(o));

		return terms;
	}

	public static ITuple createConstantTuple(Object... termObjects) {
		return BASIC.createTuple(createConstants(termObjects));
	}

	public static ITerm createConstructedTerm(String symbol,
			Object... termObjects) {
		return TERM.createConstruct(symbol, createTerms(termObjects));
	}

	public static ILiteral createLiteral(boolean positive, String predicate,
			Object... termObjects) {
		ITuple tuple = createTuple(termObjects);
		return BASIC.createLiteral(positive, BASIC.createPredicate(predicate,
				tuple.size()), tuple);
	}
}
