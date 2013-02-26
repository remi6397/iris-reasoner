package at.sti2.streamingiris.facts;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;
import at.sti2.streamingiris.storage.IRelation;

public class FactsWithExternalData implements IFacts {
	/**
	 * Constructor.
	 */
	public FactsWithExternalData(IFacts facts,
			List<IDataSource> externalDataSources) {
		mFacts = facts;
		mExternalDataSources = new ArrayList<IDataSource>(externalDataSources);
	}

	public IRelation get(IPredicate predicate) {
		IRelation result = mFacts.get(predicate);

		// If we haven't got the external data for this predicate yet...
		if (mExternalPredicatesAlreadyFetched.add(predicate)) {
			// ... then get it now.

			ITuple from = Factory.BASIC.createTuple(new ITerm[predicate
					.getArity()]);
			ITuple to = Factory.BASIC.createTuple(new ITerm[predicate
					.getArity()]);

			for (IDataSource dataSource : mExternalDataSources)
				dataSource.get(predicate, from, to, result);

		}

		return result;
	}

	public Set<IPredicate> getPredicates() {
		return mFacts.getPredicates();
	}

	/**
	 * Return all facts. The format of the resulting string is parse-able.
	 * 
	 * @return a parse-able string containing all facts
	 */
	public String toString() {
		return mFacts.toString();
	}

	private final IFacts mFacts;

	private final List<IDataSource> mExternalDataSources;

	private Set<IPredicate> mExternalPredicatesAlreadyFetched = new HashSet<IPredicate>();

	@Override
	public void addFacts(Map<IPredicate, IRelation> newFacts, long timestamp) {
		mFacts.addFacts(newFacts, timestamp);
	}

	@Override
	public void clean(long timestamp) {
		mFacts.clean(timestamp);
	}
}
