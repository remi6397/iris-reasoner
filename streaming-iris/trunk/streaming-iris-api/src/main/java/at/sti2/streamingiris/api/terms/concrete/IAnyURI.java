package at.sti2.streamingiris.api.terms.concrete;

import java.net.URI;

import at.sti2.streamingiris.api.terms.IConcreteTerm;

/**
 * <p>
 * Represents the XML Schema datatype xsd:anyURI.
 * </p>
 * <p>
 * xsd:anyURI represents a Uniform Resource Identifier Reference (URI). An
 * xsd:anyURI value can be absolute or relative, and may have an optional
 * fragment identifier (i.e., it may be a URI Reference). This type should be
 * used to specify the intention that the value fulfills the role of a URI as
 * defined by RFC 2396, as amended by RFC 2732.
 * </p>
 * 
 * @author Adrian Marte
 */
public interface IAnyURI extends IConcreteTerm {

	/**
	 * Returns the URI representing this anyURI.
	 * 
	 * @return The URI representing this anyURI.
	 */
	public URI getValue();

}
