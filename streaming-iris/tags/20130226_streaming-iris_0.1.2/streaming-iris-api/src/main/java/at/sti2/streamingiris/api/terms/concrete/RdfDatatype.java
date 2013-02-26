package at.sti2.streamingiris.api.terms.concrete;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Defines the RDF data types.
 * 
 * @author Adrian Marte
 */
public enum RdfDatatype {

	/**
	 * The rdf:PlainLiteral (former known as rdf:text) data type.
	 */
	PLAIN_LITERAL("PlainLiteral"),

	/**
	 * The rdf:XMLLiteral data type.
	 */
	XML_LITERAL("XMLLiteral");

	/**
	 * The RDF namespace.
	 */
	private static final String RDF_NAMESPACE = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";

	private String uri;

	private final String namespace;

	private final String name;

	private RdfDatatype(String name) {
		this(RDF_NAMESPACE, name);
	}

	private RdfDatatype(String namespace, String name) {
		this.namespace = namespace;
		this.name = name;
		this.uri = namespace + name;
	}

	public String getUri() {
		return this.uri;
	}

	public String getNamespace() {
		return namespace;
	}

	public String getName() {
		return name;
	}

	public boolean isSameDatatype(String iri) {
		return getUri().equals(iri);
	}

	@Override
	public String toString() {
		return getUri();
	}

	public URI toUri() {
		return URI.create(getUri());
	}

	public static RdfDatatype forUri(String uri) {
		return Lookup.table.get(uri);
	}

	public static boolean isDatatype(String uri) {
		return Lookup.table.containsKey(uri);
	}

	/**
	 * Maps the URI of each datatype to the datatype itself.
	 */
	private static class Lookup {

		private static final Map<String, RdfDatatype> table;

		static {
			table = new HashMap<String, RdfDatatype>();

			for (RdfDatatype builtin : RdfDatatype.values()) {
				table.put(builtin.getUri(), builtin);
			}
		}

	}

}
