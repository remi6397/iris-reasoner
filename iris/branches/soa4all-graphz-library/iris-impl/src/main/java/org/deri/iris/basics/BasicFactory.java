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
package org.deri.iris.basics;

import java.util.Arrays;
import java.util.List;

import org.deri.iris.api.basics.IAtom;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.factory.IBasicFactory;
import org.deri.iris.api.terms.ITerm;

/**
 * <p>
 * Simple implementatiion of the basic factory.
 * </p>
 * <p>
 * $Id$
 * </p>
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @author Darko Anicic, DERI Innsbruck
 * @version $Revision$
 */
public class BasicFactory implements IBasicFactory {

	private static final IBasicFactory FACTORY = new BasicFactory();

	private BasicFactory() {
		// this is a singelton
	}

	public IAtom createAtom(IPredicate p, ITuple tuple) {
		return new Atom(p, tuple);
	}

	public ILiteral createLiteral(boolean isPositive, IAtom atom) {
		return new Literal(isPositive, atom);
	}

	public ILiteral createLiteral(boolean positive, IPredicate p,
			ITuple tuple) {
		return createLiteral(positive, createAtom(p, tuple));
	}

	public IPredicate createPredicate(String symbol, int arity) {
		return new Predicate(symbol, arity);
	}

	public IQuery createQuery(ILiteral... literals) {
		return createQuery(Arrays.asList(literals));
	}

	public IQuery createQuery(List<ILiteral> literals) {
		return new Query(literals);
	}

	public IRule createRule(List<ILiteral> head, List<ILiteral> body) {
		return new Rule(head, body); 
	}

	public ITuple createTuple(ITerm... terms) {
		return createTuple(Arrays.asList(terms));
	}
	
	public ITuple createTuple(List<ITerm> terms) {
		return new Tuple(terms);
	}

	public IAtom createAtom(final IAtom a) {
		if (a == null) {
			throw new NullPointerException("The atom must not be null");
		}
		if (a.isBuiltin()) {
			throw new IllegalArgumentException("The atom must not be a builtin atom");
		}
		return createAtom(a.getPredicate(), createTuple(a.getTuple()));
	}

	public ILiteral createLiteral(final ILiteral l) {
		if (l == null) {
			throw new NullPointerException("The literal must not be null");
		}
		return createLiteral(l.isPositive(), createAtom(l.getAtom()));
	}

	public static IBasicFactory getInstance() {
		return FACTORY;
	}
}
