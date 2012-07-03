package at.sti2.streamingiris;

public class HostPortPair {

	private String host;
	private int port;

	public HostPortPair(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (obj.getClass() != getClass())
			return false;

		HostPortPair pair = (HostPortPair) obj;

		return pair.getHost().equals(host) && pair.getPort() == port;
	}
}
