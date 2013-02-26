package at.sti2.streamingiris.factory;

import at.sti2.streamingiris.api.factory.IBasicFactory;
import at.sti2.streamingiris.api.factory.IBuiltinsFactory;
import at.sti2.streamingiris.api.factory.IConcreteFactory;
import at.sti2.streamingiris.api.factory.IGraphFactory;
import at.sti2.streamingiris.api.factory.ITermFactory;
import at.sti2.streamingiris.basics.BasicFactory;
import at.sti2.streamingiris.builtins.BuiltinsFactory;
import at.sti2.streamingiris.graph.GraphFactory;
import at.sti2.streamingiris.terms.TermFactory;
import at.sti2.streamingiris.terms.concrete.ConcreteFactory;

/**
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @author Darko Anicic, DERI Innsbruck
 */
public class Factory {
	public static final IBasicFactory BASIC = BasicFactory.getInstance();

	public static final ITermFactory TERM = TermFactory.getInstance();

	public static final IConcreteFactory CONCRETE = ConcreteFactory
			.getInstance();

	public static final IGraphFactory GRAPH = GraphFactory.getInstance();

	public static final IBuiltinsFactory BUILTIN = BuiltinsFactory
			.getInstance();
}
