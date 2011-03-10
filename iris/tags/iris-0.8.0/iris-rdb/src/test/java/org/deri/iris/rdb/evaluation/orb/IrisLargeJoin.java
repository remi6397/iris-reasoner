package org.deri.iris.rdb.evaluation.orb;

import java.util.List;
import java.util.Map;

import org.deri.iris.Configuration;
import org.deri.iris.EvaluationException;
import org.deri.iris.KnowledgeBase;
import org.deri.iris.api.IKnowledgeBase;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.optimisations.magicsets.MagicSets;
import org.deri.iris.optimisations.rulefilter.RuleFilter;
import org.deri.iris.storage.IRelation;

public class IrisLargeJoin extends LargeJoin {

	@Override
	protected IKnowledgeBase createKnowledgeBase(
			Map<IPredicate, IRelation> rawFacts, List<IRule> rules)
			throws EvaluationException {
		Configuration configuration = new Configuration();

		configuration.programOptmimisers.add(new RuleFilter());
		configuration.programOptmimisers.add(new MagicSets());

		return new KnowledgeBase(rawFacts, rules, configuration);
	}

	public static void main(String[] args) throws Exception {
		IrisLargeJoin largeJoinTest = new IrisLargeJoin();

		largeJoinTest.join1(LargeJoin.Join1Query.BF_A,
				LargeJoin.Join1Data.DATA0);
	}

}
