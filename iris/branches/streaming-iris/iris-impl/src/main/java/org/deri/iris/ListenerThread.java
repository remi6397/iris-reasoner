package org.deri.iris;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ListenerThread extends Thread {
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
	public ListenerThread(ServerSocket server) {
		this.server = server;
	}

	public void run() {
		try {
			Socket sock = null;
			while (!Thread.interrupted()) {
				logger.info("Waiting for connection...");
				sock = server.accept();
				logger.info("Connected: " + sock);
				BufferedReader streamReader = new BufferedReader(
						new InputStreamReader(sock.getInputStream()));

				String factLine = null;

				while ((factLine = streamReader.readLine()) != null) {
					logger.debug(factLine);
				}

				streamReader.close();
			}

			if (sock != null) {
				sock.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
