package at.sti2.streamingiris.evaluation.topdown;

import java.util.List;

import at.sti2.streamingiris.api.basics.ILiteral;

/**
 * Selects a literal from a list of literals
 * 
 * @author gigi
 */
public interface ILiteralSelector {

	public ILiteral select(List<ILiteral> list);

}
