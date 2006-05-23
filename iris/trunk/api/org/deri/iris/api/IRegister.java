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

package org.deri.iris.api;

import java.util.Set;

import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.terms.concrete.IIri;

/**
 * 
 * Interface or class description
 *
 * <pre>
 * Created on 05.05.2006
 * Committed by Graham Hench
 * </pre>
 *
 * @author Graham Hench
 *
 * @version $Revision: 1.2 $ $Date: 2006-05-23 15:05:26 $
 */
public interface IRegister {
	
	public void register(IIri ontoID, Set<IRule> rules);
	
	public void deRegister(IIri ontoID);
	

}
