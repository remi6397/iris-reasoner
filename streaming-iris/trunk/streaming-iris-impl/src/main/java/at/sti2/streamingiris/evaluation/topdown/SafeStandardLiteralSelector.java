package at.sti2.streamingiris.evaluation.topdown;

import java.util.List;
import java.util.Set;

import at.sti2.streamingiris.api.basics.ILiteral;
import at.sti2.streamingiris.api.builtins.IBuiltinAtom;
import at.sti2.streamingiris.api.terms.IVariable;
import at.sti2.streamingiris.builtins.EqualBuiltin;

/**
 * Safe Standard Literal Selector. Always selects the most-left positive or
 * negative grounded literal.
 * 
 * @author gigi
 * 
 */
public class SafeStandardLiteralSelector implements ILiteralSelector {

	public ILiteral select(List<ILiteral> list) {
		if (list.isEmpty())
			return null;

		for (ILiteral lit : list) {
			Set<IVariable> variables = lit.getAtom().getTuple().getVariables();

			if (lit.isPositive()) {

				if (lit.getAtom() instanceof IBuiltinAtom) {
					IBuiltinAtom builtinAtom = (IBuiltinAtom) lit.getAtom();

					if (builtinAtom instanceof EqualBuiltin) {
						// select it
					} else if (builtinAtom.getTuple().getVariables().size() > builtinAtom
							.maxUnknownVariables()) {
						// try next literal
						continue;
					}
				}

				return lit; // positive literal

			} else if (variables.isEmpty()) {
				return lit; // negative grounded literal
			}
		}

		return null; // literal selection not possible
	}

}
