/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions by 
 * built-in predicates, default negation (under well-founded semantics), 
 * function symbols and contexts. 
 * 
 * Copyright (C) 2006  Digital Enterprise Research Institute (DERI), 
 * Leopold-Franzens-Universitaet Innsbruck, Technikerstrasse 21a, 
 * A-6020 Innsbruck. Austria.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 */
package org.deri.iris.dbstorage;

import java.util.Map;
import java.util.Set;

import org.deri.iris.api.IProgram;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.factory.IProgramFactory;
import org.deri.iris.api.storage_old.IMixedDatatypeRelation;

/**
 * <p>
 * A simple IProgramFactory implementation.
 * </p>
 * <p>
 * $Id: ProgramFactory.java,v 1.5 2007-10-09 20:33:43 bazbishop237 Exp $
 * </p>
 * @author Francisco Garcia
 * @author Darko Anicic, DERI Innsbruck
 * @author Richard Pöttler (richard dot poettler at deri dot org)
 * @version $Revision: 1.5 $
 */
public class ProgramFactory implements IProgramFactory {
	private static final IProgramFactory FACTORY = new ProgramFactory();
	private Map<?,?> conf=null;
	
	private ProgramFactory() {
		// this is a singelton
	}

	public static IProgramFactory getInstance() {
		return FACTORY;
	}

	public IProgram createProgram() {
		return new Program(conf);
	}
	
	public IProgram createProgram(Map<IPredicate, IMixedDatatypeRelation> f, Set<IRule> r, Set<IQuery> q) {
		return new Program(conf,f, r, q);
	}
	
	public void setDbConfigurationFile(Map<?,?> conf){
		this.conf=conf;
	}
}
