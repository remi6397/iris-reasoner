/**
 * 
 */
package org.deri.iris.terms.concrete;

import java.net.URI;

import org.deri.iris.api.terms.IConcreteTerm;
import org.deri.iris.api.terms.concrete.IList;
import org.deri.iris.terms.AbstractConcreteTermTest;


public class ListTest extends AbstractConcreteTermTest {


	@Override
	protected IConcreteTerm createBasic() {
		return new org.deri.iris.terms.concrete.List(new IntTerm(2), new IntTerm(3));	 
	}

	@Override
	protected String createBasicString() {
		return "[2,3]";
	}

	@Override
	protected IConcreteTerm createEqual() {
		return new org.deri.iris.terms.concrete.List(new IntTerm(2), new IntTerm(3));	 
	}

	@Override
	protected String createEqualString() {
		return "[2,3]";
	}

	@Override
	protected IConcreteTerm createGreater() {
		return new org.deri.iris.terms.concrete.List(new IntTerm(3), new IntTerm(3), new org.deri.iris.terms.concrete.List(new IntTerm(4)));
	}

	@Override
	protected String createGreaterString() {
		return "[3,3,[4]]";
	}

	@Override
	protected URI getDatatypeIRI() {
		return URI.create(IList.DATATYPE_URI);
	}

	

}
