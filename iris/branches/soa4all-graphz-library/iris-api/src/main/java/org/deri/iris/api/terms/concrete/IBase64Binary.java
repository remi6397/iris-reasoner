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
 * Definition: base64Binary represents Base64-encoded arbitrary binary data. The
 * ·value space· of base64Binary is the set of finite-length sequences of binary
 * octets. For base64Binary data the entire binary stream is encoded using the
 * Base64 Alphabet in [RFC 2045].
 * </p>
 * <p>
 * The lexical forms of base64Binary values are limited to the 65 characters of
 * the Base64 Alphabet defined in [RFC 2045], i.e., a-z, A-Z, 0-9, the plus sign
 * (+), the forward slash (/) and the equal sign (=), together with the
 * characters defined in [XML 1.0 (Second Edition)] as white space. No other
 * characters are allowed.
 * </p>
 * <p>
 * Remark: IRIS supports datatypes according to the standard 
 * specification for primitive XML Schema datatypes.
 * </p>
 * <pre>
 *      Created on 04.04.2006
 *      Committed by $Author: bazbishop237 $
 *      $Source: /tmp/iris-cvsbackup/iris/api/org/deri/iris/api/terms/concrete/IBase64Binary.java,v $,
 * </pre>
 * 
 * @author Richard Pöttler AuthorLastName
 * 
 * @version $Revision: 1.6 $ $Date: 2007-10-09 20:21:21 $
 */
public interface IBase64Binary extends IConcreteTerm
{
	/**
	 * Return the wrapped type.
	 */
	public String getValue();
}
