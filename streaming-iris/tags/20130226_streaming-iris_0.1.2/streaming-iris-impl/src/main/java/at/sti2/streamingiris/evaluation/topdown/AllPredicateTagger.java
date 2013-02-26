package at.sti2.streamingiris.evaluation.topdown;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.basics.IRule;

public class AllPredicateTagger implements IPredicateTagger {

	private List<IRule> mRules;

	public AllPredicateTagger(List<IRule> rules) {
		mRules = rules;
	}

	public Set<IPredicate> getMemoPredicates() {
		Set<IPredicate> memoPredicates = new HashSet<IPredicate>();
		for (IRule r : mRules) {
			IPredicate rp = r.getHead().get(0).getAtom().getPredicate();
			memoPredicates.add(rp);
		}

		return memoPredicates;
	}

}
