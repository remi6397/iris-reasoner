/*
 * MINS (Mins Is Not Silri) A Prolog Egine based on the Silri  
 * 
 * Copyright (C) 1999-2005  Juergen Angele and Stefan Decker
 *                          University of Innsbruck, Austria  
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.deri.mins.compiler;

import org.deri.mins.*;

/**
 * Interface or class description
 *
 * <pre>
 * Created on 16.11.2005
 * Committed by $Author: darko $
 * $Source: /tmp/iris-cvsbackup/iris/parser/org/deri/mins/compiler/Parser.java,v $,
 * </pre>
 *
 * @author Holger Lausen
 *
 * @version $Revision: 1.1 $ $Date: 2006-07-19 10:09:51 $
 */
public interface Parser {

    /**
     * This method compiles a RuleSet from a given test input according 
     * to the following grammar:
     *      expr         = {rule} predicate t_impliedby conjunction t_dot |
     {fact} predicate t_dot |
     {query} t_query conjunction t_dot;
     
     conjunction  = {body} body |
     conjunction t_and body;
     
     body         = {negated} t_not predicate |
     predicate;
     
     predicate    = id paramlist?;
     
     id           = t_alpha t_alphanum*;
     
     paramlist    = t_lpar termlist? t_rpar;
     
     termlist     = {term} term |
     termlist t_comma term;
     
     term         = id paramlist? | //constant or fsymbol
     {var} t_variable;
     
     * @param reader Reader containing a String adhering to above grammar
     * @param rs non null RuleSet that programm gets inserted
     * @throws Exception
     */
    public void compileKB(String kb, RuleSet rs) throws Exception;

    public void setSymbolMap(SymbolMap sm);

    public SymbolMap getSymbolMap();

    public Rule compileRule(String rule) throws Exception;

    public Fact compileFact(String fact) throws Exception;

}