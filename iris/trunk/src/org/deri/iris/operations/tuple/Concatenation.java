package org.deri.iris.operations.tuple;

import java.util.LinkedList;
import java.util.List;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.operations.tuple.IConcatenation;
import org.deri.iris.basics.Tuple;

/**
 * @author Darko Anicic, DERI Innsbruck
 * @date   26.05.2006 11:59:38
 */
public class Concatenation implements IConcatenation{

	/* (non-Javadoc)
	 * @see org.deri.iris.api.operations.tuple.IConcatenation#concatenate(org.deri.iris.api.basics.ITuple, org.deri.iris.api.basics.ITuple)
	 */
	@SuppressWarnings({"unchecked","unchecked"})
	public ITuple concatenate(ITuple arg0, ITuple arg1) {
		List tupleList = new LinkedList();
		tupleList.addAll(arg0.getTerms());
		tupleList.addAll(arg1.getTerms());
		
		return new Tuple(tupleList);
	}

}
