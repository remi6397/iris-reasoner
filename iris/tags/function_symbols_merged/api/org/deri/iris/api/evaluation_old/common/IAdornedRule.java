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
package org.deri.iris.api.evaluation_old.common;

import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.evaluation_old.magic.ISip;

/**
 * <p>
 * An adorned rule. The only difference to an odinary rule is, that it has a sip
 * attached, and that you can exchange literals.
 * </p>
 * <p>
 * $Id: IAdornedRule.java,v 1.2 2006-09-18 08:08:50 richardpoettler Exp $
 * </p>
 * 
 * @author richi
 * @version $Revision: 1.2 $
 * @date $Date: 2006-09-18 08:08:50 $
 */
public interface IAdornedRule extends IRule {

	/**
	 * Retruns the sip associated with this rule.
	 * 
	 * @return the sip
	 */
	public abstract ISip getSIP();

	/**
	 * Replaces the predicate of a given literal in the head. <b>This method is
	 * slow</b>, because it copies the head and the body for each invokation.
	 * 
	 * @param l
	 *            the literal of which to replace the predicate
	 * @param p
	 *            the new predicate.
	 * @throws NullPointerException
	 *             if the literal or the predicate are null.
	 * @throws IllegalArgumentException
	 *             if the arity of the predicate of the literal and the new
	 *             predicate doesn't match
	 * @throws IllegalArgumentException
	 *             if the literal couldn't be found in the head
	 */
	public abstract void replaceHeadLiteral(final ILiteral l, final IPredicate p);

	/**
	 * Replaces the predicate of a given literal in the body. <b>This method is
	 * slow</b>, because it copies the head and the body for each invokation.
	 * 
	 * @param l
	 *            the literal of which to replace the predicate
	 * @param p
	 *            the new predicate.
	 * @throws NullPointerException
	 *             if the literal or the predicate are null.
	 * @throws IllegalArgumentException
	 *             if the arity of the predicate of the literal and the new
	 *             predicate doesn't match
	 * @throws IllegalArgumentException
	 *             if the literal couldn't be found in the body
	 */
	public abstract void replaceBodyLiteral(final ILiteral l, final IPredicate p);

}