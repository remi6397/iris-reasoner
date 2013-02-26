package at.sti2.streamingiris.performance;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.log4j.Logger;

public class PerformanceTestEndlessCartesianProductInputStreamer extends Thread {

	static Logger logger = Logger
			.getLogger(PerformanceTestEndlessCartesianProductInputStreamer.class);

	private int port = 0;
	private Socket sock = null;
	private long delay;
	private long time;

	/**
	 * Constructor.
	 * 
	 * @param port
	 *            The port to which the streamer sends the data.
	 * @param fileName
	 *            The file name of the datalog program.
	 */
	public PerformanceTestEndlessCartesianProductInputStreamer(int port,
			long delay, long time) {
		this.port = port;
		this.delay = delay;
		this.time = time;
	}

	public void run() {

		int i = 0;

		try {
			long endtime = System.currentTimeMillis() + time;
			String factString;

			logger.info("Beginning of streaming.");

			while (System.currentTimeMillis() < endtime) {
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
