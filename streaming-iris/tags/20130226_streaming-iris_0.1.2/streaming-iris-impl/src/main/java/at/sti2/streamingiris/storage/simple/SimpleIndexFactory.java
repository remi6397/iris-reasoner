package at.sti2.streamingiris.storage.simple;

import at.sti2.streamingiris.storage.IIndex;
import at.sti2.streamingiris.storage.IIndexFactory;
import at.sti2.streamingiris.storage.IRelation;
import at.sti2.streamingiris.utils.equivalence.IEquivalentTerms;

/**
 * Factory for simple indexes.
 */
public class SimpleIndexFactory implements IIndexFactory {

	public IIndex createIndex(IRelation relation, int... indices) {
		return new SimpleIndex(relation, indices);
	}

	public IIndex createIndex(IRelation relation,
			IEquivalentTerms equivalentTerms, int... indices) {
		return new SimpleIndex(relation, equivalentTerms, indices);
	}

}
