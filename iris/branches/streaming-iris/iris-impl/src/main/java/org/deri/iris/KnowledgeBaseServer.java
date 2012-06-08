package org.deri.iris;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This thread waits for a connection to a specified port and starts a new
 * thread as soon as the connection is established.
 * 
 * @author norlan
 * 
 */
public class KnowledgeBaseServer extends Thread {

	private Logger logger = LoggerFactory.getLogger(getClass());
	private KnowledgeBase knowledgeBase;
	private int port;
	private ServerSocket server;

	/**
	 * Constructor.
	 * 
	 * @param knowledgeBase
	 *            The knowledge base that processes the incoming data.
	 * @param port
	 *            The port of the socket to listen on.
	 */
	public KnowledgeBaseServer(KnowledgeBase knowledgeBase, int port) {
		this.knowledgeBase = knowledgeBase;
		this.port = port;
	}

	public void run() {
		try {
			server = new ServerSocket(port);
			Thread inputThread = null;
			logger.info("Server: " + server);

			while (!Thread.interrupted()) {
				logger.info("Waiting for connection...");
				Socket sock = server.accept();
				logger.info("Connected: " + sock);
				inputThread = new Thread(new KnowledgeBaseServerThread(
						knowledgeBase, sock), "Input thread");
				inputThread.start();
			}
			if (inputThread != null) {
				inputThread.interrupt();
			}
		} catch (SocketException e) {
			logger.debug(e.getMessage());
			logger.info("KnowledgeBaseServer shut down!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void shutdown() {
		try {
			server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
