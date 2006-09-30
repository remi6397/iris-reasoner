package org.deri.iris.operations.tuple;

import org.deri.iris.api.basics.IAtom;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.factory.ITupleOperationsFactory;
import org.deri.iris.api.operations.tuple.IConcatenation;
import org.deri.iris.api.operations.tuple.IMatching;
import org.deri.iris.api.operations.tuple.IUnification;

public class TupleOperationsFactory implements ITupleOperationsFactory{

	private static final ITupleOperationsFactory FACTORY = new TupleOperationsFactory();
	
	private TupleOperationsFactory() {
		// this is a singelton
	}
	
	public static ITupleOperationsFactory getInstance() {
		return FACTORY;
	}
	
	public IConcatenation createConcatenationOperator(ITuple arg0, ITuple arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	public IConcatenation createConcatenationOperator(ITuple arg0, ITuple arg1, int[] pi) {
		// TODO Auto-generated method stub
		return null;
	}

	public IMatching createMatchingOperator(ITuple arg0, ITuple arg1) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public IUnification createUnificationOperator(ITuple arg0, ITuple arg1) {
		return new Unification(arg0, arg1);
	}

	public IUnification createUnificationOperator(IAtom arg0, IAtom arg1) {
		return new Unification(arg0, arg1);
	}
}
