package org.deri.iris.threads;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.deri.iris.KnowledgeBase;
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
	private List<Thread> inputThreads;

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
		this.inputThreads = new ArrayList<Thread>();
	}

	public void run() {
		try {
			server = new ServerSocket(port);
			logger.info("Server: " + server);

			Thread inputThread;
			while (!Thread.interrupted()) {
				logger.info("Waiting for connection...");
				Socket sock = server.accept();
				inputThread = new Thread(new KnowledgeBaseServerThread(
						knowledgeBase, sock), "Input thread");
				inputThread.start();
				inputThreads.add(inputThread);
				logger.info("Connected: " + sock);
			}
		} catch (IOException e) {
			// for (Thread thread : inputThreads) {
			// thread.interrupt();
			// }
			// logger.info("KnowledgeBaseServer shut down!");
		}
	}

	public boolean shutdown() {
		try {
			if (server != null)
				server.close();
			for (Thread thread : inputThreads) {
				thread.interrupt();
			}
			logger.info("KnowledgeBaseServer shut down!");
			return true;
		} catch (IOException e) {
			return false;
		}
	}
}
