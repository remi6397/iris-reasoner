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
package org.deri.iris.compiler;

import java.io.*;

import org.deri.iris.parser.lexer.Lexer;
import org.deri.iris.parser.lexer.LexerException;
import org.deri.iris.parser.node.Node;
import org.deri.iris.parser.parser.Parser;
import org.deri.iris.parser.parser.ParserException;
import org.deri.mins.*;

/**
 * Interface or class description
 *
 * <pre>
 * Created on 14.11.2005
 * Committed by $Author: darko $
 * $Source: /tmp/iris-cvsbackup/iris/parser/org/deri/iris/compiler/ParserImpl.java,v $,
 * </pre>
 *
 * @author Holger Lausen
 *
 * @version $Revision: 1.1 $ $Date: 2006-07-19 10:12:44 $
 */
public class ParserImpl implements org.deri.iris.compiler.Parser {
     
    public SymbolMap sm = new SymbolMap();
    
    /* (non-Javadoc)
     * @see org.deri.iris.compiler.Parser#compileKB(java.lang.StringBuffer, org.deri.mins.RuleSet)
     */
    public void compileKB(String kb, RuleSet rs) throws Exception {
        generateParseTree(kb).apply(new TreeWalker(rs, sm));
    }
    
    /* (non-Javadoc)
     * @see org.deri.iris.compiler.Parser#setSymbolMap(org.deri.iris.compiler.SymbolMap)
     */
    public void setSymbolMap(SymbolMap sm){
        this.sm = sm;
    }
    
    /* (non-Javadoc)
     * @see org.deri.iris.compiler.Parser#getSymbolMap()
     */
    public SymbolMap getSymbolMap(){
        return sm;
    }
 
    /* (non-Javadoc)
     * @see org.deri.iris.compiler.Parser#compileRule(java.lang.String)
     */
    public Rule compileRule(String rule) throws Exception{
        TreeWalker tw = new TreeWalker(null,sm);
        generateParseTree(rule).apply(tw);
        return tw.getLastRule();
    }
    
    /* (non-Javadoc)
     * @see org.deri.iris.compiler.Parser#compileFact(java.lang.String)
     */
    public Fact compileFact(String fact) throws Exception{
        TreeWalker tw = new TreeWalker(null,sm);
        generateParseTree(fact).apply(tw);
        return tw.getLastFact();
    }
    
    private Node generateParseTree(String reader) 
            throws ParserException, LexerException{
        StringReader r = new StringReader(reader);
        Lexer lexer = new Lexer(new PushbackReader(r, 16384));
        Parser parser = new Parser(lexer);
        try {
            return parser.parse();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
