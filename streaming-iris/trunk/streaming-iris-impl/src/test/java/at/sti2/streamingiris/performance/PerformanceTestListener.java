package at.sti2.streamingiris.performance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.sti2.streamingiris.CountProfiler;

public class PerformanceTestListener extends Thread {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private ServerSocket server;

	/**
	 * Constructor.
	 * 
	 * @param knowledgeBase
	 *            The knowledge base that processes the incoming data.
	 * @param port
	 *            The port of the socket to listen on.
	 */
	public PerformanceTestListener(ServerSocket server) {
		this.server = server;
	}

	public void run() {
		try {
			int i = 0;
			Socket sock = null;
			while (!Thread.interrupted()) {
				sock = server.accept();

				CountProfiler.receivedResult();

				BufferedReader streamReader = new BufferedReader(
						new InputStreamReader(sock.getInputStream()));

				String factLine = null;

				while ((factLine = streamReader.readLine()) != null) {
					// logger.debug("{}: {}", i, factLine);
				}

				streamReader.close();
				i++;
			}

			if (sock != null) {
				sock.close();
			}

		} catch (IOException e) {
		}
	}

	public boolean shutdown() {
		try {
			if (server != null)
				server.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}
}
