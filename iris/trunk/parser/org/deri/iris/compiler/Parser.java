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
package org.deri.iris.compiler;

import org.deri.iris.api.basics.IAtom;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.IProgram;

/**
 * <p>
 * Parser interface to parse a datalog program stirng.
 * </p>
 * <p>
 * $Id: Parser.java,v 1.5 2007-04-05 16:45:30 poettler_ric Exp $
 * </p>
 * @author Francisco Garcia
 * @author Richard Pöttler, richard dot poettler at deri dot org
 * @version $Revision: 1.5 $
 */
public interface Parser {

    /**
     * @param reader Reader containing a String adhering to above grammar
     * @param rs non null RuleSet that programm gets inserted
     * @throws Exception
     */
    public void compileKB(String kb, IProgram p);

    public IRule compileRule(String rule) throws Exception;

    public IAtom compileFact(String fact) throws Exception;

}
