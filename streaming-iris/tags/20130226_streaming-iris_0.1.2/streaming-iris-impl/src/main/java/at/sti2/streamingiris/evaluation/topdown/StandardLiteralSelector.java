package at.sti2.streamingiris.evaluation.topdown;

import java.util.List;

import at.sti2.streamingiris.api.basics.ILiteral;

/**
 * Standard Literal Selector. Always selects the most-left literal.
 * 
 * @author gigi
 * 
 */
public class StandardLiteralSelector implements ILiteralSelector {

	public ILiteral select(List<ILiteral> list) {
		if (list.isEmpty())
			return null;
		return list.get(0);
	}

}
