package at.sti2.streamingiris.performance;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.log4j.Logger;

public class PerformanceTestFileInputStreamer implements
		PerformanceTestInputStreamer {

	static Logger logger = Logger
			.getLogger(PerformanceTestFileInputStreamer.class);

	private String fileName = null;

	private int port = 0;
	private Socket sock = null;

	private long delay;

	/**
	 * Constructor.
	 * 
	 * @param port
	 *            The port to which the streamer sends the data.
	 * @param fileName
	 *            The file name of the datalog program.
	 */
	public PerformanceTestFileInputStreamer(int port, String fileName,
			long delay) {
		this.port = port;
		this.fileName = fileName;
		this.delay = delay;
	}

	public boolean stream() {

		int i = 0;

		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(
					fileName));
			logger.info("Beginning of streaming.");

			String factLine = bufferedReader.readLine();

			while (factLine != null) {
				i++;
				sock = new Socket("localhost", port);
				PrintWriter streamWriter = new PrintWriter(
						sock.getOutputStream());
				while (factLine != null && !factLine.isEmpty()) {
					streamWriter.println(factLine);
					factLine = bufferedReader.readLine();
				}
				streamWriter.flush();
				streamWriter.close();
				sock.close();
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					break;
				}
				factLine = bufferedReader.readLine();
			}

			logger.info("End of streaming.");
			logger.info("Streamed " + i + " facts.");

			bufferedReader.close();

			logger.info("Disconnected.");

			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}
}
