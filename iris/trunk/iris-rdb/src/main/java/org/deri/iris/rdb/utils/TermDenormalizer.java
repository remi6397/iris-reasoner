package org.deri.iris.rdb.utils;

import org.deri.iris.api.factory.IConcreteFactory;
import org.deri.iris.api.factory.ITermFactory;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.factory.Factory;

import at.sti2.rif4j.condition.Constant;
import at.sti2.rif4j.translator.iris.mapper.RifToIrisConstantMapper;

public class TermDenormalizer {

	private RifToIrisConstantMapper mapper;

	public TermDenormalizer() {
		this(Factory.TERM, Factory.CONCRETE);
	}

	public TermDenormalizer(ITermFactory termFactory, IConcreteFactory factory) {
		mapper = new RifToIrisConstantMapper(termFactory, factory);
	}

	public ITerm createTerm(String value, String type) {
		Constant constant = new Constant(type, "", value);
		ITerm term = mapper.toIrisTerm(constant);

		if (term == null) {
			term = Factory.TERM.createString(value);
		}

		return term;
	}

}
