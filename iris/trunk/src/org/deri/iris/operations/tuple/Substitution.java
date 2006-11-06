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
package org.deri.iris.operations.tuple;

import org.deri.iris.api.terms.ITerm;


/**
 * @author Darko Anicic, DERI Innsbruck
 * @date   03.08.2006 16:12:01
 */
public class Substitution {
	public ITerm original;
    public ITerm replacement;

    /**
     * Constructor.
     * @param o the original term
     * @param r the term replacing the original term
     */
    public Substitution(ITerm o, ITerm r) {
        original    = o;
        replacement = r;
    }

    /**
     * Compares two objects.
     * @return true if the objects are equal, false otherwise
     * @param obj the object to compare
     */
    public boolean equals(Object obj) {
        if((obj != null) && (obj instanceof Substitution)) {
            Substitution r = (Substitution) obj;

            return(original == null)
                  ? r.original == null
                  : (r.original.equals (original) && (replacement == null))
                    ? r.replacement == null
                    : r.replacement.equals (replacement);
        }

        return false;
    }

    /**
     * Get the hashcode of the object.
     * @return the hash value of this object
     */
    public int hashCode() {
        if(replacement == null) {
            return(original == null)
                  ? 0
                  : original.hashCode ();
        }

        if(original == null) {
            return replacement.hashCode ();
        }

        return replacement.hashCode () ^ original.hashCode () * 31;
    }

    /**
     * Convert the object to a string.
     * @return the string representation of this object
     */
    public String toString() {
        return original.toString () + " / " + replacement.toString ();
    }
}
