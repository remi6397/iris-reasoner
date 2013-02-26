package at.sti2.streamingiris.performance;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.log4j.Logger;

public class PerformanceTestCartesianProductInputStreamer extends Thread {

	static Logger logger = Logger
			.getLogger(PerformanceTestCartesianProductInputStreamer.class);

	private int port = 0;
	private Socket sock = null;
	private long delay;
	private long elements;

	/**
	 * Constructor.
	 * 
	 * @param port
	 *            The port to which the streamer sends the data.
	 * @param fileName
	 *            The file name of the datalog program.
	 */
	public PerformanceTestCartesianProductInputStreamer(int port, long delay,
			long elements) {
		this.port = port;
		this.delay = delay;
		this.elements = elements;
	}

	public void run() {

		int i = 0;

		try {
			String factString;

			logger.info("Beginning of streaming.");

			while (i < elements) {
				i++;
				sock = new Socket("localhost", port);
				PrintWriter streamWriter = new PrintWriter(
						sock.getOutputStream());

				// generate new fact
				factString = "p(" + i + ").";

				streamWriter.println(factString);

				streamWriter.flush();
				streamWriter.close();
				sock.close();
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					break;
				}
			}

			logger.info("End of streaming.");
			logger.info("Streamed " + i + " facts.");

			logger.info("Disconnected.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
