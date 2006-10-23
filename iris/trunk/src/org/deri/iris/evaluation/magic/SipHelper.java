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
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.evaluation.common.IAdornedPredicate;
import org.deri.iris.api.evaluation.common.IAdornedRule;
import org.deri.iris.api.evaluation.magic.ISip;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.evaluation.common.Adornment;
import org.deri.iris.evaluation.common.AdornedProgram.AdornedPredicate;
import org.deri.iris.evaluation.common.AdornedProgram.AdornedRule;

/**
 * <p>
 * Helpermethods to do various tasks related to sips and adornments.
 * </p>
 * <p>
 * $Id: SipHelper.java,v 1.6 2006-10-23 06:54:49 richardpoettler Exp $
 * </p>
 * 
 * @author richi
 * @version $Revision: 1.6 $
 * @date $Date: 2006-10-23 06:54:49 $
 */
public class SipHelper {

	/** A small dummy constant */
	private static final ITerm MINIMAL_CONST_TERM = TERM.createString("");

	private SipHelper() {
		// prevent subclassing
	}

	/**
	 * Retrieces the adorned version of a sip of a rule.
	 * 
	 * @param r
	 *            the rule for which to retrieve the adorned sip
	 * @return the adorned sip
	 */
	public static ISip getAdornedSip(final IAdornedRule r) {
		if (r == null) {
			throw new NullPointerException("The rule must not be null");
		}
		if (r.getHeadLenght() != 1) {
			throw new IllegalArgumentException(
					"At the moment only heads with length 1 are allowed");
		}

		final ISip copy = r.getSIP().defensifeCopy(r, r.getSIP().getQuery());
		// exchanging all occurences of unadorned literals in the sip through
		// the adorned ones in the head
		for (final ILiteral l : r.getHeadLiterals()) {
			if (l.getPredicate() instanceof AdornedPredicate) {
				final IAdornedPredicate p = (IAdornedPredicate) l.getPredicate();
				final ILiteral unadornedLiteral = BASIC.createLiteral(l
						.isPositive(), p.getUnadornedPredicate(), l.getTuple());
				while (copy.containsVertex(unadornedLiteral)) {
					copy.exchangeLiteral(unadornedLiteral, l);
				}
			}
		}
		// exchanging all occurences of unadorned literals in the sip through
		// the adorned ones in the body
		for (final ILiteral l : r.getBodyLiterals()) {
			if (l.getPredicate() instanceof AdornedPredicate) {
				final IAdornedPredicate p = (IAdornedPredicate) l.getPredicate();
				final ILiteral unadornedLiteral = BASIC.createLiteral(l
						.isPositive(), p.getUnadornedPredicate(), l.getTuple());
				while (copy.containsVertex(unadornedLiteral)) {
					copy.exchangeLiteral(unadornedLiteral, l);
				}
			}
		}
		return copy;
	}

	/**
	 * Creates a query for an adorned literal.
	 * 
	 * @param l
	 *            for which to create the query
	 * @return the created query
	 * @throws NullPointerException
	 *             if the literal is null
	 * @throws IllegalArgumentException
	 *             if the predicate of the literal isn't adorned
	 * @throws IllegalArgumentException
	 *             if the adornment contains something else than BOUND's and
	 *             FREE's
	 */
	public static IQuery getQueryForLiteral(final ILiteral l) {
		if (l == null) {
			throw new NullPointerException("The literal must not be null");
		}
		if (!(l.getPredicate() instanceof AdornedPredicate)) {
			throw new IllegalArgumentException(
					"The predicate of the literal must be adorned");
		}
		final AdornedPredicate p = (AdornedPredicate) l.getPredicate();
		final int realLength = p.getAdornment().length;
		final ITuple t = l.getTuple();
		final List<ITerm> queryTerms = new ArrayList<ITerm>(realLength);

		if (t.getArity() == realLength) { // if the arity matches the lenght
			// of the adornemt
			int counter = 0;
			for (final Adornment a : p.getAdornment()) {
				final ITerm actualTerm = t.getTerm(counter);
				switch (a) {
				case BOUND:
					if (t.isGround()) {
						queryTerms.add(actualTerm);
					} else {
						queryTerms.add(MINIMAL_CONST_TERM);
					}
					break;
				case FREE:
					queryTerms.add(t.getTerm(counter));
					break;
				default:
					throw new IllegalArgumentException(
							"Can only handle BOUND and FREE as adornments");
				}
				counter++;
			}
		} else { // if the arities doesn't match -> might be an adorned
			// predicate
			int counter = 0;
			for (final Adornment a : p.getAdornment()) {
				switch (a) {
				case BOUND:
					queryTerms.add(MINIMAL_CONST_TERM);
					break;
				case FREE:
					queryTerms.add(t.getTerm(counter));
					break;
				default:
					throw new IllegalArgumentException(
							"Can only handle BOUND and FREE as adornments");
				}
				counter++;
			}
		}
		return BASIC.createQuery(BASIC.createLiteral(l.isPositive(), p, BASIC
				.createTuple(queryTerms)));
	}

	/**
	 * <p>Creates a adorned rule out of an undadorned one.</p>
	 * <p>NOTE: Maybe this got to become a constructor of the adorned rule.</p>
	 * 
	 * @param r
	 *            for which to create the adorned rule
	 * @return the adorned rule
	 * @throws NullPointerException
	 *             if the rule is null
	 * @throws IllegalArgumentException
	 *             if the length of the head is unequal to 1
	 */
	public static IAdornedRule getAdornedRule(final IRule r) {
		if (r instanceof IAdornedRule) {
			return (IAdornedRule) r;
		}
		if (r == null) {
			throw new NullPointerException("The rule must not´ be null");
		}
		if (r.getHeadLenght() != 1) {
			throw new IllegalArgumentException(
					"The head must have a length of 1");
		}

		final ILiteral headLiteral = r.getHeadLiteral(0);

		if (!(headLiteral.getPredicate() instanceof AdornedPredicate)) {
			throw new IllegalArgumentException(
					"The predicate of the head must be adorned");
		}

		return new AdornedRule(r, new SIPImpl(r,
				getQueryForLiteral(headLiteral)));
	}
}
