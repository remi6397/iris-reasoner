package at.sti2.streamingiris.facts;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.storage.IRelation;
import at.sti2.streamingiris.storage.IRelationFactory;
import at.sti2.streamingiris.storage.simple.SimpleRelationFactory;

/**
 * A manager for all facts stored in a knowledge-base.
 */
public class Facts implements IFacts {
	// private Logger logger = LoggerFactory.getLogger(getClass());

	/** The map storing the predicate-relation relationship. */
	protected final Map<IPredicate, IRelation> mPredicateRelationMap = new HashMap<IPredicate, IRelation>();

	protected final IRelationFactory mRelationFactory;

	/**
	 * Constructor.
	 */
	public Facts(IRelationFactory relationFactory) {
		mRelationFactory = relationFactory;
	}

	/**
	 * Construct a Facts object from a predicate-relation map.
	 * 
	 * @param rawFacts
	 *            The facts to add.
	 */
	public Facts(Map<IPredicate, IRelation> rawFacts,
			IRelationFactory relationFactory) {
		mRelationFactory = relationFactory;
		mPredicateRelationMap.putAll(rawFacts);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.deri.iris.new_stuff.facts.IFacts#get(org.deri.iris.api.basics.IPredicate
	 * )
	 */
	public IRelation get(IPredicate predicate) {
		IRelation relation = mPredicateRelationMap.get(predicate);

		if (relation == null) {
			relation = mRelationFactory.createRelation();
			mPredicateRelationMap.put(predicate, relation);
		}

		return relation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.deri.iris.new_stuff.facts.IFacts#getPredicates()
	 */
	public Set<IPredicate> getPredicates() {
		return mPredicateRelationMap.keySet();
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();

		for (Map.Entry<IPredicate, IRelation> entry : mPredicateRelationMap
				.entrySet()) {
			IRelation relation = entry.getValue();
			IPredicate predicate = entry.getKey();

			for (int t = 0; t < relation.size(); ++t) {
				ITuple tuple = relation.get(t);
				result.append(predicate.getPredicateSymbol());
				result.append(tuple);
				result.append('.');
			}
		}

		return result.toString();
	}

	@Override
	public void addFacts(Map<IPredicate, IRelation> newFacts, long timestamp) {
		for (Entry<IPredicate, IRelation> entry : newFacts.entrySet()) {
			IPredicate predicate = entry.getKey();
			IRelation relation = entry.getValue();
			if (!mPredicateRelationMap.containsKey(predicate)) {
				mPredicateRelationMap.put(predicate,
						new SimpleRelationFactory().createRelation());
				mPredicateRelationMap.get(predicate)
						.addAll(relation, timestamp);
			} else {
				mPredicateRelationMap.get(predicate)
						.addAll(relation, timestamp);
			}
			// logger.debug("ADDED: " + predicate + " " + relation + "["
			// + timestamp + "]");
		}
	}

	@Override
	public void clean(long timestamp) {
		for (IRelation entry : mPredicateRelationMap.values()) {
			entry.clean(timestamp);
		}
	}
}
