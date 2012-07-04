/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions.
 * 
 * Copyright (C) 2008 Semantic Technology Institute (STI) Innsbruck, 
 * University of Innsbruck, Technikerstrasse 21a, 6020 Innsbruck, Austria.
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
package at.sti2.streamingiris.evaluation.stratifiedbottomup.naive;

import java.util.List;


import at.sti2.streamingiris.Configuration;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.evaluation.stratifiedbottomup.IRuleEvaluator;
import at.sti2.streamingiris.facts.IFacts;
import at.sti2.streamingiris.rules.compiler.ICompiledRule;
import at.sti2.streamingiris.storage.IRelation;

/**
 * Naive evaluation. see Ullman, Vol. 1
 */
public class NaiveEvaluator implements IRuleEvaluator {
	// private Logger logger = LoggerFactory.getLogger(getClass());

	public void evaluateRules(List<ICompiledRule> rules, IFacts facts,
			Configuration configuration, long timestamp)
			throws EvaluationException {
		boolean cont = true;
		while (cont) {
			cont = false;

			// For each rule in the collection (stratum)
			for (final ICompiledRule rule : rules) {
				IRelation delta = rule.evaluate();

				if (delta != null && delta.size() > 0) {
					IPredicate predicate = rule.headPredicate();

					if (facts.get(predicate).addAll(delta, timestamp))
						cont = true;

					// TODO Norbert: logging
					// if (logger.isDebugEnabled()) {
					// for (int i = 0; i < delta.size(); i++) {
					// logger.debug("Created fact [" + timestamp + "]: "
					// + predicate + " " + delta.get(i));
					// }
					// }
				}
			}
		}
	}
}
