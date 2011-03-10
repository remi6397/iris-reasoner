package org.deri.iris.rdb.evaluation.orb;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.deri.iris.Configuration;
import org.deri.iris.EvaluationException;
import org.deri.iris.api.IKnowledgeBase;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.facts.Facts;
import org.deri.iris.facts.IFacts;
import org.deri.iris.optimisations.magicsets.MagicSets;
import org.deri.iris.optimisations.rulefilter.RuleFilter;
import org.deri.iris.rdb.RdbKnowledgeBase;
import org.deri.iris.storage.IRelation;

public class IrisRdbLargeJoin extends LargeJoin {

	@Override
	protected IKnowledgeBase createKnowledgeBase(
			Map<IPredicate, IRelation> rawFacts, List<IRule> rules)
			throws EvaluationException {
		Configuration configuration = new Configuration();

		configuration.programOptmimisers.add(new RuleFilter());
		configuration.programOptmimisers.add(new MagicSets());

		IFacts facts = new Facts(rawFacts, configuration.relationFactory);

		try {
			return new RdbKnowledgeBase(facts, rules, configuration);
		} catch (IOException e) {
			throw new EvaluationException(
					"Failed to create the database directory");
		} catch (ClassNotFoundException e) {
			throw new EvaluationException("Failed to load database driver");
		} catch (SQLException e) {
			throw new EvaluationException("An SQL error occurred");
		}
	}

	public static void main(String[] args) throws Exception {
		IrisRdbLargeJoin largeJoinTest = new IrisRdbLargeJoin();

		largeJoinTest.join1(LargeJoin.Join1Query.BF_A,
				LargeJoin.Join1Data.DATA0);
	}

}
