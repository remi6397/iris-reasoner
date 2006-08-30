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
package org.deri.iris.evaluation.magic;

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.TERM;

import java.util.ArrayList;
import java.util.List;

import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.evaluation.common.Adornment;
import org.deri.iris.evaluation.common.AdornedProgram.AdornedPredicate;
import org.deri.iris.evaluation.common.AdornedProgram.AdornedRule;

/**
 * <p>
 * Helpermethods to interact with sips.
 * </p>
 * <p>
 * $Id: SipHelper.java,v 1.1 2006-08-30 12:51:26 richardpoettler Exp $
 * </p>
 * 
 * @author richi
 * @version $Revision: 1.1 $
 * @date $Date: 2006-08-30 12:51:26 $
 */
public class SipHelper {

	/** A small dummy constant */
	private static final ITerm MINIMAL_CONST_TERM = TERM.createConstant(TERM
			.createString(""));

	private SipHelper() {
		// prevent subclassing
	}

	/**
	 * Retrieces the adorned version of a sip of a rule
	 * 
	 * @param r
	 *            the rule for which to retrieve the adorned sip
	 * @return the adorned sip
	 */
	public static SIPImpl getAdornedSip(final AdornedRule r) {
		if (r == null) {
			throw new NullPointerException("The rule must not be null");
		}
		if (r.getHeadLenght() != 1) {
			throw new IllegalArgumentException(
					"At the moment only heads with length 1 are allowed");
		}

		final ILiteral headLiteral = r.getHeadLiteral(0);

		if (!(headLiteral.getPredicate() instanceof AdornedPredicate)) {
			throw new IllegalArgumentException(
					"The predicate of the literal must be adorned");
		}

		final AdornedPredicate p = (AdornedPredicate) headLiteral
				.getPredicate();

		// create query
		List<ITerm> queryTerms = new ArrayList<ITerm>(headLiteral.getTuple()
				.getArity());
		int counter = 0;
		for (final Adornment a : p.getAdornment()) {
			switch (a) {
			case BOUND:
				queryTerms.add(MINIMAL_CONST_TERM);
				counter++;
				break;
			case FREE:
				queryTerms.add(headLiteral.getTuple().getTerm(counter));
				counter++;
				break;
			default:
				throw new IllegalArgumentException(
						"Can only handle BOUND and FREE as adornments");
			}
		}

		// recreating the sip -> maybe should be done in a pretier way
		// TODO: create the sip directly, not through the computation of the
		// constructor
		return new SIPImpl(r, BASIC.createQuery(BASIC.createLiteral(headLiteral
				.isPositive(), p, BASIC.createTuple(queryTerms))));
	}
}
