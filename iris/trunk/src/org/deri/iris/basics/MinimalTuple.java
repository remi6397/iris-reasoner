package org.deri.iris.basics;

import java.util.List;

import org.deri.iris.api.terms.ITerm;

/**
 * @author Darko Anicic, DERI Innsbruck
 * @date   02.06.2006 18:45:38
 */

/**
 * MinimalTuple is a class used for selecting a portion of a tree (relation)
 * that is greater than the MinimalTuple. Instances of this class are used 
 * to distinguish between ordinary tuples of the relation (nodes of the tree)
 * and an artificially made tuple (MinimalTuple) that is created for 
 * selecting purposes.
 */
public class MinimalTuple extends Tuple{

	public MinimalTuple(List<ITerm> terms) {
		super(terms);
	}

}
