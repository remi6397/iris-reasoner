/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions by 
 * built-in predicates, default negation (under well-founded semantics), 
 * function symbols and contexts. 
 * 
 * Copyright (C) 2006  Digital Enterprise Research Institute (DERI), 
 * Leopold-Franzens-Universitaet Innsbruck, Technikerstrasse 21a, 
 * A-6020 Innsbruck. Austria.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 */
package org.deri.iris.builtins;

import org.deri.iris.api.builtins.IBuiltInAtom;
import org.deri.iris.api.factory.IBuiltInsFactory;
import org.deri.iris.api.terms.ITerm;

/**
 * <p>
 * Factory to create all sorts of builtins.
 * </p>
 * <p>
 * $Id: BuiltinsFactory.java,v 1.1 2006-09-21 09:01:19 richardpoettler Exp $
 * </p>
 * 
 * @author richi
 * @version $Revision: 1.1 $
 * @date $Date: 2006-09-21 09:01:19 $
 */
public class BuiltinsFactory implements IBuiltInsFactory {

	private static final IBuiltInsFactory INSTANCE = new BuiltinsFactory();

	private BuiltinsFactory() {
		// this is a singelton
	}

	/**
	 * Returns the singelton instance of this factory.
	 * 
	 * @return a instane of this factory
	 */
	public static IBuiltInsFactory getInstance() {
		return INSTANCE;
	}

	public IBuiltInAtom createEqual(ITerm t0, ITerm t1) {
		return new EqualBuiltin(t0, t1);
	}

	public IBuiltInAtom createGreater(ITerm t0, ITerm t1) {
		return new GreaterBuiltin(t0, t1);
	}

	public IBuiltInAtom createGreaterEqual(ITerm t0, ITerm t1) {
		return new GreaterEqualBuiltin(t0, t1);
	}

	public IBuiltInAtom createLess(ITerm t0, ITerm t1) {
		return new LessBuiltin(t0, t1);
	}

	public IBuiltInAtom createLessEqual(ITerm t0, ITerm t1) {
		return new LessEqualBuiltin(t0, t1);
	}

	public IBuiltInAtom createUnequal(ITerm t0, ITerm t1) {
		return new UnEqualBuiltin(t0, t1);
	}
}
