/*
 * Copyright 2011, Semantic Technology Institute (STI) Innsbruck, 
 * University of Innsbruck, Technikerstrasse 21a, 6020 Innsbruck, Austria.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.deri.iris.api.terms.concrete;

import org.deri.iris.api.terms.IConcreteTerm;

/**
 * <p>
 * Definition: hexBinary represents arbitrary hex-encoded binary data. The
 * ·value space· of hexBinary is the set of finite-length sequences of binary
 * octets. 3.2.15.1 Lexical Representation
 * </p>
 * <p>
 * hexBinary has a lexical representation where each binary octet is encoded as
 * a character tuple, consisting of two hexadecimal digits ([0-9a-fA-F])
 * representing the octet code. For example, "0FB7" is a hex encoding for the
 * 16-bit integer 4023 (whose binary representation is 111110110111).
 * </p>
 * <p>
 * Remark: IRIS supports datatypes according to the standard 
 * specification for primitive XML Schema datatypes.
 * </p>
 * <pre>
 *    Created on 04.04.2006
 *    Committed by $Author: bazbishop237 $
 *    $Source: /tmp/iris-cvsbackup/iris/api/org/deri/iris/api/terms/concrete/IHexBinary.java,v $,
 * </pre>
 * 
 * @author Richard Pöttler
 * 
 * @version $Revision: 1.5 $ $Date: 2007-10-09 20:21:21 $
 */
public interface IHexBinary extends IConcreteTerm
{
	/**
	 * Return the wrapped type.
	 */
	public String getValue();
}
