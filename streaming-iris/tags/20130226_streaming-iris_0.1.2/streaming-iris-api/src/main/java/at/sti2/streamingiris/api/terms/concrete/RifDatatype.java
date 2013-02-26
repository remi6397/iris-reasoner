package at.sti2.streamingiris.api.terms.concrete;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Defines the list of RIF data types.
 * 
 * @author Adrian Marte
 */
public enum RifDatatype {

	IRI("iri"),

	LOCAL("local");

	/**
	 * The namespace of RIF.
	 */
	public static final String RIF_NAMESPACE = "http://www.w3.org/2007/rif#";

	private String namespace;

	private String name;

	private String uri;

	private RifDatatype(String name) {
		this(RIF_NAMESPACE, name);
	}

	private RifDatatype(String namespace, String name) {
		this.namespace = namespace;
		this.name = name;
		this.uri = namespace + name;
	}

	public String getNamespace() {
		return namespace;
	}

	public String getName() {
		return name;
	}

	public String getUri() {
		return this.uri;
	}

	@Override
	public String toString() {
		return getUri();
	}

	public URI toUri() {
		return URI.create(getUri());
	}

	public static RifDatatype forUri(String uri) {
		return Lookup.table.get(uri);
	}

	public static boolean isDatatype(String uri) {
		return Lookup.table.containsKey(uri);
	}

	/**
	 * Maps the URI of each datatype to the datatype itself.
	 */
	private static class Lookup {

		private static final Map<String, RifDatatype> table;

		static {
			table = new HashMap<String, RifDatatype>();

			for (RifDatatype builtin : RifDatatype.values()) {
				table.put(builtin.getUri(), builtin);
			}
		}

	}

}
