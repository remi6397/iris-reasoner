package at.sti2.streamingiris;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.Socket;

import org.apache.log4j.Logger;

public class IrisOutputStreamer implements IIrisOutputStreamer {

	static Logger logger = Logger.getLogger(IrisOutputStreamer.class);

	private String host = null;
	private int port = 0;
	private Socket socket = null;

	private PrintWriter streamWriter;

	/**
	 * Constructor.
	 * 
	 * @param host
	 *            The host where the results are sent.
	 * @param port
	 *            The port where the results are sent.
	 */
	public IrisOutputStreamer(String host, int port) {
		this.host = host;
		this.port = port;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.deri.iris.demo.IIrisOutputStreamer#stream(java.lang.String)
	 */
	@Override
	public void stream(String output) {
		try {
			socket = new Socket(host, port);
			streamWriter = new PrintWriter(socket.getOutputStream());
			BufferedReader bufferedReader = new BufferedReader(
					new StringReader(output));
			String factLine = null;

			while ((factLine = bufferedReader.readLine()) != null) {
				streamWriter.println(factLine);
			}

			streamWriter.flush();
			streamWriter.close();

			bufferedReader.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.deri.iris.demo.IIrisOutputStreamer#shutdown()
	 */
	@Override
	public boolean shutdown() {
		boolean result;
		try {
			if (streamWriter != null)
				streamWriter.close();
			if (socket != null)
				socket.close();
		} catch (IOException e) {
			logger.error("IO exception occured!", e);
			e.printStackTrace();
		} finally {
			result = true;
		}
		return result;
	}
}
