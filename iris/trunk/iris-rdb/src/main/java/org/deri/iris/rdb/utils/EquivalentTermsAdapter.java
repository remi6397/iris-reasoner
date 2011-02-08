package org.deri.iris.rdb.utils;

import java.util.Set;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.factory.Factory;
import org.deri.iris.storage.IRelation;
import org.deri.iris.utils.equivalence.IEquivalentTerms;

public class EquivalentTermsAdapter implements IEquivalentTerms {

	private IRelation equivalenceRelation;

	public EquivalentTermsAdapter(IRelation equivalenceRelation) {
		this.equivalenceRelation = equivalenceRelation;
	}

	@Override
	public boolean areEquivalent(ITerm x, ITerm y) {
		ITuple tuple = Factory.BASIC.createTuple(x, y);
		return equivalenceRelation.contains(tuple);
	}

	@Override
	public void setEquivalent(ITerm x, ITerm y) {
		ITuple tuple = Factory.BASIC.createTuple(x, y);
		equivalenceRelation.add(tuple);
	}

	@Override
	public ITerm findRepresentative(ITerm term) {
		return null;
	}

	@Override
	public Set<ITerm> getEquivalent(ITerm term) {
		return null;
	}
	
	@Override
	public String toString() {
		return equivalenceRelation.toString();
	}

}
