package org.deri.iris.api.terms.concrete;

import org.deri.iris.api.terms.IStringTerm;

/**
 * 
 * Definition: hexBinary represents arbitrary hex-encoded binary data. The
 * ·value space· of hexBinary is the set of finite-length sequences of binary
 * octets. 3.2.15.1 Lexical Representation
 * 
 * hexBinary has a lexical representation where each binary octet is encoded as
 * a character tuple, consisting of two hexadecimal digits ([0-9a-fA-F])
 * representing the octet code. For example, "0FB7" is a hex encoding for the
 * 16-bit integer 4023 (whose binary representation is 111110110111).
 * 
 * <pre>
 *    Created on 04.04.2006
 *    Committed by $Author: richardpoettler $
 *    $Source: /tmp/iris-cvsbackup/iris/api/org/deri/iris/api/terms/concrete/IHexBinary.java,v $,
 * </pre>
 * 
 * @author Richard Pöttler
 * 
 * @version $Revision: 1.2 $ $Date: 2006-05-30 14:56:18 $
 */
public interface IHexBinary extends IStringTerm<IHexBinary> {
}
