package org.deri.iris.api.terms.concrete;

import org.deri.iris.api.terms.IStringTerm;

/**
 * Definition: base64Binary represents Base64-encoded arbitrary binary data. The
 * ·value space· of base64Binary is the set of finite-length sequences of binary
 * octets. For base64Binary data the entire binary stream is encoded using the
 * Base64 Alphabet in [RFC 2045].
 * 
 * The lexical forms of base64Binary values are limited to the 65 characters of
 * the Base64 Alphabet defined in [RFC 2045], i.e., a-z, A-Z, 0-9, the plus sign
 * (+), the forward slash (/) and the equal sign (=), together with the
 * characters defined in [XML 1.0 (Second Edition)] as white space. No other
 * characters are allowed.
 * 
 * <pre>
 *      Created on 04.04.2006
 *      Committed by $Author: darko $
 *      $Source: /tmp/iris-cvsbackup/iris/api/org/deri/iris/api/terms/concrete/IBase64Binary.java,v $,
 * </pre>
 * 
 * @author Richard Pöttler AuthorLastName
 * 
 * @version $Revision: 1.1 $ $Date: 2006-05-23 13:09:02 $
 */
public interface IBase64Binary extends IStringTerm<IBase64Binary>, Cloneable {
}
